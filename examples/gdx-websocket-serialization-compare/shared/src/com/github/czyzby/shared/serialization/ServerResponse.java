package com.github.czyzby.shared.serialization;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

/** Server response packet using gdx-websocket-serialization.
 *
 * @author MJ */
public class ServerResponse implements Transferable<ServerResponse> {
    private final String message;

    public ServerResponse(final String message) {
        this.message = message;
    }

    @Override
    public void serialize(final Serializer serializer) throws SerializationException {
        serializer.serializeString(message);
    }

    @Override
    public ServerResponse deserialize(final Deserializer deserializer) throws SerializationException {
        return new ServerResponse(deserializer.deserializeString());
    }

    public String getMessage() {
        return message;
    }
}
