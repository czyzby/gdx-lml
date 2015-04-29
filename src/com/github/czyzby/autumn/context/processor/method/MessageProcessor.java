package com.github.czyzby.autumn.context.processor.method;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.SnapshotArray;
import com.github.czyzby.autumn.annotation.method.OnMessage;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.method.message.ComponentMessageListener;
import com.github.czyzby.autumn.context.processor.method.message.MessageRunnable;
import com.github.czyzby.autumn.context.processor.method.message.MethodComponentMessageListener;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedMethod;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;

/** OnMessage annotation processor and one of core, default components. Some processors use this one for
 * specific actions invocations. Do NOT remove from context, unless you want to replace all other default
 * processors.
 *
 * @author MJ */
public class MessageProcessor extends ComponentMethodAnnotationProcessor {
	/** Statics for code clarity in message listening methods. */
	public static final boolean REMOVE_AFTER_INVOCATION = true, KEEP_AFTER_INVOCATION = false;

	private final ObjectMap<String, ObjectSet<ComponentMessageListener>> messageListeners = LazyObjectMap
			.newMapOfSets();
	/** Kept in class to avoid creating multiple instances for each message. */
	private final SnapshotArray<ComponentMessageListener> listenersToRemove = GdxArrays.newSnapshotArray();

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return OnMessage.class;
	}

	@Override
	public <Type> void processMethod(final ContextContainer context, final ContextComponent component,
			final ReflectedMethod method) {
		final OnMessage eventListenerData = method.getAnnotation(OnMessage.class);
		registerListener(new MethodComponentMessageListener(eventListenerData, method, component, context));
	}

	/** @param listener will be added to the listeners' collection for the given message content. */
	public void registerListener(final ComponentMessageListener listener) {
		messageListeners.get(listener.getMessageContent()).add(listener);
	}

	/** @param message invokes all listeners listening to its content. Ignored if null. */
	public void postMessage(final String message) {
		if (message != null) {
			if (!messageListeners.containsKey(message)) {
				return;
			}
			for (final ComponentMessageListener listener : messageListeners.get(message)) {
				if (listener.isForcingMainThread()) {
					Gdx.app.postRunnable(new MessageRunnable(this, listener));
				} else {
					notifyListener(listener);
				}
			}
			removeUnusedListeners();
		}
	}

	/** Allows to manually notify the listener we hold a reference to. For extreme cases (and internal use).
	 *
	 * @param listener will act as if it received a message. If processing action returns true, it will be
	 *            removed. */
	public <EventType> void notifyListener(final ComponentMessageListener listener) {
		// If true is returned, listener is removed. This allows for custom listeners that get removed under
		// specific conditions.
		if (listener.processMessage()) {
			listenersToRemove.add(listener);
		}
	}

	/** Forces removal of listeners that are not supposed to be processing messages anymore. */
	public void removeUnusedListeners() {
		while (GdxArrays.sizeOf(listenersToRemove) > 0) {
			final Object[] listeners = listenersToRemove.begin();
			int removedListeners = 0;
			for (final Object listener : listeners) {
				if (listener == null) {
					continue;
				}
				messageListeners.get(((ComponentMessageListener) listener).getMessageContent()).remove(
						(ComponentMessageListener) listener);
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
