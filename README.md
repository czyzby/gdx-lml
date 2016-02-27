#[VisUI](https://github.com/kotcrab/VisEditor/wiki/VisUI) parser for [LML](http://github.com/czyzby/gdx-lml) templates

See [gdx-lml](http://github.com/czyzby/gdx-lml). This extension allows *LML* to create improved *VisUI* widgets instead of regular *Scene2D* actors.

## Examples

Check [gdx-lml-vis-tests](http://github.com/czyzby/gdx-lml-vis-tests) for examples of all tags and macros usage.

See [online demo](http://vis.kotcrab.com/demo/lml/) (might be slightly outdated).

## Dependencies

Gradle:
```
    compile "com.github.czyzby:gdx-lml-vis:1.5.$gdxVersion"
```
Currently supported LibGDX version is **1.9.2**.

GWT module:
```
	<inherits name='com.github.czyzby.lml.vis.GdxLmlVis' />
```
On GWT, you might also want to include sources of `gdx-lml-vis` and currently used `VisUI` library version.

## Usage

Start with [LML tutorial](https://github.com/czyzby/gdx-lml/wiki/Tutorial) - *gdx-lml-vis* works pretty much like regular LML, it just parses templates to different actors, provides additional widgets and contains default skins. Make sure to check out the [example project](http://github.com/czyzby/gdx-lml-vis-tests) and [VisUI features](https://github.com/kotcrab/VisEditor/wiki/VisUI).

### Upgrading regular *gdx-lml* to *gdx-lml-vis*

Instead of building your parser with `Lml` class (or `LmlParserBuilder`), use `VisLml` (or `VisLmlParserBuilder`). Everything else is pretty straightforward: `VisLmlSyntax` overrides default *Scene2D* actors with improved *VisUI* widgets, so you can use all of the standard tags. Your existing *LML* templates should work out of the box - you might only need to change style names here and there, since *VisUI* uses its own custom `Skin`.

### Non-*GWT* features

To add non-GWT features (like the `FileChooser` support or various file validators from `FormValidator` class), use `ExtendedVisLml#extend(LmlParser)` method. This will register all the extra attributes and tags at the cost of nasty compilation errors on GWT platform.

### Including *gdx-lml-vis* in [Autumn MVC](https://github.com/czyzby/gdx-autumn-mvc)

In one of your components (possibly in a configuration component, if you have one), include an annotated field with syntax instance:

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

By using `SkinService#addSkin` method, the disposing of VisUI skin and registering it in LML parser is already handled for you. Still, you might also want to manually dispose of the `ColorPicker` instance (as it is reused for performance reasons) if you ever used one:

```
@Destroy
public static void destroyColorPicker() {
    ColorPickerContainer.dispose();
}
```

Note that by making the method static, instance of the class containing the method will not be kept, so it can be safely garbage collected after application initiation. This is very useful if you want to keep such settings in a single configuration class.

## What's new

1.5 -> 1.6

- Removed `/*` alias for comment macro. Since `DTD` creator was added to LML, now it is possible to create templates that are somewhat-valid `XML` files. `/*` was the only default tag that used forbidden `XML` characters.

1.4 -> 1.5

- As `vertical` style was removed from default skin, `Separator` tag no longer supports `vertical`/`horizontal` attributes.
- `VerticalFlowGroup` and `HorizontalFlowGroup` support. To use these groups in a `dragPane` tag, set `type` attribute to `vFlow` or `hFlow` (as always, case ignored).
- `ListView` support. Now you can display a collection of values in a customized way. Note that currently `ListView` does **NOT** work on GWT due to reflection use in VisUI 1.0.1. It will be fixed in future versions.
- Added `deadzoneRadius` attribute to `draggable` tag.

1.3 -> 1.4

- Added `SwapListener` to `FixedSizeGridGroup`. Now you can manage (and cancel) swap events without having to modify internal `Draggable` listener.
- Added `TabbedPaneLmlTag#getTabbedPane(Table)` method. Since `TabbedPane` does not extend `Actor` class, it cannot be injected to `@LmlActor`-annotated fields - instead, its main table (`TabbedPane#getTable()`) is used. `#getTabbedPane(Table)` method allows to extract the direct reference to `TabbedPane` from its main table - ideally, you'll want to use this in an on-create or on-close method and assign the pane, as this reference might be lost after you clear LML meta-data with `LmlUtilities#clearLmlUserObjects(Iterable<Actor>)` or similar methods.
- Removed `ColumnGroup` support, since this widget is deprecated in the last `VisUI` version. Use `VerticalGroup` instead.
- `FixedSizeGridGroup`, `VisFormTable` and `VisTabTable` are no longer GWT-reflected. They were moved from `com.github.czyzby.lml.vis.ui.reflected` to `com.github.czyzby.lml.vis.ui` package. `MockUpActor` was extracted from `FixedSizeGridGroup` (it was an internal class) and moved to `com.github.czyzby.lml.vis.ui.reflected` package.

### Archive
Older change logs are available in `CHANGES.md` file.
