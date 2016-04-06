package com.github.czyzby.websocket.impl;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.data.WebSocketState;
import com.github.czyzby.websocket.serialization.Serializer;

/** Abstract base for {@link WebSocket} implementations.
 *
 * @author MJ */
public abstract class AbstractWebSocket implements WebSocket {
    private final String url;
    private final Array<WebSocketListener> listeners = new Array<WebSocketListener>(2); // Default 16 is likely too big.
    private Serializer serializer = WebSockets.DEFAULT_SERIALIZER;
    private boolean serializeAsString;
    private boolean sendGracefully;

    public AbstractWebSocket(final String url) {
        this.url = url;
    }

    @Override
    public Serializer getSerializer() {
        return serializer;
    }

    @Override
    public void setSerializer(final Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void setSerializeAsString(final boolean serializeAsString) {
        this.serializeAsString = serializeAsString;
    }

    @Override
    public void setSendGracefully(final boolean sendGracefully) {
        this.sendGracefully = sendGracefully;
    }

    @Override
    public void addListener(final WebSocketListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(final WebSocketListener listener) {
        listeners.removeValue(listener, true);
    }

    /** @return internal collection of all registered listeners. */
    protected Array<WebSocketListener> getListeners() {
        return listeners;
    }

    /** Listeners will be notified about socket opening. */
    protected void postOpenEvent() {
        for (final WebSocketListener listener : listeners) {
            if (listener.onOpen(this)) {
                break;
            }
        }
    }

    /** Listeners will be notified about socket closing.
     *
     * @param closeCode closing code.
     * @param reason optional closing reason. */
    protected void postCloseEvent(final WebSocketCloseCode closeCode, final String reason) {
        for (final WebSocketListener listener : listeners) {
            if (listener.onClose(this, closeCode, reason)) {
                break;
            }
        }
    }

    /** Listeners will be notified about socket error.
     *
     * @param error thrown during by the socket or socket related actions. */
    protected void postErrorEvent(final Throwable error) {
        for (final WebSocketListener listener : listeners) {
            if (listener.onError(this, error)) {
                break;
            }
        }
    }

    /** Listeners will be notified about received message.
     *
     * @param packet sent by the server. */
    protected void postMessageEvent(final byte[] packet) {
        for (final WebSocketListener listener : listeners) {
            if (listener.onMessage(this, packet)) {
                break;
            }
        }
    }

    /** Listeners will be notified about received message.
     *
     * @param packet sent by the server. */
    protected void postMessageEvent(final String packet) {
        for (final WebSocketListener listener : listeners) {
            if (listener.onMessage(this, packet)) {
                break;
            }
        }
    }

    @Override
    public boolean isSecure() {
        final String secureWebSocketPrefix = "wss";
        return getUrl().substring(0, secureWebSocketPrefix.length()).equalsIgnoreCase(secureWebSocketPrefix);
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void sendKeepAlivePacket() throws WebSocketException {
        send("");
    }

    @Override
    public void send(final Object packet) throws WebSocketException {
        try {
            if (packet != null) {
                if (serializeAsString) {
                    send(serializer.serializeAsString(packet));
                } else {
                    send(serializer.serialize(packet));
                }
            }
        } catch (final WebSocketException exception) {
            onSendingException(exception);
        } catch (final Exception exception) {
            onSendingException(exception);
        }
    }

    @Override
    public boolean isOpen() {
        return getState() == WebSocketState.OPEN;
    }

    @Override
    public boolean isClosed() {
        return getState() == WebSocketState.CLOSED;
    }

    @Override
    public boolean isClosing() {
        return getState() == WebSocketState.CLOSING;
    }

    @Override
    public boolean isConnecting() {
        return getState() == WebSocketState.CONNECTING;
    }

    /** @param exception will be rethrown as {@link WebSocketException} or notify listeners if sending is graceful. */
    protected void onSendingException(final Exception exception) {
        if (sendGracefully) {
            postErrorEvent(exception);
        } else {
            throw new WebSocketException("Unable to send packet.", exception);
        }
    }

    /** @param exception will be rethrown or notify listeners if sending is graceful. */
    protected void onSendingException(final WebSocketException exception) {
        if (sendGracefully) {
            postErrorEvent(exception);
        } else {
            throw exception;
        }
    }

    @Override
    public void send(final byte[] packet) throws WebSocketException {
        try {
            if (packet != null) {
                sendBinary(packet);
            }
        } catch (final WebSocketException exception) {
            onSendingException(exception);
        } catch (final Exception exception) {
            onSendingException(exception);
        }
    }

    @Override
    public void send(final String packet) throws WebSocketException {
        try {
            if (packet != null) {
                sendString(packet);
            }
        } catch (final WebSocketException exception) {
            onSendingException(exception);
        } catch (final Exception exception) {
            onSendingException(exception);
        }
    }

    /** @param packet should be sent to the server. Is never null.
     * @throws Exception thrown during sending. */
    protected abstract void sendBinary(byte[] packet) throws Exception;

    /** @param packet should be sent to the server. Is never null.
     * @throws Exception thrown during sending. */
    protected abstract void sendString(String packet) throws Exception;

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public void close() throws WebSocketException {
        close(WebSocketCloseCode.NORMAL, null);
    }

    @Override
    public void close(final String reason) throws WebSocketException {
        close(WebSocketCloseCode.NORMAL, reason);
    }

    @Override
    public void close(final WebSocketCloseCode code) throws WebSocketException {
        close(code, null);
    }
}
