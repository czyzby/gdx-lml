package com.github.czyzby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.kiwi.util.gdx.AbstractApplicationListener;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.collection.disposable.DisposableArray;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.LmlView;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.vis.util.VisLml;
import com.github.czyzby.websocket.JsonWebSocketTest;
import com.github.czyzby.websocket.SerializationWebSocketTest;
import com.github.czyzby.websocket.StringWebSocketTest;
import com.github.czyzby.websocket.WebSocketTest;
import com.github.czyzby.websocket.WebSocketTest.ResponseListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;

// LmlView and ActionContainer are interfaces used to for UI library: gdx-lml-vis.
// AbstractApplicationListener is an alternative to ApplicationAdapter from gdx-kiwi.
public class Core extends AbstractApplicationListener implements LmlView, ActionContainer, ResponseListener {
    /** Preferred window size. Virtual size of viewport. */
    public static final int WIDTH = 600, HEIGHT = 480;

    private Stage stage;

    // Injected by gdx-lml-vis parser during processing of core.lml:
    @LmlActor("response") Label responseLabel;
    @LmlActor("input") VisTextField inputField;
    @LmlActor("array") Button arrayCheckBox;
    @LmlActor("list") Button listCheckBox;

    private final WebSocketTest stringTest = new StringWebSocketTest();
    private final WebSocketTest jsonTest = new JsonWebSocketTest();
    private final WebSocketTest serializationTest = new SerializationWebSocketTest();
    private final DisposableArray<WebSocketTest> tests = DisposableArray.of(stringTest, jsonTest, serializationTest);

    @Override
    public void create() {
        // Creating UI with gdx-lml-vis:
        stage = new Stage(new FitViewport(WIDTH, HEIGHT));
        Gdx.input.setInputProcessor(stage);
        VisUI.load();
        VisLml.parser().build().createView(this, Gdx.files.internal("core.lml"));
        // Smooth screen fading in:
        stage.addAction(Actions.sequence(Actions.alpha(0f), Actions.alpha(1f, 1f)));

        // Connecting with the server:
        for (final WebSocketTest test : tests) {
            test.setListener(this);
            test.connect("localhost");
        }
    }

    @Override
    public void onMessage(final String message) {
        responseLabel.setText(message);
    }

    // Invoked by buttons in core.lml template:
    @LmlAction("sendString")
    public void sendStringPacket() {
        send(stringTest);
    }

    @LmlAction("sendJson")
    public void sendJsonPacket() {
        send(jsonTest);
    }

    @LmlAction("sendSerialization")
    public void sendSerializationPacket() {
        send(serializationTest);
    }

    /** @param toTest sends current message to the server using the chosen socket. */
    private void send(final WebSocketTest toTest) {
        final String message = inputField.getText();
        if (arrayCheckBox.isChecked()) {
            final String[] messages = new String[100];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = message;
            }
            toTest.send(messages);
        } else if (listCheckBox.isChecked()) {
            toTest.send(message, 100);
        } else {
            toTest.send(message);
        }
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
        tests.dispose();
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
