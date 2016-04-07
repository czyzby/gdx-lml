package com.github.czyzby.websocket;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.websocket.data.WebSocketException;

/** This is the proposed implementation of a {@link WebSocketListener} when using communicating with the server using
 * serialized objects rather than raw strings or bytes. Instead of forcing the user to determine packet type manually
 * (with {@code instanceof} or class comparing), this listener allows to register {@link Handler handlers} to each
 * packet type - when the selected type of packet is received, registered handler will be invoked. This allows to build
 * applications using event-driven approach. Errors during packet handling are delegated to
 * {@link #onError(WebSocket, Throwable)} method rather than rethrown.
 *
 * @author MJ
 * @see #registerHandler(Class, Handler)
 * @see Handler */
public class WebSocketHandler extends AbstractWebSocketListener {
    /** Maps class of expected packets to their handlers. */
    private final ObjectMap<Class<?>, Handler<Object>> handlers = new ObjectMap<Class<?>, Handler<Object>>();
    /** Used as default value when invoking {@link ObjectMap#get(Object, Object)} on {@link #handlers} to prevent
     * NPE. */
    private final Handler<Object> unknown = new Handler<Object>() {
        @Override
        public boolean handle(final WebSocket webSocket, final Object packet) {
            if (failIfNoHandler) {
                onError(webSocket, new WebSocketException("Unknown packet type: " + packet.getClass()));
            }
            return NOT_HANDLED;
        }
    };
    private boolean failIfNoHandler = true;

    /** @param packetClass class of the packet that should be passed to the selected handler.
     * @param handler will be notified when the chosen type of packet is received. Should be prepared to handle the
     *            specific packet class, otherwise {@link ClassCastException} might be thrown. */
    @SuppressWarnings("unchecked")
    public void registerHandler(final Class<?> packetClass, final Handler<?> handler) {
        handlers.put(packetClass, (Handler<Object>) handler);
    }

    /** @param failIfNoHandler if true and a web socket receives packet that has no handler registered to its class, an
     *            exception will passed to {@link #onError(WebSocket, Throwable)} method. Defaults to true. */
    public void setFailIfNoHandler(final boolean failIfNoHandler) {
        this.failIfNoHandler = failIfNoHandler;
    }

    @Override
    protected boolean onMessage(final WebSocket webSocket, final Object packet) throws WebSocketException {
        try {
            return handlers.get(packet.getClass(), unknown).handle(webSocket, packet);
        } catch (final Exception exception) {
            return onError(webSocket,
                    new WebSocketException("Unable to handle the received packet: " + packet, exception));
        }
    }

    /** Common interface for handlers that consume a specific type of packets.
     *
     * @author MJ
     *
     * @param <Packet> type of handled packets.
     * @see EmptyHandler */
    public static interface Handler<Packet> {
        /** Should perform the logic using the received packet.
         *
         * @param webSocket this socket received the packet.
         * @param packet the deserialized packet instance.
         * @return true if message was fully handled and other web socket listeners should not be notified.
         * @see WebSocketListener#FULLY_HANDLED
         * @see WebSocketListener#NOT_HANDLED */
        boolean handle(WebSocket webSocket, Packet packet);
    }

    /** A simple {@link Handler} implementation that does nothing.
     *
     * @author MJ */
    public static class EmptyHandler implements Handler<Object> {
        @Override
        public boolean handle(final WebSocket webSocket, final Object packet) {
            return NOT_HANDLED;
        }
    }
}
