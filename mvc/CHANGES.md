Newest change logs are in the `README.md` file.

# Autumn MVC 1.X

1.2 -> 1.3:

- `SkinService` now has an `addSkin(String, Skin)` method, allowing you to manually load and register a skin. You could load your skin manually before, but you had to add it to `LmlParser` in `InterfaceService` and dispose it on your own. Now `SkinService` does that for you.
- Action executed after locale changing is now in a static class: `LocaleService$LocaleChangeAction`. This allows for easier locale changing action tweaking.
- Added `InterfaceService#setActionOnBundlesReload(Runnable)`, allowing to specify an action executed each time registered `I18NBundle` objects are loaded.
- Now `@ViewDialog` can also support `Window` child. This is meant to be more flexible when it comes to custom `Dialog` implementations (like `VisDialog` in `VisUI`, which does not extend `Dialog` class).
- `InterfaceService#initiateAllControllers()` now also initiates dialog controllers (those with dialog instance caching turned on). This might be an overlooked method, but it generally should be called after assets are loaded to greatly speed up screen transitions on platforms that have a hard time reading files and creating views as fast as regular desktop applications (GWT).
- Android class scanner implemented in [Autumn Android](http://github.com/czyzby/gdx-autumn-android).

1.1 -> 1.2:

- Fixed `@SkinAsset` injection. Now it throws meaningful exceptions when invalid skin ID is passed.
- `@LmlParserSyntax` annotation. This is a preference annotation that can be used to quickly change internal `LmlParser`'s syntax. `@LmlParserSyntax` should annotate a field which contains an implementation of `LmlSyntax` - its value will be used to replace the current syntax of the parser kept by `InterfaceService`. This happens before any templates are parsed.
- Music settings will now be properly read from and saved in preferences.

0 -> 1

- Updated to LML 1. Removed `ViewActor` annotation; now LML's `LmlActor` is used. Added support for new LML features: `@OnChange`, `@LmlActor`, `@LmlAction`, etc. Make sure to update your LML templates according to the updated syntax. See LML docs for more info.
- Updated to Autumn 1. All annotation processors were refactored to match the new API; their functionality stayed pretty much the same, unless noted below.
- `StageViewport` annotation now can also annotate a class that implements `ObjectProvider<Stage>`; previously only fields were supported.
- While updating from 0 to 1, Kiwi and Autumn MVC has changed the least out of my LibGDX libraries. Actually, the biggest change in Autumn MVC itself was updating the annotation processors and refactoring view managers to match new Autumn and LML features, with a few new small functionalities here and there that were trivial to add with the new systems.

# Autumn MVC 0.X

The last version that uses LML 0 and Autumn 0 is `0.8.1.7.0` (or `0.9.1.7.0-SNAPSHOT`).

0.7 -> 0.8

- @ViewActor annotation now takes an array of strings, making it possible to inject multiple actors into one field. Supported collection types are Array, ObjectSet and ObjectMap - standard LibGDX collections. If you use an Array, the injected actors' order will match the order of actor IDs that you pass into the annotation. If you use an ObjectMap, it has to be a `<String,CommonActorSuperClass>` map - actors will be put into the map with the key matching their ID. If you pass an empty array to the ViewActor annotation (default behavior), an array containing field's name will be used instead. Note that this does not break any existing code; if the annotated field is not a collection, actor mapped by the first ID in the annotation's string array (or mapped by field's name) will be injected directly into the field.

0.7.1.6.1 -> 0.7.1.6.4:

- @SkinAsset annotation. Injects objects mapped in the Skin after it is fully loaded.
- Skin-related logic was moved from InterfaceService (which seems to, unfortunately, still do too much stuff) to SkinService. InterfaceService#getSkin method is still available though.
- Action priorities slightly changed. InterfaceService now initiates with VERY_HIGH instead of TOP priority.
- All Autumn MVC messages are now stored in AutumnMessage class. `AssetService.ASSETS_LOADED_MESSAGE` was moved to AutumnMessage.

0.6 -> 0.7:

- Default view shower will remove all tooltips and dialogs before the screen is shown, making sure that previously opened "helper" widgets will not be present on the view on the next showing. Default view shower can be changed globally in InterfaceService static methods, so this behavior can be changed.
- reload(Runnable) method added to InterfaceService, now you can request reloading of all screen and execute a custom action after the current screen is hidden. Useful for custom actions that require reloading of all screens (like viewport ratio change).
- show(Class, Runnable) method added to InterfaceService. Now you can transfer to another screen with a custom action that will be executed after the current screen is hidden.
- InterfaceService now has methods that return (defensive copies of) arrays that contain all managed views and dialogs controllers. These methods might be useful if you want to iterate over all created stages and manually change their viewports, for example (note: resizing is already handled for you, don't worry). This is not something you will probably do very often, but if you really have to - copy the content of the returned arrays to avoid creating multiple array objects. Managed controllers collections usually do not change over time anyway.
- Now passing actor ID in @ViewActor is optional - if you don't specify actor ID in the annotation, field's name will be used as the ID (basically, it will look for an actor with "id" tag attribute equal to the field's name). If you want to obfuscate your code, you might want to stick with the annotation variable though.

0.5 -> 0.6:

- Since Autumn no longer uses custom reflection wrappers, API was refactored to use "native" LibGDX reflection.
- Now only absolutely necessary classes are registered for GWT reflection - less meta-data in JS.
- Fixed a bug (?) where dialogs with cached instances where not destroyed on views reload. Now views reload also triggers dialogs destruction.