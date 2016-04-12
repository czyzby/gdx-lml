package com.github.czyzby.websocket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.shared.json.JsonClientArrayMessage;
import com.github.czyzby.shared.json.JsonClientListMessage;
import com.github.czyzby.shared.json.JsonClientMessage;
import com.github.czyzby.shared.json.ServerResponse;
import com.github.czyzby.websocket.WebSocketHandler.Handler;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;

/** Tests {@link JsonSerializer}.
 *
 * @author MJ */
public class JsonWebSocketTest extends AbstractWebSocketTest {
    @Override
    protected int getPort() {
        return 8001;
    }

    @Override
    public void send(final String message) {
        final JsonClientMessage packet = new JsonClientMessage();
        packet.message = message;
        sendPacket(packet);
    }

    @Override
    public void send(final String[] message) {
        final JsonClientArrayMessage packet = new JsonClientArrayMessage();
        packet.message = message;
        sendPacket(packet);
    }

    @Override
    public void send(final String message, int times) {
        final Array<JsonClientMessage> messages = GdxArrays.newArray(JsonClientMessage.class);
        while (times-- > 0) {
            final JsonClientMessage packet = new JsonClientMessage();
            packet.message = message;
            messages.add(packet);
        }
        final JsonClientListMessage list = new JsonClientListMessage();
        list.messages = messages;
        sendPacket(list);
    }

    private void sendPacket(final Object packet) {
        final long start = System.currentTimeMillis();
        final byte[] serialized = getSocket().getSerializer().serialize(packet);
        final long time = System.currentTimeMillis() - start;
        Gdx.app.log("JSON", "Serialized packet in " + time + " millies.");
        getSocket().send(serialized);
    }

    @Override
    protected void registerSerializer(final WebSocket socket) {
        // JsonSerializer is already the default object serializer.
    }

    @Override
    protected WebSocketListener createListener() {
        final WebSocketHandler handler = new WebSocketHandler();
        handler.registerHandler(ServerResponse.class, new Handler<ServerResponse>() {
            @Override
            public boolean handle(final WebSocket webSocket, final ServerResponse packet) {
                getListener().onMessage(packet.message);
                return WebSocketHandler.FULLY_HANDLED;
            }

        });
        return handler;
    }

}
