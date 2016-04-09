package com.github.czyzby.context.event;

/** An example event. See {@link MyEventHandler}
 *
 * @author MJ */
public class MyEvent {
    private final String message;

    public MyEvent(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
