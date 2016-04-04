package com.github.czyzby.websocket.data;

/** Thrown when there are problems with a web socket.
 *
 * @author MJ */
public class WebSocketException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_MESSAGE = "Error occured on web socket.";

    public WebSocketException() {
        this(DEFAULT_ERROR_MESSAGE);
    }

    public WebSocketException(final String message) {
        super(message);
    }

    public WebSocketException(final Throwable cause) {
        super(DEFAULT_ERROR_MESSAGE, cause);
    }

    public WebSocketException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
