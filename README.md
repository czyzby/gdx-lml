#LibGDX Autumn FCS
Desktop "natives" for [LibGDX Autumn](https://github.com/czyzby/gdx-autumn) - dependency injection with component scan mechanism.

## Scanner
`DesktopClassScanner` implements Autumn's `ClassScanner` interface and is the default scanner for desktop applications. It uses `fast-classpath-scanner` library for efficient scanning that does not depend on reflection. Basically, it is a thin wrapper over `FastClasspathScanner` class that satisfies Autumn's needs.

### Alternatives
If, for some reason, class scanning does not work on your desktop target platform or you do not want another dependency, you can try to use `FallbackDesktopClassScanner` (from base `gdx-autumn` library) - it uses reflection to look for annotations, so it's much less efficient. `FDCS` is compatible with Java 6. If that's also not an option, use `FixedClassScanner` and provide classes that you want to be scanned - while this defeats the whole purpose of automated class scanning, it should definitely work on every platform.

##Dependency
Gradle dependency (for desktop LibGDX project):

```
    compile "com.github.czyzby:gdx-autumn-fcs:1.3.$gdxVersion"
```

Currently supported LibGDX version is **1.7.1**.