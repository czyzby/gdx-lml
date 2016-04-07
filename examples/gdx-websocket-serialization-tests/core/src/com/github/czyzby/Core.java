package com.github.czyzby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.AbstractApplicationListener;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.LmlView;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.vis.util.VisLml;
import com.github.czyzby.shared.MyPackets;
import com.github.czyzby.shared.Ping;
import com.github.czyzby.shared.Pong;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketHandler.Handler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;

// LmlView and ActionContainer are interfaces used to for UI library: gdx-lml-vis.
// AbstractApplicationListener is an alternative to ApplicationAdapter from gdx-kiwi.
public class Core extends AbstractApplicationListener implements LmlView, ActionContainer {
    /** Preferred window size. Virtual size of viewport. */
    public static final int WIDTH = 480, HEIGHT = 340;

    private Stage stage;
    private WebSocket socket;

    // Injected by gdx-lml-vis parser during processing of core.lml:
    @LmlActor("status") Label statusLabel;
    @LmlActor("input") VisTextField inputField;

    @Override
    public void create() {
        // Note: you can also use WebSockets.newSocket() and WebSocket.toWebSocketUrl() methods.
        socket = ExtendedNet.getNet().newWebSocket("localhost", 8000);
        socket.addListener(getListener());
        // Creating a new ManualSerializer - this replaces the default JsonSerializer and allows to use the
        // serialization mechanism from gdx-websocket-serialization library.
        final ManualSerializer serializer = new ManualSerializer();
        socket.setSerializer(serializer);
        // Registering all expected packets:
        MyPackets.register(serializer);

        // Creating UI with gdx-lml-vis:
        stage = new Stage(new FitViewport(WIDTH, HEIGHT));
        Gdx.input.setInputProcessor(stage);
        VisUI.load();
        VisLml.parser().build().createView(this, Gdx.files.internal("core.lml"));
        // Smooth screen fading in:
        stage.addAction(Actions.sequence(Actions.alpha(0f), Actions.alpha(1f, 1f)));

        // Connecting with the server.
        socket.connect();
    }

    private WebSocketListener getListener() {
        // WebSocketHandler is an implementation of WebSocketListener that uses the current Serializer (ManualSerializer
        // in this case) to create objects from received raw data. Instead of forcing you to work with Object and do
        // manual casting, this listener allows to register handlers for each expected packet class.
        final WebSocketHandler handler = new WebSocketHandler();
        // Registering Ping handler:
        handler.registerHandler(Ping.class, new Handler<Ping>() {
            @Override
            public boolean handle(final WebSocket webSocket, final Ping packet) {
                statusLabel.setText("Received PING: " + packet.getValue() + "!");
                return true;
            }
        });
        // Registering Pong handler:
        handler.registerHandler(Pong.class, new Handler<Pong>() {
            @Override
            public boolean handle(final WebSocket webSocket, final Pong packet) {
                statusLabel.setText("Received PONG: " + packet.getValue() + "!");
                return true;
            }
        });
        // Side note: this would be a LOT cleaner with Java 8 lambdas (or using another JVM language, like Kotlin).
        return handler;
    }

    // Invoked by buttons in core.lml template:
    @LmlAction("sendPing")
    public void sendPingPacket() {
        final Ping ping = new Ping();
        ping.setValue(getInput());
        ping.setClient(true);
        socket.send(ping);
    }

    @LmlAction("sendPong")
    public void sendPongPacket() {
        final Pong pong = new Pong();
        pong.setValue(getInput());
        pong.setServer(false);
        socket.send(pong);
    }

    private int getInput() {
        if (inputField.isEmpty()) {
            return 0;
        }
        final int input = Integer.parseInt(inputField.getText());
        inputField.setText(Strings.EMPTY_STRING);
        return input;
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    protected void render(final float deltaTime) {
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void dispose() {
        WebSockets.closeGracefully(socket); // Null-safe closing method that catches and logs any exceptions.
        Disposables.disposeOf(stage);
        VisUI.dispose();
    }

    // LmlView methods, used to parse core.lml template:
    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public String getViewId() {
        return "core";
    }
}
