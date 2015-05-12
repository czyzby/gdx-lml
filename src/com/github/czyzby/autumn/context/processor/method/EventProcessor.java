package com.github.czyzby.autumn.context.processor.method;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.reflect.Method;
import com.github.czyzby.autumn.annotation.method.OnEvent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.method.event.ComponentEventListener;
import com.github.czyzby.autumn.context.processor.method.event.EventRunnable;
import com.github.czyzby.autumn.context.processor.method.event.MethodComponentEventListener;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** OnEvent annotation processor and one of core, default components. Most processors use this one for
 * conditional actions invocations. Do NOT remove from context, unless you want to replace all other default
 * processors.
 *
 * @author MJ */
public class EventProcessor extends ComponentMethodAnnotationProcessor {
	/** Statics for code clarity in event listening methods. */
	public static final boolean REMOVE_AFTER_INVOCATION = true, KEEP_AFTER_INVOCATION = false;

	private final ObjectMap<Class<?>, ObjectSet<ComponentEventListener>> eventListeners = LazyObjectMap
			.newMapOfSets();
	/** Kept in class to avoid creating multiple instances for each event. */
	private final SnapshotArray<ComponentEventListener> listenersToRemove = GdxArrays.newSnapshotArray();

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return OnEvent.class;
	}

	@Override
	public <Type> void processMethod(final ContextContainer context, final ContextComponent component,
			final Method method) {
		final OnEvent eventListenerData = Reflection.getAnnotation(method, OnEvent.class);
		registerListener(new MethodComponentEventListener(eventListenerData, method, component, context));
	}

	/** @param listener will be added to the listeners' collection for the given event type. */
	public void registerListener(final ComponentEventListener listener) {
		eventListeners.get(listener.getEventType()).add(listener);
	}

	/** @param event invokes all listeners listening to its event type. Ignored if null. */
	public <EventType> void postEvent(final EventType event) {
		if (event != null) {
			if (!eventListeners.containsKey(event.getClass())) {
				return;
			}
			for (final ComponentEventListener listener : eventListeners.get(event.getClass())) {
				if (listener.isForcingMainThread()) {
					Gdx.app.postRunnable(new EventRunnable(this, listener, event));
				} else {
					postEventForListener(event, listener);
				}
			}
			removeUnusedListeners();
		}
	}

	/** Allows to manually post an event to a listener we hold a reference to. For extreme cases (and internal
	 * use).
	 *
	 * @param event will be manually posted to the listener.
	 * @param listener will fire the event. If processing action returns true, it will be removed. */
	public <EventType> void postEventForListener(final EventType event, final ComponentEventListener listener) {
		// If true is returned, listener is removed. This allows for custom listeners that get removed under
		// specific conditions.
		if (listener.processEvent(event)) {
			listenersToRemove.add(listener);
		}
	}

	/** Forces removal of listeners that are not supposed to be processing events anymore. */
	public void removeUnusedListeners() {
		while (GdxArrays.sizeOf(listenersToRemove) > 0) {
			final Object[] listeners = listenersToRemove.begin();
			int removedListeners = 0;
			for (final Object listener : listeners) {
				if (listener == null) {
					continue;
				}
				eventListeners.get(((ComponentEventListener) listener).getEventType()).remove(
						(ComponentEventListener) listener);
				removedListeners++;
			}
			listenersToRemove.end();
			if (removedListeners == GdxArrays.sizeOf(listenersToRemove)) {
				// Nothing was added concurrently, safe to clear.
				listenersToRemove.clear();
				// Otherwise, iterating over every listener once again, just to be sure.
			}
		}
	}
}
