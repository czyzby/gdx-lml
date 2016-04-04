package com.github.czyzby.websocket.impl;

import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.data.WebSocketState;

/** Default web socket implementation for GWT applications.
 *
 * @author MJ */
public class GwtWebSocket extends AbstractWebSocket {
    private final WebSocket webSocket = new WebSocket(this);

    public GwtWebSocket(final String url) {
        super(url);
    }

    @Override
    public void connect() throws WebSocketException {
        if (webSocket.isOpen() || webSocket.isConnecting()) {
            close(WebSocketCloseCode.AWAY);
        }
        try {
            webSocket.open(super.getUrl());
        } catch (final Throwable exception) {
            throw new WebSocketException("Unable to open the web socket.", exception);
        }
    }

    @Override
    public WebSocketState getState() {
        return webSocket.getState();
    }

    @Override
    public void close(final WebSocketCloseCode code, final String reason) throws WebSocketException {
        try {
            webSocket.close(code, reason);
        } catch (final Throwable exception) {
            throw new WebSocketException("Unable to close the web socket.", exception);
        }
    }

    @Override
    protected void sendBinary(final byte[] packet) throws Exception {
        webSocket.send(packet);
    }

    @Override
    protected void sendString(final String packet) throws Exception {
        webSocket.send(packet);
    }

    @Override
    public boolean isSecure() {
        return webSocket.isSecure();
    }

    @Override
    public boolean isSupported() {
        return WebSocket.areWebSocketsSupported();
    }

    @Override
    public String getUrl() {
        final String url = webSocket.getUrl();
        return url == null ? super.getUrl() : url;
    }

    @Override
    public boolean isClosed() {
        return webSocket.isClosed();
    }

    @Override
    public boolean isClosing() {
        return webSocket.isClosing();
    }

    @Override
    public boolean isConnecting() {
        return webSocket.isConnecting();
    }

    @Override
    public boolean isOpen() {
        return webSocket.isOpen();
    }
}
