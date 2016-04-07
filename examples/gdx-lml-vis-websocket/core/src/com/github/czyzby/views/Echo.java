package com.github.czyzby.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.czyzby.Core;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.annotation.LmlBefore;
import com.github.czyzby.lml.annotation.LmlInject;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.github.czyzby.views.container.WebSocketButtons;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketAdapter;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.kotcrab.vis.ui.widget.VisTextField;

/** Example of cross-platform web sockets usage. Connects with external web sockets echo server. Allows the user to send
 * his input to the server, prints the responses and any major status changes.
 *
 * @author MJ */
public class Echo extends AbstractLmlView {
    private final WebSocket webSocket = ExtendedNet.getNet().newWebSocket("echo.websocket.org", 80);
    private I18NBundle bundle; // Standard LibGDX i18n API.

    @LmlActor("status") Label status;
    @LmlActor("input") VisTextField input;

    /** Value of this field is created and filled by LML parser. Delegating management of widgets disable status from
     * {@link Echo} to another class ({@link WebSocketButtons}) makes the code a little bit cleaner. */
    @LmlInject WebSocketButtons buttons;

    public Echo() {
        super(Core.newStage());
        webSocket.addListener(getListener());
    }

    /** Invoked before Echo.lml template parsing. Extracts {@link I18NBundle} from current LML parser.
     *
     * @param parser used to parse LML template. */
    @LmlBefore
    void assignBundle(final LmlParser parser) {
        bundle = parser.getData().getDefaultI18nBundle();
    }

    @LmlAction("open")
    void connect() {
        if (!webSocket.isSupported()) {
            status.setText(bundle.get("notSupported"));
            return;
        }
        try {
            webSocket.connect();
        } catch (final Exception exception) {
            status.setText(bundle.get("connectionError"));
            Gdx.app.error("WebSocket", exception.getMessage());
        }
    }

    /** @return a new instance of {@link WebSocketListener}, displaying status messages in case of any web socket
     *         events. */
    private WebSocketListener getListener() {
        return new WebSocketAdapter() {
            @Override
            public boolean onOpen(final WebSocket webSocket) {
                buttons.onConnect();
                status.setText(bundle.get("connected"));
                return FULLY_HANDLED;
            }

            @Override
            public boolean onMessage(final WebSocket webSocket, final String packet) {
                status.setText(bundle.get("message") + " " + packet);
                return FULLY_HANDLED;
            }

            @Override
            public boolean onClose(final WebSocket webSocket, final WebSocketCloseCode code, final String reason) {
                buttons.onDisconnect();
                status.setText(bundle.get("disconnected"));
                return FULLY_HANDLED;
            }
        };
    }

    @LmlAction("send")
    void sendMessage() {
        if (webSocket.isOpen()) {
            final String message = input.getText();
            webSocket.send(message);
            input.setText(Strings.EMPTY_STRING);
            status.setText(bundle.get("sent") + " " + message);
        }
    }

    @LmlAction("close")
    void disconnect() {
        if (webSocket.isOpen()) {
            WebSockets.closeGracefully(webSocket); // Null-safe closing method that catches and logs any exceptions.
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        disconnect();
    }

    @Override
    public String getViewId() {
        return "ws";
    }
}
