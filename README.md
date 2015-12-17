#LibGDX Autumn GWT
GWT natives for [LibGDX Autumn](https://github.com/czyzby/gdx-autumn) - dependency injection with component scan mechanism.

##Reflection
Autumn GWT generates an object aware of all LibGDX-reflected classes, registered with standard `gdx.reflect.include` (and omitted with `.exclude`). `GwtClassScanner` is the default Autumn's `ClassScanner` for GWT - it will detect all annotated classes that were included for LibGDX reflection.

### Troubleshooting
Sometimes upon application initiation you might see an error like this:

```
	(...) Couldn't find Type for class (...)
```

This means that the class was referenced by some reflection-based methods, but was not properly included in the reflection pool. An error like this might be thrown for all kinds of strange reasons, with all kinds of unexpected classes. While it might be thrown for types that you commonly inject as assets in Autumn, like Sound or TextureAtlas, it also might need some obscure class that you don't even seem to use. This is because this class might be requested for some kind of referenced reflected method of field.

Adding these classes to reflection pool solves the problem most of the times (if it doesn't - don't hesitate to contact me). After replacing names with your packages and classes, try adding these to your GWT module:

```
	<extend-configuration-property name="gdx.reflect.include" value="path.of.problematic.package" />
	<extend-configuration-property name="gdx.reflect.include" value="problematic.package.ProblematicClass" />
```

##Dependency
Gradle dependency (for GWT project):

```
    compile "com.github.czyzby:gdx-autumn-gwt:1.4.$gdxVersion"
    compile "com.github.czyzby:gdx-autumn-gwt:1.4.$gdxVersion:sources"
```

Currently supported LibGDX version is **1.7.2**.

GWT module:

```
    <inherits name='com.github.czyzby.autumn.gwt.GdxAutumnGwt' />
```