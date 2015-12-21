#LibGDX Autumn Android
Android "natives" for [LibGDX Autumn](https://github.com/czyzby/gdx-autumn) - dependency injection with component scan mechanism.

## Scanner
`AndroidClassScanner` implements Autumn's `ClassScanner` interface and is the default scanner for Android applications.

### Alternatives
If, for some reason, class scanning does not work on Android, I'm afraid you're stuck with `FixedClassScanner` - you have to register "scanned" classes manually, so this defeats the whole purpose of automated class scanning, but it should definitely work on every platform.

## Dependencies
`Gradle` dependency (for Android LibGDX project):
```
    compile "com.github.czyzby:gdx-autumn-android:1.4.$gdxVersion"
```

Currently supported LibGDX version is **1.7.2**.