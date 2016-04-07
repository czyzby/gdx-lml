package com.github.czyzby.websocket;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.serialization.Serializer;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;

/** Utilities for web sockets.
 *
 * @author MJ */
public class WebSockets {
    /** ws:// */
    public static final String WEB_SOCKET_ADDRESS_PREFIX = "ws://";
    /** wss:// */
    public static final String SECURE_WEB_SOCKET_ADDRESS_PREFIX = "wss://";

    protected static WebSocketFactory FACTORY;
    /** Assigned as the initial {@link Serializer} to new {@link WebSocket} instances. Handles serializing of objects to
     * strings or byte arrays when using {@link WebSocket#send(Object)} method. By default, serializes objects to JSON
     * format using {@link JsonSerializer}. */
    public static Serializer DEFAULT_SERIALIZER = new JsonSerializer();

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

    /** @param webSocket can be null. Will be closed using default {@link WebSocket#close()} method. Any exception that
     *            occurs during web socket closing will be caught and logged as debug log using current LibGDX
     *            application logging mechanism. (Basically, exception's message will be logged in the console if debug
     *            logs are on.) */
    public static void closeGracefully(final WebSocket webSocket) {
        if (webSocket != null) {
            try {
                webSocket.close();
            } catch (final Exception exception) {
                Gdx.app.debug("WebSocket", exception.getMessage());
            }
        }
    }

    /** Provides web socket instances.
     *
     * @author MJ */
    protected static interface WebSocketFactory {
        /** @param url URL to connect with. Factory can assume that the URL is not null and valid.
         * @return platform-specific {@link WebSocket} instance. */
        WebSocket newWebSocket(String url);
    }
}
