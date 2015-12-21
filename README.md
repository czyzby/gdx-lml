#VisUI parser for LML templates.

See [gdx-lml](http://github.com/czyzby/gdx-lml). Instead of building your parser with `Lml` class (or `LmlParserBuilder`), use `VisLml` (or `VisLmlParserBuilder`).

## Examples

Check [gdx-lml-vis-tests](http://github.com/czyzby/gdx-lml-vis-tests) for examples of all tags and macros usage.

See [online demo](http://vis.kotcrab.com/demo/lml/).

## Dependencies

Gradle:
```
    compile "com.github.czyzby:gdx-lml-vis:1.4.$gdxVersion"
```
Currently supported LibGDX version is **1.7.2**.

GWT module:
```
	<inherits name='com.github.czyzby.lml.vis.GdxLmlVis' />
```
On GWT, you might also want to include sources of `gdx-lml-vis` and currently used `VisUI` library version.

### Non-GWT features

To add non-GWT features (like the `FileChooser` support or various file validators from `FormValidator` class), use `ExtendedVisLml#extend(LmlParser)` method. This will register all the extra attributes and tags at the cost of nasty compilation errors on GWT platform.

### Including gdx-lml-vis in Autumn MVC

In one of your components (possibly in a configuration component, if you have one), include an annotated syntax field:

```
@LmlParserSyntax VisLmlSyntax syntax = new VisLmlSyntax();
```

Make sure that you call `VisUI.load()` and register Vis skin in LML parser. This can be easily done with an initiation method:

```
@Initiate(priority = AutumnActionPriority.TOP_PRIORITY) // ...or custom, higher.
private void initiate(final SkinService skinService) {
    VisUI.load(); // VisUI.load(Gdx.files.internal("path/to/your/skin.json"));
    skinService.addSkin("default", VisUI.getSkin());
}
```

By using `SkinService`'s `addSkin` method, the disposing of VisUI skin is already handled for you. Still, you might also want to dispose of the `ColorPicker` instance (as it is reused for performance reasons) if you ever needed one:

```
@Destroy
public static void destroyColorPicker() {
    ColorPickerContainer.dispose();
}
```

Note that by making the method static, instance of the class containing the method will not be kept, so it can be safely garbage collected after application initiation. This is very useful if you want to keep such settings in a single configuration class.

## What's new

1.3 -> 1.4

- Added `SwapListener` to `FixedSizeGridGroup`. Now you can manage (and cancel) swap events without having to modify internal `Draggable` listener.
- Added `TabbedPaneLmlTag#getTabbedPane(Table)` method. Since `TabbedPane` does not extend `Actor` class, it cannot be injected to `@LmlActor`-annotated fields - instead, its main table (`TabbedPane#getTable()`) is used. `#getTabbedPane(Table)` method allows to extract the direct reference to `TabbedPane` from its main table - ideally, you'll want to use this in an on-create or on-close method and assign the pane, as this reference might be lost after you clear LML meta-data with `LmlUtilities#clearLmlUserObjects(Iterable<Actor>)` or similar methods.
- Removed `ColumnGroup` support, since this widget is deprecated in the last `VisUI` version. Use `VerticalGroup` instead.
- `FixedSizeGridGroup`, `VisFormTable` and `VisTabTable` are no longer GWT-reflected. They were moved from `com.github.czyzby.lml.vis.ui.reflected` to `com.github.czyzby.lml.vis.ui` package. `MockUpActor` was extracted from `FixedSizeGridGroup` (it was an internal class) and moved to `com.github.czyzby.lml.vis.ui.reflected` package.

### Archive
Older change logs are available in `CHANGES.md` file.