Newest change logs are in the `README.md` file.

# LML-Vis 1.X

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