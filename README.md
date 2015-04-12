#LibGDX Autumn GWT
GWT natives for LibGDX Autumn - dependency injection with component scan mechanism.

##Reflection
Autumn GWT provides its own (limited) reflection implementation, that is far from the complete mechanism, but it does have some functionalities that the LibGDX reflection lacks. It's mostly for internal use - all you have to worry about is registration of your classes in GWT definition with:

```
    <extend-configuration-property name="gdx.autumn.include" value="your.included.package.name" />
    <extend-configuration-property name="gdx.autumn.exclude" value="your.excluded.package.name" />
```

As you can see, it's very similar to `gdx.reflect.include` and `.exclude`.

Note that LibGDX looks for all referenced classes and reflects them all, while Autumn reflection reflects the classes that you include (plus the ones included by default) and nothing else. This is usually enough for Autumn needs.

The default class scanner is `com.github.czyzby.autumn.gwt.scanner.GwtClassScanner`.

##Dependency
Gradle dependency (for GWT project):

```
    compile "com.github.czyzby:gdx-autumn-gwt:0.4.$gdxVersion"
```

GWT module:

```
    <inherits name='com.github.czyzby.autumn.gwt.GdxAutumnGwt' />
```

There is a small bug in 0.4.1.5.5 version, however. *GdxAutumn* module originally inherited *GdxLml* (which is another of my libraries) and I forgot to change it to *GdxKiwi* dependency. If you want to use Autumn without LML, you have to create package `com.github.czyzby.lml` with an empty module: `GdxLml.gwt.xml`, otherwise GWT will be unable to resolve core Autumn dependency. Sorry for that - naturally, it will be fixed in the next versions.