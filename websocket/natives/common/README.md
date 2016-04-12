# LibGDX Web Sockets
Desktop and Android natives for [LibGDX Web Sockets](../..). Based on [nv-websocket-client](https://github.com/TakahikoKawasaki/nv-websocket-client). Make sure to call `CommonWebSockets.initiate()` before creating web sockets:
```
        // Initiating web sockets module - safe to call before creating application:
        CommonWebSockets.initiate();
        new LwjglApplication(new MyApplicationListener());
```

## Dependencies
`Gradle` dependency (for desktop and Android LibGDX projects - safe to use in core project if you don't target any other platforms):
```
        compile "com.github.czyzby:gdx-websocket-common:$libVersion.$gdxVersion"
```
`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).
