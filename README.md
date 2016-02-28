#LibGDX Autumn Android
Android "natives" for [LibGDX Autumn](https://github.com/czyzby/gdx-autumn) - dependency injection with component scan mechanism.

## Scanner
`AndroidClassScanner` implements Autumn's `ClassScanner` interface and is the default scanner for Android applications.

### Alternatives
If, for some reason, class scanning does not work on Android, I'm afraid you're stuck with `FixedClassScanner` - you have to register "scanned" classes manually, so this defeats the whole purpose of automated class scanning, but it should definitely work on every platform.

## Dependencies
`Gradle` dependency (for Android LibGDX project):
```
    compile "com.github.czyzby:gdx-autumn-android:$libVersion.$gdxVersion"
```
`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).