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
    compile "com.github.czyzby:gdx-autumn-gwt:0.5.$gdxVersion"
```

GWT module:

```
    <inherits name='com.github.czyzby.autumn.gwt.GdxAutumnGwt' />
```

If you are using Autumn in GWT just for the reflection pool (without context or components - with [LML](http://github.com/czyzby/gdx-lml), for example), make sure to make this call:

```
    Reflection.setReflectionProvider(GwtReflection.getReflectionProvider());
```

This allows to call commonly used Autumn `Reflection`'s methods with the correct provider. Note that this is done internally by `GwtClassScanner`'s constructor, so the call is not necessary if you're using the default scanner.