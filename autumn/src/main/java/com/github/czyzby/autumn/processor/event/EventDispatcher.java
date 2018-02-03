package com.github.czyzby.autumn.processor.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.reflect.Method;
import com.github.czyzby.autumn.annotation.OnEvent;
import com.github.czyzby.autumn.context.Context;
import com.github.czyzby.autumn.context.ContextDestroyer;
import com.github.czyzby.autumn.context.ContextInitializer;
import com.github.czyzby.autumn.context.error.ContextInitiationException;
import com.github.czyzby.autumn.processor.AbstractAnnotationProcessor;
import com.github.czyzby.autumn.processor.event.impl.ReflectionEventListener;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;
import java.util.Iterator;

/** Processes events. Can be injected and used to invoke registered listeners with {@link #postEvent(Object)}. If this
 * processor is not injected into any component, it will mostly likely get garbage-collected after context initiation,
 * even if there are methods or components annotated with {@link OnEvent}.
 *
 * @author MJ */
public class EventDispatcher extends AbstractAnnotationProcessor<OnEvent> {
    private final ObjectMap<Class<?>, SnapshotArray<EventListener<?>>> listeners;
    private final ObjectMap<Class<?>, SnapshotArray<EventListener<?>>> mainThreadListeners;
    private final ObjectSet<Class<?>> asyncEvents;

    public EventDispatcher() {
        ObjectProvider<SnapshotArray<EventListener<?>>> provider =
                new ObjectProvider<SnapshotArray<EventListener<?>>>() {
                    @Override
                    public SnapshotArray<EventListener<?>> provide() {
                        return new SnapshotArray<EventListener<?>>();
                    }
                };
        listeners = LazyObjectMap.newMap(provider);
        mainThreadListeners = LazyObjectMap.newMap(provider);
        asyncEvents = new ObjectSet<Class<?>>();
    }

    @Override
    public Class<OnEvent> getSupportedAnnotationType() {
        return OnEvent.class;
    }

    @Override
    public boolean isSupportingMethods() {
        return true;
    }

    @Override
    public void processMethod(final Method method, final OnEvent annotation, final Object component,
            final Context context, final ContextInitializer initializer, final ContextDestroyer contextDestroyer) {
        addListener(new ReflectionEventListener(method, component, context, annotation.removeAfterInvocation(),
                annotation.strict()), annotation);
    }

    @Override
    public boolean isSupportingTypes() {
        return true;
    }

    @Override
    public void processType(final Class<?> type, final OnEvent annotation, final Object component,
            final Context context, final ContextInitializer initializer, final ContextDestroyer contextDestroyer) {
        if (component instanceof EventListener<?>) {
            addListener((EventListener<?>) component, annotation);
        } else {
            throw new ContextInitiationException("Unable to register listener. " + component
                    + " is annotated with OnEvent, but does not implement EventListener interface.");
        }
    }

    /**
     * @param listener   will be registered.
     * @param annotation contains listener's data.
     */
    public void addListener(final EventListener<?> listener, final OnEvent annotation) {
        addListener(listener, annotation.value(), annotation.forceMainThread());
    }

    /**
     * @param listener will be registered. Will be invoked as soon as the event is posted.
     * @param eventType type of handled events.
     */
    public void addListener(final EventListener<?> listener, final Class<?> eventType) {
        addListener(listener, eventType, false);
    }

    /**
     * @param listener will be registered.
     * @param eventType type of handled events.
     * @param forceMainThread if true, listener will be invoked only on main LibGDX thread with
     * Gdx.app.postRunnable(Runnable). Otherwise the listener is invoked as soon as the event is posted.
     */
    public void addListener(final EventListener<?> listener, final Class<?> eventType, final boolean forceMainThread) {
        if (forceMainThread) {
            mainThreadListeners.get(eventType).add(listener);
        } else {
            listeners.get(eventType).add(listener);
        }
    }

    /**
     * If {@code executeAsync} is false events of eventClass class will be processed consistently.<br>
     * For example, there are two threads call {@link EventDispatcher#postEvent(Object)} on events of identical types,
     * then each listener will process event from first thread and then will process event from second:<br>
     * {@code listener[0].processEvent(event1)}<br>
     * {@code listener[1].processEvent(event1)}<br>
     * {@code listener[0].processEvent(event2)}<br>
     * {@code listener[1].processEvent(event2)}<br>
     * <br>
     * If {@code executeAsync} is true events of identical types from different threads will be processed independent:<br>
     * {@code listener[0].processEvent(event1)}<br>
     * {@code listener[0].processEvent(event2)}<br>
     * {@code listener[1].processEvent(event1)}<br>
     * {@code listener[1].processEvent(event2)}<br>
     * <br>
     * By default events from different threads processing consistently
     * @param eventClass configurable event type
     * @param executeAsync if true, events of same type will be processed asynchronously.
     */
    public void setHandlePolicy(Class<?> eventClass, boolean executeAsync) {
        if (executeAsync) {
            asyncEvents.add(eventClass);
        } else
            asyncEvents.remove(eventClass);
    }

    /**
     * @param listener will be removed (if registered). Only identical (==) values will be removed
     * @param eventType type of the event that the listener is registered to handle.
     */
    public void removeListener(final EventListener<?> listener, final Class<?> eventType) {
        listeners.get(eventType).removeValue(listener, true);
        mainThreadListeners.get(eventType).removeValue(listener, true);
    }

    /**
     * @param eventType all listeners registered to handle this type will be removed.
     */
    public void removeListenersForType(final Class<?> eventType) {
        listeners.remove(eventType);
        mainThreadListeners.remove(eventType);
    }

    /**
     * Removes all registered listeners. Use with care.
     */
    public void clearListeners() {
        listeners.clear();
        mainThreadListeners.clear();
    }

    /** @param event will be posted and invoke all listeners registered to its exact class. Nulls are ignored. */
    public void postEvent(final Object event) {
        if (event == null) {
            return;
        }

        boolean async = asyncEvents.contains(event.getClass());
        if (listeners.containsKey(event.getClass())) {
            if (async) {
                asyncInvokeListeners(event, listeners.get(event.getClass()));
            } else {
                invokeListeners(event, listeners.get(event.getClass()));
            }
        }
        if (mainThreadListeners.containsKey(event.getClass())) {
            Gdx.app.postRunnable(new EventRunnable(event, mainThreadListeners.get(event.getClass()), async));
        }
    }

    /** @param event was just posted.
     * @param listeners will be invoked.
     * @see EventDispatcher#asyncInvokeListeners(Object, SnapshotArray)
     * @see EventDispatcher#setHandlePolicy(Class, boolean) */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Types are always correct.
    protected static void invokeListeners(final Object event, final SnapshotArray<EventListener<?>> listeners) {
        synchronized (listeners) {
            for (final Iterator<EventListener<?>> iterator = listeners.iterator(); iterator.hasNext(); ) {
                final EventListener listener = iterator.next();
                if (!listener.processEvent(event)) {
                    iterator.remove();
                }
            }
        }
    }

    /** @param event was just posted.
     * @param listeners will be invoked.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Types are always correct.
    protected static void asyncInvokeListeners(final Object event, final SnapshotArray<EventListener<?>> listeners) {
        // unsafe array issue: https://github.com/libgdx/libgdx/issues/5076
        Object[] snapshot = listeners.begin();
        for (Object l : snapshot) {
            if (l == null) continue;
            EventListener listener = (EventListener) l;
            if (!listener.processEvent(event)) {
                listeners.removeValue(listener, true);
            }
        }
        listeners.end();
    }

    /** Invokes listeners.
     *
     * @author MJ */
    public static class EventRunnable implements Runnable {
        private final Object event;
        private final SnapshotArray<EventListener<?>> listeners;
        private final boolean async;

        public EventRunnable(final Object event, final SnapshotArray<EventListener<?>> listeners, final boolean async) {
            this.event = event;
            this.listeners = listeners;
            this.async = async;
        }

        @Override
        public void run() {
            if (async) {
                asyncInvokeListeners(event, listeners);
            } else {
                invokeListeners(event, listeners);
            }
        }
    }
}
