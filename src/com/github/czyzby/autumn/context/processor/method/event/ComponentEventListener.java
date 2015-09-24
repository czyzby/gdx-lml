package com.github.czyzby.autumn.context.processor.method.event;

/** Interface for all component event listeners managed by EventProcessors.
 *
 * @author MJ */
public interface ComponentEventListener {
    /** Statics for code clarity in {@link #processEvent(Object)} method. */
    boolean REMOVE_AFTER_INVOCATION = true, KEEP_AFTER_INVOCATION = false;

    /** @return type of events that the listener listens to. */
    Class<?> getEventType();

    /** @param event an event that just occurred. Safe to cast to processed event class type.
     * @return if true, listener will be removed after the invocation. */
    boolean processEvent(Object event);

    /** @return if true, event will be executed on the application's main thread. */
    boolean isForcingMainThread();
}
