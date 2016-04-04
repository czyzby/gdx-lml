package com.github.czyzby.websocket;

import com.github.czyzby.websocket.data.WebSocketCloseCode;

/** Empty implementation of {@link WebSocketListener}.
 *
 * @author MJ */
public class WebSocketAdapter implements WebSocketListener {
    @Override
    public boolean onOpen(final WebSocket webSocket) {
        return NOT_HANDLED;
    }

    @Override
    public boolean onClose(final WebSocket webSocket, final WebSocketCloseCode code, final String reason) {
        return NOT_HANDLED;
    }

    @Override
    public boolean onMessage(final WebSocket webSocket, final String packet) {
        return NOT_HANDLED;
    }

    @Override
    public boolean onMessage(final WebSocket webSocket, final byte[] packet) {
        return NOT_HANDLED;
    }

    @Override
    public boolean onError(final WebSocket webSocket, final Throwable error) {
        return NOT_HANDLED;
    }
}
