# LibGDX Autumn JTransc
[JTransc](https://github.com/jtransc/gdx-backend-jtransc) natives for [LibGDX Autumn](../..) - dependency injection with component scan mechanism.

## Scanner
`JTranscClassScanner` implements Autumn's `ClassScanner` interface and is the default scanner for JTransc applications.

### Alternatives
If, for some reason, class scanning does not work on your targeted platform or you do not want another dependency, you can try to use `FixedClassScanner` (from basic `gdx-autumn` library) - but the fact that you have to register all expected classes defeats the whole purpose of *automatic* class scanning.

## Dependencies
`Gradle` dependency (for JTransc LibGDX project):
```
        compile "com.github.czyzby:gdx-autumn-jtransc:$libVersion.$gdxVersion"
```
`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).
