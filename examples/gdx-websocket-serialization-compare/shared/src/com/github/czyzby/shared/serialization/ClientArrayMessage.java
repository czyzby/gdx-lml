package com.github.czyzby.shared.serialization;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;
import com.github.czyzby.websocket.serialization.impl.Size;

/** Client message packet using gdx-websocket-serialization.
 *
 * @author MJ */
public class ClientArrayMessage implements Transferable<ClientArrayMessage> {
    private final String[] message;

    public ClientArrayMessage(final String[] message) {
        this.message = message;
    }

    @Override
    public void serialize(final Serializer serializer) throws SerializationException {
        // Serializing array size as short. Assuming String is no longer than Short#MAX_VALUE.
        serializer.serializeStringArray(message, Size.SHORT, Size.SHORT);
    }

    @Override
    public ClientArrayMessage deserialize(final Deserializer deserializer) throws SerializationException {
        return new ClientArrayMessage(deserializer.deserializeStringArray(Size.SHORT, Size.SHORT));
    }

    public String[] getMessage() {
        return message;
    }
}
