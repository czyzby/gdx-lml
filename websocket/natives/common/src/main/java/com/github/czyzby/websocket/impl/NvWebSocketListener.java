package com.github.czyzby.websocket.impl;

import java.util.List;
import java.util.Map;

import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

/** Adapter for NV web socket listener. Wraps around {@link NvWebSocket}.
 *
 * @author MJ */
public class NvWebSocketListener extends WebSocketAdapter {
    private final NvWebSocket socket;

    public NvWebSocketListener(final NvWebSocket socket) {
        this.socket = socket;
    }

    @Override
    public void onError(final WebSocket websocket, final WebSocketException cause) throws Exception {
        socket.postErrorEvent(cause);
    }

    @Override
    public void onConnected(final WebSocket websocket, final Map<String, List<String>> headers) throws Exception {
        socket.postOpenEvent();
    }

    @Override
    public void onDisconnected(final WebSocket websocket, final WebSocketFrame serverCloseFrame,
            final WebSocketFrame clientCloseFrame, final boolean closedByServer) throws Exception {
        if (closedByServer) {
            triggerOnDisconnectEvent(serverCloseFrame.getCloseCode(), serverCloseFrame.getCloseReason());
        } else {
            triggerOnDisconnectEvent(clientCloseFrame.getCloseCode(), clientCloseFrame.getCloseReason());
        }
    }

    private void triggerOnDisconnectEvent(final int closeCode, final String closeReason) {
        socket.postCloseEvent(WebSocketCloseCode.getByCodeOrElse(closeCode, WebSocketCloseCode.ABNORMAL), closeReason);
    }

    @Override
    public void onConnectError(final WebSocket websocket, final WebSocketException exception) throws Exception {
        onError(websocket, exception);
    }

    @Override
    public void onTextMessageError(final WebSocket websocket, final WebSocketException cause, final byte[] data)
            throws Exception {
        onError(websocket, cause);
    }

    @Override
    public void onUnexpectedError(final WebSocket websocket, final WebSocketException cause) throws Exception {
        onError(websocket, cause);
    }

    @Override
    public void onBinaryMessage(final WebSocket websocket, final byte[] binary) throws Exception {
        if (binary != null && binary.length > 0) {
            socket.postMessageEvent(binary);
        }
    }

    @Override
    public void onTextMessage(final WebSocket websocket, final String text) throws Exception {
        if (text != null && text.length() > 0) {
            socket.postMessageEvent(text);
        }
    }
}
