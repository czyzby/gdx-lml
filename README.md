#LibGDX Autumn GWT
GWT natives for [LibGDX Autumn](https://github.com/czyzby/gdx-autumn) - dependency injection with component scan mechanism.

##Reflection
Autumn GWT generates an object aware of all LibGDX-reflected classes, registered with standard `gdx.reflect.include` (and omitted with `.exclude`). `GwtClassScanner` is the default Autumn's `ClassScanner` for GWT - it will detect all annotated classes that were included for LibGDX reflection.

### Troubleshooting
Sometimes upon application initiation you might see an error like this:

```
	(...) Couldn't find Type for class (...)
```

This means that the class was referenced by some reflection-based methods, but was not properly included in the reflection pool. This might be thrown for types that you commonly inject as assets in Autumn MVC, like Sound or TextureAtlas, for example. Adding these classes to reflection pool solves the problem most of the times (if it doesn't - don't hesitate to contact me).

##Dependency
Gradle dependency (for GWT project):

```
    compile "com.github.czyzby:gdx-autumn-gwt:0.7.$gdxVersion"
    compile "com.github.czyzby:gdx-autumn-gwt:0.7.$gdxVersion:sources"
```

Currently supported LibGDX version is **1.6.4**.

GWT module:

```
    <inherits name='com.github.czyzby.autumn.gwt.GdxAutumnGwt' />
```