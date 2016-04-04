package com.github.czyzby.websocket.data;

/** Contains all official web socket states.
 *
 * @author MJ */
public enum WebSocketState {
    /** 0. Initial state. */
    CONNECTING,

    /** 1. When connected to the server. */
    OPEN,

    /** 2. When about to disconnect. */
    CLOSING,

    /** 3. When disconnected. */
    CLOSED;

    /** @return official ID of the state. */
    public int getId() {
        return ordinal();
    }

    /** @param id official ID of the state.
     * @return state connected with the ID.
     * @throws WebSocketException if invalid ID. */
    public static WebSocketState getById(final int id) {
        if (id < CONNECTING.getId() || id > CLOSED.getId()) {
            throw new WebSocketException("Invalid state ID: " + id);
        }
        return values()[id];
    }
}
