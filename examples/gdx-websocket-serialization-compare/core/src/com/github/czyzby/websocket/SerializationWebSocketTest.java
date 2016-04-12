package com.github.czyzby.websocket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.shared.serialization.ClientArrayMessage;
import com.github.czyzby.shared.serialization.ClientListMessage;
import com.github.czyzby.shared.serialization.ClientMessage;
import com.github.czyzby.shared.serialization.Packets;
import com.github.czyzby.shared.serialization.ServerResponse;
import com.github.czyzby.websocket.WebSocketHandler.Handler;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

/** Tests {@link ManualSerializer}.
 *
 * @author MJ */
public class SerializationWebSocketTest extends AbstractWebSocketTest {
    @Override
    protected int getPort() {
        return 8002;
    }

    @Override
    public void send(final String message) {
        sendPacket(new ClientMessage(message));
    }

    @Override
    public void send(final String[] message) {
        sendPacket(new ClientArrayMessage(message));
    }

    @Override
    public void send(final String message, int times) {
        final Array<ClientMessage> messages = GdxArrays.newArray(ClientMessage.class);
        while (times-- > 0) {
            messages.add(new ClientMessage(message));
        }
        sendPacket(new ClientListMessage(messages));
    }

    private void sendPacket(final Object packet) {
        final long start = System.currentTimeMillis();
        final byte[] serialized = getSocket().getSerializer().serialize(packet);
        final long time = System.currentTimeMillis() - start;
        Gdx.app.log("gdx-websocket-serialization", "Serialized packet in " + time + " millies.");
        getSocket().send(serialized);
    }

    @Override
    protected void registerSerializer(final WebSocket socket) {
        final ManualSerializer serializer = new ManualSerializer();
        Packets.register(serializer);
        socket.setSerializer(serializer);
    }

    @Override
    protected WebSocketListener createListener() {
        final WebSocketHandler handler = new WebSocketHandler();
        handler.registerHandler(ServerResponse.class, new Handler<ServerResponse>() {
            @Override
            public boolean handle(final WebSocket webSocket, final ServerResponse packet) {
                getListener().onMessage(packet.getMessage());
                return WebSocketHandler.FULLY_HANDLED;
            }

        });
        return handler;
    }
}
