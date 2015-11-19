#VisUI parser for LML templates.

See [gdx-lml](http://github.com/czyzby/gdx-lml). Instead of building your parser with `Lml` class (or `LmlParserBuilder`), use `VisLml` (or `VisLmlParserBuilder`).

## Examples

Check [gdx-lml-vis-tests](http://github.com/czyzby/gdx-lml-vis-tests) for examples of all tags and macros usage.

See [online demo](http://vis.kotcrab.com/demo/lml/).

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

## Changes

1.2 -> 1.3

- `allowAlphaEdit` (`allowAlpha`) attribute for `colorPicker` tag.
- `VisDialog` style setting is no longer ignored.
- Fixed `TabbedPane` with custom showing/hiding actions. `Action`-extending object was not included in GWT reflection, which caused runtime errors.
- Fixed `ColorPicker` attribute. Since `setColor` method calls color picker's listener, there was one extra unnecessary listener call before the picker was shown. Now listener is cleared before internally calling `setColor`.
- Added support for `TabbedPane` tab disabling. `Tab` now supports custom `disable` attribute.
- `maxLength` attribute in `numberSelector` tag. Affects internal `VisTextField`.
- `Draggable` listener supported through `draggable` tag. The tag will add the listener to its direct parent actor. If the parent is a `DragPane`, it will set the listener to all its children instead.
- `DragPane` group supported through `dragPane` tag. As most groups, accepts all widgets and converts plain text to labels. Allows to modify its content through actor dragging.
- `itemWidth` and `itemHeight` attributes added to `gridGroup` tag.
- `image` (with `icon` alias) attribute available for `VisImageButton` and `VisImageTextButton`. These attributes replace `imageUp` drawable in buttons' styles: if this is the only drawable in the style, it will be always drawn on the button. You can just use a single style without any icons and manage buttons' images through these attributes.