package com.github.czyzby.websocket.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSockets;

/** Extends {@link Net} API with web socket related operations. Delegates other method calls to wrapped {@link Net}
 * instance.
 *
 * @author MJ
 * @see #newWebSocket(String, int)
 * @see #newWebSocket(String, int, String)
 * @see #newSecureWebSocket(String, int)
 * @see #newSecureWebSocket(String, int, String) */
public class ExtendedNet implements Net {
    private final Net net;

    public ExtendedNet(final Net net) {
        this.net = net;
    }

    /** Wraps current application's {@link Net} instance, extending its functionalities. {@link ExtendedNet} will be
     * available through {@link Gdx#net}.
     *
     * @return a new {@link ExtendedNet} instance. */
    public static ExtendedNet wrapNet() {
        final ExtendedNet extendedNet = new ExtendedNet(Gdx.net);
        Gdx.net = extendedNet;
        return extendedNet;
    }

    /** @return global instance of the {@link ExtendedNet}. If {@link Gdx#net} is not wrapped yet, this method will
     *         ensure that this field points to a {@link ExtendedNet} instance. */
    public static ExtendedNet getNet() {
        if (!(Gdx.net instanceof ExtendedNet)) {
            return wrapNet();
        }
        return (ExtendedNet) Gdx.net;
    }

    @Override
    public void sendHttpRequest(final HttpRequest httpRequest, final HttpResponseListener httpResponseListener) {
        net.sendHttpRequest(httpRequest, httpResponseListener);
    }

    @Override
    public void cancelHttpRequest(final HttpRequest httpRequest) {
        net.cancelHttpRequest(httpRequest);
    }

    @Override
    public ServerSocket newServerSocket(final Protocol protocol, final String hostname, final int port,
            final ServerSocketHints hints) {
        return net.newServerSocket(protocol, hostname, port, hints);
    }

    @Override
    public ServerSocket newServerSocket(final Protocol protocol, final int port, final ServerSocketHints hints) {
        return net.newServerSocket(protocol, port, hints);
    }

    @Override
    public Socket newClientSocket(final Protocol protocol, final String host, final int port, final SocketHints hints) {
        return net.newClientSocket(protocol, host, port, hints);
    }

    @Override
    public boolean openURI(final String URI) {
        return net.openURI(URI);
    }

    /** @param host server address.
     * @param port server port.
     * @return a new {@link WebSocket} instance, which allows to connect with the server. */
    public WebSocket newWebSocket(final String host, final int port) {
        return WebSockets.newSocket(WebSockets.toWebSocketUrl(host, port));
    }

    /** @param host server address.
     * @param port server port.
     * @param content path.
     * @return a new {@link WebSocket} instance, which allows to connect with the server. */
    public WebSocket newWebSocket(final String host, final int port, final String content) {
        return WebSockets.newSocket(WebSockets.toWebSocketUrl(host, port, content));
    }

    /** @param host server address.
     * @param port server port.
     * @return a new {@link WebSocket} instance, which allows to securely connect with the server. */
    public WebSocket newSecureWebSocket(final String host, final int port) {
        return WebSockets.newSocket(WebSockets.toSecureWebSocketUrl(host, port));
    }

    /** @param host server address.
     * @param port server port.
     * @param content path.
     * @return a new {@link WebSocket} instance, which allows to securely connect with the server. */
    public WebSocket newSecureWebSocket(final String host, final int port, final String content) {
        return WebSockets.newSocket(WebSockets.toSecureWebSocketUrl(host, port, content));
    }
}
