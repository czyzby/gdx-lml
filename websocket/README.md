# LibGDX Web Sockets

Default LibGDX `Net` API provides only TCP sockets and HTTP requests. This library aims to add client-side web sockets support.

`ExtendedNet` (as unfortunate as it might sound) contains additional methods for opening web sockets, as well as some static instance providers. `WebSockets` class has some general web sockets utilities. Both binary and text packets are supported on every platform (but note that older browsers might have problems with binary data). The code is heavily documented.

See example project [using simple string communication](../examples/gdx-websocket-tests) or [using JSON serialization](../examples/gdx-websocket-json) for more info and a basic working application examples. [This mock-up application](../examples/gdx-lml-vis-websocket) uses `gdx-websocket` to connect with an external web socket echo server. *Note: examples might depend on latest snapshot versions and use new features.*

Note that this library contains *only* the web sockets *abstraction* - it has the necessary interfaces, basic serialization (using LibGDX API) and some abstract implementations, but not much else. Every platform has to include a specific library with the actual implementation and initiate its module before using web sockets. Make sure to check out natives libraries `READMEs`.

## Dependencies
`Gradle` dependency (for LibGDX core project):
```
         compile "com.github.czyzby:gdx-websocket:$libVersion.$gdxVersion"
```
`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).

GWT module:
```
         <inherits name='com.github.czyzby.websocket.GdxWebSocket' />
```

### Natives

Desktop/Android [natives](natives/common):
```
         compile "com.github.czyzby:gdx-websocket-common:$libVersion.$gdxVersion"
```

GWT [natives](natives/gwt):
```
        compile "com.github.czyzby:gdx-websocket-gwt:$libVersion.$gdxVersion"
        compile "com.github.czyzby:gdx-websocket-gwt:$libVersion.$gdxVersion:sources"
```

GWT natives module:
```
        <inherits name='com.github.czyzby.websocket.GdxWebSocketGwt' />
```

### Extensions

- [gdx-websocket-serialization](natives/serialization): a custom serialization mechanism, not based on reflection. Alternative to JSON-based communication. More verbose, but gives you full control over (de)serialization process. Useful for performance-critical applications. Check out its [example project](../examples/gdx-websocket-serialization-tests).

## Changes

1.5 -> 1.6

- Added `AbstractWebSocketListener`, which handles object deserialization and logs errors. This is a solid base for your `WebSocketListener` implementation if don't use pure string-based communication. 
- Added `WebSocketHandler`, which extends `AbstractWebSocketListener` even further. Instead of dealing with raw `Object` types and having to determine packet type on your own, you can register a `Handler` to a specific packet class and it will be invoked each time a packet of the selected type is received.
- Added default `Serializer` implementation: `JsonSerializer`. Uses **LibGDX** `Json` API to serialize objects as strings.
- Added `WebSockets#DEFAULT_SERIALIZER`. Modify this field to automatically assign serializer of your choice to all new web socket instances.
- Added `Base64Serializer`. Uses **LibGDX** `Base64Coder` API to encode and decode the data to and from *BASE64*. Wraps around an existing serializer.
- Added custom serialization in [gdx-websocket-serialization](natives/serialization) library. `ManualSerializer` is an alternative to the default `JsonSerializer`.
- Added `WebSockets#closeGracefully(WebSocket)` null-safe utility method. Attempts to close the passed web socket and catches any thrown exceptions (their message is logged using `Gdx.app.debug` method). If passed web socket is null, it will be ignored. Useful for application disposing methods, when you don't exactly care if the web socket is not properly closed and *have to* continue disposing other native assets, even if `close()` call fails.
