package com.github.czyzby;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.czyzby.shared.MyJsonMessage;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.net.ExtendedNet;

public class Core extends ApplicationAdapter {
    private String message = "Connecting...";
    private SpriteBatch batch;
    private BitmapFont font;
    private WebSocket socket;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        font = new BitmapFont();
        // Note: you can also use WebSockets.newSocket() and WebSocket.toWebSocketUrl() methods.
        socket = ExtendedNet.getNet().newWebSocket("localhost", 8000);
        // By default, JsonSerializer will be used to send objects. If you want to change it, use setSerializer method.
        // For example, uncomment the following code to use BASE64 encoding additionally to JSON serialization:
        // socket.setSerializer(new Base64Serializer(new JsonSerializer()));
        socket.addListener(getListener());
        socket.connect();
    }

    private WebSocketListener getListener() {
        return new AbstractWebSocketListener() {
            @Override
            public boolean onOpen(final WebSocket webSocket) {
                message = "Connected!";
                final MyJsonMessage myMessage = new MyJsonMessage();
                myMessage.text = "Hello server!";
                webSocket.send(myMessage);
                return FULLY_HANDLED;
            }

            @Override
            public boolean onClose(final WebSocket webSocket, final WebSocketCloseCode code, final String reason) {
                message = "Disconnected!";
                return FULLY_HANDLED;
            }

            @Override
            protected boolean onMessage(final WebSocket webSocket, final Object packet) {
                if (packet instanceof MyJsonMessage) {
                    final MyJsonMessage jsonMessage = (MyJsonMessage) packet;
                    message = jsonMessage.text + jsonMessage.id + "!";
                }
                return FULLY_HANDLED;
            }
        };
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font.draw(batch, message, 10f, Gdx.graphics.getHeight() / 2f);
        batch.end();
    }

    @Override
    public void dispose() {
        socket.close();
        batch.dispose();
        font.dispose();
    }
}
