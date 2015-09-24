package com.github.czyzby.autumn.context.processor.method.message;

/** Interface for all component messages listeners managed by MessageProcessors.
 *
 * @author MJ */
public interface ComponentMessageListener {
    /** Statics for code clarity in {@link #processMessage()} method. */
    boolean REMOVE_AFTER_INVOCATION = true, KEEP_AFTER_INVOCATION = false;

    /** @return content of the message that the listener is listening for. */
    String getMessageContent();

    /** Invoked when the listened message occurs.
     *
     * @return if true, listener will be removed after the invocation. */
    boolean processMessage();

    /** @return if true, event will be executed on the application's main thread. */
    boolean isForcingMainThread();
}
