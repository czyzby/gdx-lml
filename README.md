#LibGDX Markup Language
Templates for LibGDX Scene2D with HTML-like syntax and FreeMarker-inspired macros.

##Sample project
See [gdx-lml-tests](http://github.com/czyzby/gdx-lml-tests).

##Maven artifact
To import LML with Gradle, add this dependency to your core:
```
    compile "com.github.czyzby:gdx-lml:0.2.$gdxVersion"
```
`$gdxVersion` can be replaced with 1.5.5.

If you want to use LML with GWT, you have to add this module to your GdxDefinition:
```
	<inherits name='com.github.czyzby.lml.GdxLml' />
```
Also, if you're using reflected action with `ActionContainers` on GWT, don't forget to register them as reflected classes:
```
	<extend-configuration-property name="gdx.reflect.include" value="your.reflected.package" />
```

##Documentation
See [LibGDX forum thread](http://www.badlogicgames.com/forum/viewtopic.php?f=17&t=18843) and [example project](http://github.com/czyzby/gdx-lml-tests).
