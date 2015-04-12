package com.github.czyzby.autumn.context.processor.method.message;

import com.github.czyzby.autumn.context.processor.method.MessageProcessor;

/** Utility container for a message action to be posted in the main thread to avoid anonymous classes.
 *
 * @author MJ */
public class MessageRunnable implements Runnable {
	private final MessageProcessor messageProcessor;
	private final ComponentMessageListener listener;

	public MessageRunnable(final MessageProcessor messageProcessor, final ComponentMessageListener listener) {
		this.messageProcessor = messageProcessor;
		this.listener = listener;
	}

	@Override
	public void run() {
		messageProcessor.notifyListener(listener);
		messageProcessor.removeUnusedListeners();
	}
}