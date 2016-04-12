package com.github.czyzby.websocket;

import com.github.czyzby.websocket.net.ExtendedNet;

/** Abstract base for all tests.
 *
 * @author MJ */
public abstract class AbstractWebSocketTest implements WebSocketTest {
    private WebSocket socket;
    private ResponseListener listener;

    @Override
    public void connect(final String host) {
        // Note: you can also use WebSockets.newSocket() and WebSocket.toWebSocketUrl() methods.
        socket = ExtendedNet.getNet().newWebSocket("localhost", getPort());
        socket.addListener(createListener());
        registerSerializer(socket);
        socket.connect();
    }

    protected abstract int getPort();

    protected WebSocket getSocket() {
        return socket;
    }

    protected abstract void registerSerializer(WebSocket socket);

    protected abstract WebSocketListener createListener();

    @Override
    public void setListener(final ResponseListener listener) {
        this.listener = listener;
    }

    protected ResponseListener getListener() {
        return listener;
    }

    @Override
    public void dispose() {
        WebSockets.closeGracefully(socket);
    }
}
