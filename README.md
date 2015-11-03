#VisUI parser for LML templates.

See [gdx-lml](http://github.com/czyzby/gdx-lml). Instead of building your parser with `Lml` class (or `LmlParserBuilder`), use `VisLml` (or `VisLmlParserBuilder`).

## Examples

Check [gdx-lml-vis-tests](http://github.com/czyzby/gdx-lml-vis-tests) for examples of all tags and macros usage.

## Dependency

Gradle:
```
    compile "com.github.czyzby:gdx-lml-vis:1.2.$gdxVersion"
```
Currently supported LibGDX version is **1.7.1**.

GWT module:
```
	<inherits name='com.github.czyzby.lml.vis.GdxLmlVis' />
```

### Including gdx-lml-vis in Autumn MVC

In one of your components (possibly in a configuration component, if you have one), include an annotated syntax field:

```
@LmlParserSyntax VisLmlSyntax syntax = new VisLmlSyntax();
```

Make sure that you call `VisUI.load()` and register Vis skin in LML parser. This can be easily done with an initiation method:

```
@Initiate(AutumnActionPriority.TOP_PRIORITY) // ...or higher.
void init(InterfaceService interfaceService) {
    VisUI.load();
    interfaceService.getParser().getData().setDefaultSkin(VisUI.getSkin());
}
```

### Non-GWT features

To add non-GWT features (like the `FileChooser` support or various file validators from `FormValidator` class), use `ExtendedVisLml#extend(LmlParser)` method. This will register all the extra attributes and tags at the cost of nasty compilation errors on GWT platform.

## Changes

1.2 -> 1.3

- `allowAlphaEdit` (`allowAlpha`) attribute for `colorPicker` tag.