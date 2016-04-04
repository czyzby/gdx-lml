# LibGDX Web Sockets

Default LibGDX `Net` API provides only TCP sockets and HTTP requests. This library aims to add client-side web sockets support.

`ExtendedNet` (as unfortunate as it might sound) contains additional methods for opening web sockets, as well as some static instance providers. `WebSockets` class has some general web sockets utilities. Both binary and text packets are supported on every platform (but note that older browsers might have problems with binary data). The code is heavily documented.

See [example project](https://github.com/czyzby/gdx-lml/tree/master/examples/gdx-websocket-tests) for more info and a basic working application example.

Note that this library contains the web sockets *abstraction* - it has the necessary interfaces and abstract implementations, but not much else. Every platform has to include a specific library with the actual implementation and initiate its web socket module. Make sure to check out natives libraries `READMEs`.

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

Desktop/Android natives:
```
         compile "com.github.czyzby:gdx-websocket-common:$libVersion.$gdxVersion"
```

GWT natives:
```
        compile "com.github.czyzby:gdx-websocket-gwt:$libVersion.$gdxVersion"
        compile "com.github.czyzby:gdx-websocket-gwt:$libVersion.$gdxVersion:sources"
```

GWT natives module:
```
        <inherits name='com.github.czyzby.websocket.GdxWebSocketGwt' />
```
