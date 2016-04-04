# LibGDX Autumn FCS
Desktop "natives" for [LibGDX Autumn](https://github.com/czyzby/gdx-lml/tree/master/autumn) - dependency injection with component scan mechanism.

## Scanner
`DesktopClassScanner` implements Autumn's `ClassScanner` interface and is the default scanner for desktop applications. It uses [`fast-classpath-scanner`](https://github.com/lukehutch/fast-classpath-scanner) library for efficient scanning that does not depend on reflection. Basically, it is a thin wrapper over `FastClasspathScanner` class, satisfying Autumn's needs.

### Alternatives
If, for some reason, class scanning does not work on your desktop target platform or you do not want another dependency, you can try to use `FallbackDesktopClassScanner` (from basic `gdx-autumn` library) - it uses reflection to look for annotations, so it's much less efficient. `FDCS` is compatible with Java 6. If `FDCS` is also not an option, use `FixedClassScanner` and provide classes that you want to be scanned - while this defeats the whole purpose of automated class scanning, it should definitely work on every platform.

## Dependencies
`Gradle` dependency (for desktop LibGDX project):
```
        compile "com.github.czyzby:gdx-autumn-fcs:$libVersion.$gdxVersion"
```
`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).
