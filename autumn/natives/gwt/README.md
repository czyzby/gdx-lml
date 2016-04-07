# LibGDX Autumn GWT
GWT natives for [LibGDX Autumn](https://github.com/czyzby/gdx-lml/tree/master/autumn) - dependency injection with component scan mechanism.

## Scanner
Autumn GWT generates an object aware of all LibGDX-reflected classes, registered with standard `gdx.reflect.include` (and omitted with `gdx.reflect.exclude`) GWT definition property. `GwtClassScanner` is the default Autumn's `ClassScanner` implementation for GWT - it will detect all annotated classes that were included for LibGDX reflection.

### Troubleshooting
Sometimes upon application initiation you might see an error like this:

```
        (...) Couldn't find Type for class (...)
```

This means that the class was referenced by some reflection-based methods, but was not properly included in the reflection pool. An error like this might be thrown for all kinds of strange reasons, with all kinds of unexpected classes. While it might be thrown for types that you commonly inject as assets in Autumn, like `Sound` or `TextureAtlas`, it also might need some obscure class that you don't even seem to use.

Adding these classes to reflection pool solves the problem most of the times (if it doesn't - don't hesitate to contact me). After replacing names with your packages and classes, try adding one of these to your GWT module:

```
        <extend-configuration-property name="gdx.reflect.include" value="path.of.problematic.package" />
        <extend-configuration-property name="gdx.reflect.include" value="problematic.package.ProblematicClass" />
```

If one of your components fails to initiate, make sure that all of its extended super classes (and, if desperate, all implemented interfaces) are included in reflection pool. This is sometimes the issue, as super classes might need to be reflected for correct scan of annotations.

## Dependencies
`Gradle` dependency (for GWT LibGDX project):
```
        compile "com.github.czyzby:gdx-autumn-gwt:$libVersion.$gdxVersion"
        compile "com.github.czyzby:gdx-autumn-gwt:$libVersion.$gdxVersion:sources"
```
`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).

GWT module:
```
        <inherits name='com.github.czyzby.autumn.gwt.GdxAutumnGwt' />
```
