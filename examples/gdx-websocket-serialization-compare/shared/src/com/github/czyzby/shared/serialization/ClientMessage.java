package com.github.czyzby.shared.serialization;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;
import com.github.czyzby.websocket.serialization.impl.Size;

/** Client message packet using gdx-websocket-serialization.
 *
 * @author MJ */
public class ClientMessage implements Transferable<ClientMessage> {
    private final String message;

    public ClientMessage(final String message) {
        this.message = message;
    }

    @Override
    public void serialize(final Serializer serializer) throws SerializationException {
        serializer.serializeString(message, Size.SHORT); // Assuming String is no longer than Short#MAX_VALUE.
    }

    @Override
    public ClientMessage deserialize(final Deserializer deserializer) throws SerializationException {
        return new ClientMessage(deserializer.deserializeString(Size.SHORT));
    }

    public String getMessage() {
        return message;
    }
}
