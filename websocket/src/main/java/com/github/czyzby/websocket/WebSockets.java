package com.github.czyzby.websocket;

import com.github.czyzby.websocket.data.WebSocketException;

/** Utilities for web sockets.
 *
 * @author MJ */
public class WebSockets {
    /** ws:// */
    public static final String WEB_SOCKET_ADDRESS_PREFIX = "ws://";
    /** wss:// */
    public static final String SECURE_WEB_SOCKET_ADDRESS_PREFIX = "wss://";

    protected static WebSocketFactory FACTORY;

    private WebSockets() {
    }

    /** @param url a valid URL.
     * @return {@link WebSocket} instance, allowing to connect with the passed URL.
     * @see #toWebSocketUrl(String, int) */
    public static WebSocket newSocket(final String url) {
        if (FACTORY == null) {
            throw new WebSocketException("Web sockets are not initiated.");
        }
        return FACTORY.newWebSocket(url);
    }

    /** @param host IP or domain name of the server.
     * @param port port of the application. Will be validated.
     * @return web socket URL.
     * @throws WebSocketException if port is invalid. */
    public static String toWebSocketUrl(final String host, final int port) {
        return toWebSocketUrl(host, port, null);
    }

    /** @param host IP or domain name of the server.
     * @param port port of the application. Will be validated.
     * @param contentPath path at which the connection is open. Optional.
     * @return web socket URL.
     * @throws WebSocketException if port is invalid. */
    public static String toWebSocketUrl(final String host, final int port, final String contentPath) {
        return toUrl(WEB_SOCKET_ADDRESS_PREFIX, host, port, contentPath);
    }

    /** @param host IP or domain name of the server.
     * @param port port of the application. Will be validated.
     * @return secure web socket URL.
     * @throws WebSocketException if port is invalid. */
    public static String toSecureWebSocketUrl(final String host, final int port) {
        return toSecureWebSocketUrl(host, port, null);
    }

    /** @param host IP or domain name of the server.
     * @param port port of the application. Will be validated.
     * @param contentPath path at which the connection is open. Optional.
     * @return secure web socket URL.
     * @throws WebSocketException if port is invalid. */
    public static String toSecureWebSocketUrl(final String host, final int port, final String contentPath) {
        return toUrl(SECURE_WEB_SOCKET_ADDRESS_PREFIX, host, port, contentPath);
    }

    private static String toUrl(final String prefix, final String host, final int port, final String contentPath) {
        if (isPortValid(port)) {
            return prefix + host + ":" + port + "/" + (contentPath == null ? "" : contentPath);
        }
        throw new WebSocketException("Invalid port: " + port);
    }

    /** @param port will be validated.
     * @return true if the port is valid. */
    public static boolean isPortValid(final int port) {
        return port > 0 && port <= 65535;
    }

    /** Provides web socket instances.
     *
     * @author MJ */
    protected static interface WebSocketFactory {
        WebSocket newWebSocket(String url);
    }
}
