package com.github.czyzby.websocket;

import com.github.czyzby.websocket.data.WebSocketCloseCode;

/** Allows to hook up to web socket events.
 *
 * @author MJ
 * @see AbstractWebSocketListener
 * @see WebSocketAdapter
 * @see WebSocketHandler */
public interface WebSocketListener {
    /** Return in listener's methods for code clarity. */
    boolean FULLY_HANDLED = true, NOT_HANDLED = false;

    /** Triggered when the client is connected.
     *
     * @param webSocket affected socket.
     * @return <code>true</code> if event fully handled and other listeners should not be notified.
     * @see #FULLY_HANDLED
     * @see #NOT_HANDLED */
    boolean onOpen(WebSocket webSocket);

    /** Triggered when the client is disconnected.
     *
     * @param webSocket affected socket.
     * @param code code of closing.
     * @param reason optional reason of the closing.
     * @return <code>true</code> if event fully handled and other listeners should not be notified.
     * @see #FULLY_HANDLED
     * @see #NOT_HANDLED */
    boolean onClose(WebSocket webSocket, WebSocketCloseCode code, String reason);

    /** @param webSocket affected socket.
     * @param packet received from the server.
     * @return <code>true</code> if event fully handled and other listeners should not be notified.
     * @see #FULLY_HANDLED
     * @see #NOT_HANDLED */
    boolean onMessage(WebSocket webSocket, String packet);

    /** @param webSocket affected socket.
     * @param packet received from the server.
     * @return <code>true</code> if event fully handled and other listeners should not be notified.
     * @see #FULLY_HANDLED
     * @see #NOT_HANDLED */
    boolean onMessage(WebSocket webSocket, byte[] packet);

    /** @param webSocket affected socket.
     * @param error exception connected with the web socket occurred. Might be null in extreme cases. Note that not all
     *            exceptions are fatal and some are to be expected - this method might be triggered if, for example,
     *            unsupported client method is used or trying to close the client while sending a message, which is
     *            unavoidable from time to time. Unless the client method specifies otherwise, all exceptions are caught
     *            and redirected to this method.
     * @return <code>true</code> if event fully handled and other listeners should not be notified.
     * @see #FULLY_HANDLED
     * @see #NOT_HANDLED */
    boolean onError(WebSocket webSocket, Throwable error);
}
