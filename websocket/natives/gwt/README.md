# LibGDX Web Sockets for GWT
GWT natives for [LibGDX Web Sockets](../..). Make sure to call `GwtWebSockets.initiate()` before creating web sockets:
```
        @Override
        public ApplicationListener createApplicationListener() {
            // Initiating web sockets module - safe to call before creating application listener:
            GwtWebSockets.initiate();
            return new MyApplicationListener();
        }
```

## Dependencies
`Gradle` dependency (for GWT LibGDX project):
```
        compile "com.github.czyzby:gdx-websocket-gwt:$libVersion.$gdxVersion"
        compile "com.github.czyzby:gdx-websocket-gwt:$libVersion.$gdxVersion:sources"
```
`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).

GWT module:
```
        <inherits name='com.github.czyzby.websocket.GdxWebSocketGwt' />
```
