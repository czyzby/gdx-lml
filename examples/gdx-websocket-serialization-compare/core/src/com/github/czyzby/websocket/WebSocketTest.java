package com.github.czyzby.websocket;

import com.badlogic.gdx.utils.Disposable;

/** Common base for websocket tests.
 *
 * @author MJ */
public interface WebSocketTest extends Disposable {
    /** @param host server host name. */
    void connect(String host);

    /** @param message will be wrapped with a packet object (in necessary) and sent to the server. */
    void send(String message);

    /** @param message will be wrapped with a packet object (in necessary) and sent to the server. */
    void send(String[] message);

    /** @param message should be wrapped with a packet object and a list.
     * @param times amount of elements in the list. */
    void send(String message, int times);

    /** @param listener utility interface. Allows to listen to server responses. */
    void setListener(ResponseListener listener);

    /** Allows to listen to server responses.
     *
     * @author MJ */
    public static interface ResponseListener {
        /** @param message received from the server */
        void onMessage(String message);
    }
}
