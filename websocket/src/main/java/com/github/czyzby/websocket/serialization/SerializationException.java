package com.github.czyzby.websocket.serialization;

/** Thrown when unable to serialize or deserialize the packet.
 *
 * @author MJ */
public class SerializationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_MESSAGE = "Unable to (de)serialize.";

    public SerializationException() {
        super(DEFAULT_MESSAGE);
    }

    public SerializationException(final String message) {
        super(message);
    }

    public SerializationException(final Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public SerializationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
