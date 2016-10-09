Newest change logs are in the `README.md` file.

# LML-Vis 1.X

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

1.2 -> 1.3

- `allowAlphaEdit` (`allowAlpha`) attribute for `colorPicker` tag.
- `VisDialog` style setting is no longer ignored.
- Fixed `TabbedPane` with custom showing/hiding actions. `Action`-extending object was not included in GWT reflection, which caused runtime errors.
- Fixed `colorPicker` attribute. Since `setColor` method calls color picker's listener, there was one extra unnecessary listener call before the picker was shown. Now listener is cleared before internally calling `setColor`.
- Added support for `TabbedPane` tab disabling. `Tab` now supports custom `disable` attribute.
- `maxLength` attribute in `numberSelector` tag. Affects internal `VisTextField`.
- `Draggable` listener supported through `draggable` tag. The tag will add the listener to its direct parent actor. If the parent is a `DragPane`, it will set the listener to all its children instead.
- `DragPane` group supported through `dragPane` tag. As most groups, accepts all widgets and converts plain text to labels. Allows to modify its content through actor dragging.
- `itemWidth` and `itemHeight` attributes added to `gridGroup` tag.
- `image` (with `icon` alias) attribute available for `VisImageButton` and `VisImageTextButton`. These attributes replace `imageUp` drawable in buttons' styles: if this is the only drawable in the style, it will be always drawn on the button. You can just use a single style without any icons and manage buttons' images through these attributes.
- Added new color pickers. `BasicColorPicker` and `ExtendedColorPicker` widgets are supported through `basicColorPicker` and `extendedColorPicker` tags.