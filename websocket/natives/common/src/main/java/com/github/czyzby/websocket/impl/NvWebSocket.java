package com.github.czyzby.websocket.impl;

import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.data.WebSocketState;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

/** Default web socket implementation for desktop and mobile platforms.
 *
 * @author MJ */
public class NvWebSocket extends AbstractWebSocket {
    private final WebSocketFactory webSocketFactory = new WebSocketFactory();
    private WebSocket webSocket;

    public NvWebSocket(final String url) {
        super(url);
    }

    @Override
    public void connect() throws WebSocketException {
        try {
            dispose();
            final WebSocket currentWebSocket = webSocket = webSocketFactory.createSocket(getUrl());
            currentWebSocket.addListener(new NvWebSocketListener(this));
            currentWebSocket.connect();
        } catch (final Throwable exception) {
            throw new WebSocketException("Unable to connect.", exception);
        }
    }

    /** Removes current web socket instance. */
    protected void dispose() {
        final WebSocket currentWebSocket = webSocket;
        if (currentWebSocket != null && currentWebSocket.isOpen()) {
            try {
                currentWebSocket.disconnect(WebSocketCloseCode.AWAY.getCode());
            } catch (final Exception exception) {
                postErrorEvent(exception);
            }
        }
    }

    @Override
    public WebSocketState getState() {
        final WebSocket currentWebSocket = webSocket;
        return currentWebSocket == null ? WebSocketState.CLOSED : convertState(currentWebSocket.getState());
    }

    private static WebSocketState convertState(final com.neovisionaries.ws.client.WebSocketState state) {
        // Somewhat faster than a switch.
        if (state == com.neovisionaries.ws.client.WebSocketState.CREATED) {
            // Not even connecting yet.
            return WebSocketState.CLOSED;
        } else if (state == com.neovisionaries.ws.client.WebSocketState.CONNECTING) {
            return WebSocketState.CONNECTING;
        } else if (state == com.neovisionaries.ws.client.WebSocketState.OPEN) {
            return WebSocketState.OPEN;
        } else if (state == com.neovisionaries.ws.client.WebSocketState.CLOSING) {
            return WebSocketState.CLOSING;
        } else if (state == com.neovisionaries.ws.client.WebSocketState.CLOSED) {
            return WebSocketState.CLOSED;
        }
        return WebSocketState.CLOSED;
    }

    @Override
    public boolean isSecure() {
        final WebSocket currentWebSocket = webSocket;
        return currentWebSocket != null && "wss".equalsIgnoreCase(currentWebSocket.getURI().getScheme());
    }

    @Override
    public boolean isOpen() {
        final WebSocket currentWebSocket = webSocket;
        return currentWebSocket != null && currentWebSocket.isOpen();
    }

    @Override
    public void close(final WebSocketCloseCode code, final String reason) throws WebSocketException {
        final WebSocket currentWebSocket = webSocket;
        if (currentWebSocket != null) {
            try {
                currentWebSocket.disconnect(code.getCode(), reason);
            } catch (final Throwable exception) {
                throw new WebSocketException("Unable to close the web socket.", exception);
            }
        }
    }

    @Override
    protected void sendBinary(final byte[] packet) throws Exception {
        webSocket.sendBinary(packet);
    }

    @Override
    protected void sendString(final String packet) throws Exception {
        webSocket.sendText(packet);
    }
}
