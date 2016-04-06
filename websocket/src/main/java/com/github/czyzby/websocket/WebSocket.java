package com.github.czyzby.websocket;

import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.data.WebSocketState;
import com.github.czyzby.websocket.serialization.Serializer;

/** Common interface for all web socket implementations.
 *
 * @author MJ */
public interface WebSocket {
    /** Will try to connect with the given URL. Note that
     *
     * @throws WebSocketException if unable to connect. */
    void connect() throws WebSocketException;

    /** @return current web socket state. Should never return null - if web socket was not opened yet, should return
     *         CLOSED state. */
    WebSocketState getState();

    /** @return true if client is currently connected with the server. */
    boolean isOpen();

    /** @return true if web socket is in connecting state. */
    boolean isConnecting();

    /** @return true if web socket is in closing state. */
    boolean isClosing();

    /** @return true if web socket is in closed state. Might return true if the web socket was not opened yet. */
    boolean isClosed();

    /** @return true if client's connection is secure. */
    boolean isSecure();

    /** @return URL used by current web socket. Can be null if not properly connected or web socket is corrupted. */
    String getUrl();

    /** @param listener will be notified of web socket events. Listeners are notified in the order that they were
     *            added. */
    void addListener(WebSocketListener listener);

    /** @param listener will no longer be notified of web socket events. */
    void removeListener(WebSocketListener listener);

    /** Sends an empty packet to the server.
     *
     * @throws WebSocketException if unable to send the packet. */
    void sendKeepAlivePacket() throws WebSocketException;

    /** @param serializer will be used to serialize passed packets. */
    void setSerializer(Serializer serializer);

    /** @return serializer used to serialize passed packets. */
    Serializer getSerializer();

    /** @param asString if true, packets will be serialized to strings instead of bytes. Defaults to false.
     * @see #send(Object) */
    void setSerializeAsString(boolean asString);

    /** @param sendGracefully if true, exceptions thrown during packet sending will not be thrown immediately - instead,
     *            they will be passed to {@link WebSocketListener#onError(WebSocket, Throwable)} to all registered
     *            listeners. Defaults to false. */
    void setSendGracefully(boolean sendGracefully);

    /** @param packet will be serialized and sent to the server if the client is connected. Nulls are ignored. Request
     *            is ignored if client is not connected. Fails if no serializer is set.
     * @throws WebSocketException if unable to send the packet due to an exception. Not thrown if the client is not
     *             connected.
     * @see #setSerializer(Serializer)
     * @see #setSerializeAsString(boolean) */
    void send(Object packet) throws WebSocketException;

    /** @param packet will be sent as-is to the server. Nulls are ignored. Request is ignored if client is not
     *            connected.
     * @throws WebSocketException if unable to send the packet due to an exception. Not thrown if the client is not
     *             connected. */
    void send(String packet) throws WebSocketException;

    /** @param packet will be sent as binary data to the server. Nulls are ignored. Might not be supported by every
     *            platform - older browsers in particular. Request is ignored if client is not connected.
     * @throws WebSocketException if unable to send the packet due to an exception. Not thrown if the client is not
     *             connected. */
    void send(byte[] packet) throws WebSocketException;

    /** @return true if web sockets are supported on this platform. */
    boolean isSupported();

    /** Closes the connection. Uses normal close code and no reason.
     *
     * @throws WebSocketException if unable to close the connection. */
    void close() throws WebSocketException;

    /** Closes the connection. Uses normal close code.
     *
     * @param reason closing reason. Optional.
     * @throws WebSocketException if unable to close the connection. */
    void close(String reason) throws WebSocketException;

    /** Closes the connection. Uses no disconnection reason.
     *
     * @param code connection close code. Cannot be null.
     * @throws WebSocketException if unable to close the connection. */
    void close(WebSocketCloseCode code) throws WebSocketException;

    /** Closes the connection.
     *
     * @param code connection close code. Cannot be null.
     * @param reason closing reason. Optional.
     * @throws WebSocketException if unable to close the connection. */
    void close(WebSocketCloseCode code, String reason) throws WebSocketException;
}
