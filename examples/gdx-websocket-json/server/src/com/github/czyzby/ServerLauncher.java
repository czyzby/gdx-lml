package com.github.czyzby;

import java.util.concurrent.atomic.AtomicInteger;

import com.github.czyzby.shared.MyJsonMessage;
import com.github.czyzby.websocket.serialization.Serializer;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

// Note that server web socket implementation is not provided by gdx-websocket. This class uses an external library: Vert.x.
public class ServerLauncher {
    private final Vertx vertx = Vertx.vertx();
    private final AtomicInteger idCounter = new AtomicInteger();
    private final Serializer serializer = new JsonSerializer();
    // If you uncommented BASE64 serializer in the client, use this serializer instead:
    // private final Serializer serializer = new Base64Serializer(new JsonSerializer());

    public static void main(final String... args) throws Exception {
        new ServerLauncher().launch();
    }

    private void launch() {
        System.out.println("Launching web socket server...");
        final HttpServer server = vertx.createHttpServer();
        server.websocketHandler(webSocket -> {
            // Printing received packets to console, sending response:
            webSocket.frameHandler(frame -> handleFrame(webSocket, frame));
            // Closing the socket in 5 seconds:
            vertx.setTimer(5000L, id -> webSocket.close());
        }).listen(8000);
    }

    private void handleFrame(final ServerWebSocket webSocket, final WebSocketFrame frame) {
        // Deserializing received message:
        final Object request = serializer.deserialize(frame.binaryData().getBytes());
        if (request instanceof MyJsonMessage) {
            System.out.println("Received message: " + ((MyJsonMessage) request).text);
        }

        // Sending a simple response message after 1 second:
        final MyJsonMessage response = new MyJsonMessage();
        response.id = idCounter.getAndIncrement();
        response.text = "Hello client ";
        vertx.setTimer(1000L, id -> webSocket.writeFinalBinaryFrame(Buffer.buffer(serializer.serialize(response))));
    }
}
