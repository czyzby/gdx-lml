package com.github.czyzby.websocket;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.serialization.SerializationException;

/** Basic {@link WebSocketListener} implementation. Deserializes raw strings and byte arrays received from the server
 * using current serializer assigned to the {@link WebSocket} object. Delegates serialization exceptions to
 * {@link #onError(WebSocket, Throwable)}. Logs errors using LibGDX logging mechanism.
 *
 * @author MJ */
public abstract class AbstractWebSocketListener implements WebSocketListener {
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
        try {
            return onMessage(webSocket, webSocket.getSerializer().deserialize(packet));
        } catch (final WebSocketException exception) {
            return onError(webSocket, exception);
        } catch (final SerializationException exception) {
            return onError(webSocket, exception);
        }
    }

    @Override
    public boolean onMessage(final WebSocket webSocket, final byte[] packet) {
        try {
            return onMessage(webSocket, webSocket.getSerializer().deserialize(packet));
        } catch (final WebSocketException exception) {
            return onError(webSocket, exception);
        } catch (final SerializationException exception) {
            return onError(webSocket, exception);
        }
    }

    /** @param webSocket packet was received on this socket.
     * @param packet deserialized value of the packet.
     * @return true if message was fully handled and other registered listeners should not be notified.
     * @see WebSocketListener#FULLY_HANDLED
     * @see WebSocketListener#NOT_HANDLED
     * @throws WebSocketException if this exception is thrown by this method, it will be delegated to
     *             {@link #onError(WebSocket, Throwable)} rather than rethrown. Basically, this is a utility for
     *             exception handling. */
    protected abstract boolean onMessage(WebSocket webSocket, Object packet) throws WebSocketException;

    @Override
    public boolean onError(final WebSocket webSocket, final Throwable error) {
        Gdx.app.error(webSocket.toString(), "Web socket reported an error.", error);
        return NOT_HANDLED;
    }
}
