package com.github.czyzby.autumn.context.processor.method.event;

import com.github.czyzby.autumn.context.processor.method.EventProcessor;

/** Utility container for an event action to be posted in the main thread to avoid anonymous classes.
 *
 * @author MJ */
public class EventRunnable implements Runnable {
	private final EventProcessor eventProcessor;
	private final ComponentEventListener listener;
	private final Object event;

	public EventRunnable(final EventProcessor eventProcessor, final ComponentEventListener listener,
			final Object event) {
		this.eventProcessor = eventProcessor;
		this.listener = listener;
		this.event = event;
	}

	@Override
	public void run() {
		eventProcessor.postEventForListener(event, listener);
		eventProcessor.removeUnusedListeners();
	}
}