package com.github.czyzby.websocket;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

/** Tests simple string communication.
 *
 * @author MJ */
public class StringWebSocketTest extends AbstractWebSocketTest {
    @Override
    protected int getPort() {
        return 8000;
    }

    @Override
    public void send(final String message) {
        getSocket().send(message);
    }

    @Override
    public void send(final String[] message) {
        getSocket().send(Strings.join(",", (Object[]) message));
    }

    @Override
    public void send(final String message, int times) {
        final Array<String> list = GdxArrays.newArray(times);
        while (times-- > 0) {
            list.add(message);
        }
        getSocket().send(list.toString());
    }

    @Override
    protected void registerSerializer(final WebSocket socket) {
        // Uses no serialization.
    }

    @Override
    protected WebSocketListener createListener() {
        return new WebSocketAdapter() {
            @Override
            public boolean onMessage(final WebSocket webSocket, final String packet) {
                getListener().onMessage(packet);
                return FULLY_HANDLED;
            }
        };
    }

}
