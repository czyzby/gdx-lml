package com.github.czyzby;

import com.github.czyzby.shared.serialization.Packets;
import com.github.czyzby.shared.serialization.ServerResponse;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;
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
    private final JsonSerializer jsonSerializer;

    public ServerLauncher() {
        serializer = new ManualSerializer();
        Packets.register(serializer);
        jsonSerializer = new JsonSerializer();
    }

    public static void main(final String... args) throws Exception {
        new ServerLauncher().launch();
    }

    private void launch() {
        System.out.println("Launching web socket server...");
        HttpServer server = vertx.createHttpServer();
        server.websocketHandler(webSocket -> {
            // String test:
            webSocket.frameHandler(frame -> handleStringFrame(webSocket, frame));
        }).listen(8000);
        server = vertx.createHttpServer();
        server.websocketHandler(webSocket -> {
            // JSON test:
            webSocket.frameHandler(frame -> handleJsonFrame(webSocket, frame));
        }).listen(8001);
        server = vertx.createHttpServer();
        server.websocketHandler(webSocket -> {
            // Serialization test:
            webSocket.frameHandler(frame -> handleSerializationFrame(webSocket, frame));
        }).listen(8002);
    }

    private static void handleStringFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
        final String response = "Packet had " + frame.binaryData().length()
                + " bytes. Cannot deserialize packet class.";
        System.out.println(response);
        webSocket.writeFinalTextFrame(response);
    }

    private void handleJsonFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
        final byte[] packet = frame.binaryData().getBytes();
        final long start = System.nanoTime();
        final Object deserialized = jsonSerializer.deserialize(packet);
        final long time = System.nanoTime() - start;

        final com.github.czyzby.shared.json.ServerResponse response = new com.github.czyzby.shared.json.ServerResponse();
        response.message = "Packet had " + packet.length + " bytes. Class: " + deserialized.getClass().getSimpleName()
                + ", took " + time + " nanos to deserialize.";
        System.out.println(response.message);
        final byte[] serialized = jsonSerializer.serialize(response);
        webSocket.writeFinalBinaryFrame(Buffer.buffer(serialized));
    }

    private void handleSerializationFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
        final byte[] packet = frame.binaryData().getBytes();
        final long start = System.nanoTime();
        final Object deserialized = serializer.deserialize(packet);
        final long time = System.nanoTime() - start;

        final ServerResponse response = new ServerResponse("Packet had " + packet.length + " bytes. Class: "
                + deserialized.getClass().getSimpleName() + ", took " + time + " nanos to deserialize.");
        System.out.println(response.getMessage());
        final byte[] serialized = serializer.serialize(response);
        webSocket.writeFinalBinaryFrame(Buffer.buffer(serialized));
    }
}
