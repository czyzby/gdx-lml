package com.github.czyzby;

import com.github.czyzby.shared.MyPackets;
import com.github.czyzby.shared.Ping;
import com.github.czyzby.shared.Pong;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

// Note that server web socket implementation is not provided by gdx-websocket. This class uses an external library: Vert.x.
public class ServerLauncher {
    private final Vertx vertx = Vertx.vertx();
    private final ManualSerializer serializer;

    public ServerLauncher() {
        serializer = new ManualSerializer();
        MyPackets.register(serializer);
    }

    public static void main(final String... args) throws Exception {
        new ServerLauncher().launch();
    }

    private void launch() {
        System.out.println("Launching web socket server...");
        final HttpServer server = vertx.createHttpServer();
        server.websocketHandler(webSocket -> {
            // Printing received packets to console, sending response:
            webSocket.frameHandler(frame -> handleFrame(webSocket, frame));
        }).listen(8000);
    }

    private void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
        // Deserializing received message:
        final Object request = serializer.deserialize(frame.binaryData().getBytes());
        System.out.println("Received packet: " + request);
        // Sending a response - Ping to Pong, Pong to Ping.
        if (request instanceof Ping) {
            final Pong response = new Pong();
            response.setValue(((Ping) request).getValue() / 2f);
            response.setServer(true);
            webSocket.writeFinalBinaryFrame(Buffer.buffer(serializer.serialize(response)));
        } else if (request instanceof Pong) {
            final Ping response = new Ping();
            response.setValue((int) ((Pong) request).getValue() * 2);
            response.setClient(false);
            webSocket.writeFinalBinaryFrame(Buffer.buffer(serializer.serialize(response)));
        }
    }
}
