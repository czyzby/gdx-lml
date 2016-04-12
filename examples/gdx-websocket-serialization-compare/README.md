# gdx-websocket-serialization

This is a simple example of a web socket based communication between the server and the clients, exchanging packets serialized with [`gdx-websocket-serialization`](https://github.com/czyzby/gdx-lml/tree/master/websocket/natives/serialization). It allows to compare its performance with other common serialization methods. Note that it does not come packed with Gradle wrapper, so make sure to install it on your own.

Client uses *LibGDX* (obviously) along with [`gdx-websocket`](https://github.com/czyzby/gdx-lml/tree/master/websocket) to communicate with the server and [`gdx-lml-vis`](https://github.com/czyzby/gdx-lml/tree/master/lml-vis) to create GUI. Desktop uses `gdx-websocket-common` natives library, while GWT project depends on `gdx-websocket-gwt`. Server is built with a few lines of code thanks to the amazing *Vert.x* framework.

### Running the application

The client application allows you to send a message as a simple string, `JSON` object or data serialized with `gdx-websocket-serialization`. Server will respond with the amount of bytes it received and how long it took to deserialize the packet (if the server was able to determine its type in the first place).

You can check out application's behavior without an IDE thanks to Gradle tasks:

- `gradle server:run` launches the server.
- `gradle desktop:run` launches desktop client. Note that `gdx-websocket-common` library should also work on Android (hence the name), so Android code and dependencies would be similar.
- `gradle html:superDev` compiles GWT application and provides the web client version on `8080` port. Visit `http://localhost:8080/html` to check it out.

Note that this example sends data as byte arrays. Although binary format might not be supported by some browsers, WebGL would probably be unavailable there anyway, so this is the preferred way. If you want to send JSON data as string web socket frames, use `WebSocket#setSerializeAsString` method.
