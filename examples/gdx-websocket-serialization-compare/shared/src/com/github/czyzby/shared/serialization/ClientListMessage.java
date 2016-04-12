package com.github.czyzby.shared.serialization;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.websocket.serialization.ArrayProvider;
import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;
import com.github.czyzby.websocket.serialization.impl.Size;

/** Client list message packet using gdx-websocket-serialization.
 *
 * @author MJ */
public class ClientListMessage implements Transferable<ClientListMessage> {
    /** Utility for deserializing. */
    private final static ClientMessage MOCK_UP_PACKET = new ClientMessage(null);
    private final static ArrayProvider<ClientMessage> PROVIDER = new ArrayProvider<ClientMessage>() {
        @Override
        public ClientMessage[] getArray(final int size) {
            return new ClientMessage[size];
        }
    };

    private final Array<ClientMessage> messages;

    public ClientListMessage(final Array<ClientMessage> messages) {
        this.messages = messages;
    }

    @Override
    public void serialize(final Serializer serializer) throws SerializationException {
        // Assuming no nulls and no more than Short#MAX_VALUE elements.
        serializer.serializeTransferableArray(messages.items, 0, messages.size, Size.SHORT);
    }

    @Override
    public ClientListMessage deserialize(final Deserializer deserializer) throws SerializationException {
        return new ClientListMessage(new Array<ClientMessage>(
                deserializer.deserializeTransferableArray(MOCK_UP_PACKET, PROVIDER, Size.SHORT)));
    }

    public Array<ClientMessage> getMessages() {
        return messages;
    }
}
