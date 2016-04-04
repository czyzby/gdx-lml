package com.github.czyzby.websocket;

import com.github.czyzby.websocket.WebSockets.WebSocketFactory;
import com.github.czyzby.websocket.impl.GwtWebSocket;

/** Allows to initiate GWT web sockets module.
 *
 * @author MJ */
public class GwtWebSockets {
    private GwtWebSockets() {
    }

    /** Initiates {@link WebSocketFactory}. */
    public static void initiate() {
        WebSockets.FACTORY = new GwtWebSocketFactory();
    }

    /** Provides {@link GwtWebSocket} instances.
     *
     * @author MJ */
    protected static class GwtWebSocketFactory implements WebSocketFactory {
        @Override
        public WebSocket newWebSocket(final String url) {
            return new GwtWebSocket(url);
        }
    }
}
