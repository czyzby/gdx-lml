# LibGDX Autumn MVC
Totally not a Spring rip-off (TM).

If you're used to Spring MVC, you may feel like home. Dependency injection, component scan, view templates and a lot of internals are handled for you. The goal of Autumn is to let you focus on game's logic rather than the overall project structure: you define the assets, views and their controllers - Autumn MVC makes it easy to connect and manage them with just a few lines of code and a whole lot of annotations.

The original idea was to make my first LibGDX utilities public, but after I realized how much effort it takes to actually set up all of my managers, I decided that I *need* to make it simpler to use for everyone - including myself. This is the result.

Autumn MVC tries to be as flexible as possible, but it does force a specific project structure and approach. If you want only some parts of the "framework", that's completely fine: [Kiwi](../kiwi) is a set of Guava-inspired utilities for general use (in LibGDX applications), [LML](../lml) is a pretty powerful HTML-like markup language that allows to easily build complex Scene2D UIs, and [Autumn](../autumn) provides annotation-processing mechanism that allows for dependency injection with component scan out of the box. You can use each and every of them in any combination (knowing that both LML and Autumn use Kiwi internally). However, excluding some of Autumn MVC components is usually not an option, as most of them depend on each other.

## Why should you use Autumn MVC
Simply put - to save your time. While a pure Java application without reflection might start or even work slightly faster, it requires you to handle a lot of stuff... stuff that usually LibGDX makes pretty easy to implement, to be honest, but sometimes awkward to use or not fully supported out of the box. Autumn support goes a step further in - hopefully - the right direction. Autumn takes care of:

- asset management,
- i18n,
- preferences,
- UI building complexity,
- objects initiation and dependencies,
- heavy object disposing,
- event posting,
- screen transitions,
- dialog management,
- music themes.

Normally, you would have to implement some kind of system that manages screens, internationalization, asset management, overall application initiation, and possibly a UI builder too. With Autumn MVC, all you care about is creating a configuration file with basic assets paths (bundles, skin) and a few classes annotated with `@View` that reference LML files.

### But I don't like LML...
I can understand that not everyone might be a fan of HTML-like syntax and tedious refactoring. Personally, I find UIs created in Java less readable and too verbose, but if for some reason that's the way you want to go, take the hard way by making your `@View`-annotated classes implement `ViewController` and you will have full control over the screen, without losing SOME features, like the asset management, component injection and so on. There's even an abstract base for views without LML: `AbstractViewController`. However, screen transition mechanism relies on actions, so dropping Scene2D for another UI system is a no-go. You don't have to - or even are encouraged to - use Scene2D for your game logic though.

### Why not...
Why should you use Autumn MVC instead of a professional, mature dependency injection library? Well, if you already have some of your own utilities and not a huge fan of view templates, you will probably do just fine with Dagger or whatever it is you want to use. In the end, it comes down to what saves your time.

However, Autumn was written with LibGDX in mind, so it provides a lot of extra functionalities which you won't find in other component management systems - unless we're talking about other LibGDX extensions, of course. I don't know of any similar semi-frameworks on top of LibGDX though.

To sum up - give it a go and find out if it suits your style.

## How to start
Read on [LML](../lml) to see how the views are created. Check out [Autumn](../autumn) to find out more about the components system. You may also want to know a thing or two about [Kiwi](../kiwi), as these utilities might make your programming a bit easier. Then check out [example project](../examples/gdx-autumn-mvc-simple).

### Application
Instead of implementing `ApplicationListener` or extending `ApplicationAdapter`, use `AutumnApplication`. Actually, you can even use it without extending - this is not an abstract class. Initiating this object requires you to pass a root scanning class (which will usually be the class in the bottom of your package hierarchy) and a class scanner, which is (usually) platform-specific.

After that, you might want to create a single `@Component` class with configuration, that will be initiated and destroyed when the context is being built. By annotating its fields, you can choose skin, i18n bundles, preferences (and so on) that will be used by LML parser. Available configurations:

- `@I18nBundle` - should annotate a string with a path the I18nBundle or a FileHandle pointing to a bundle. Can optionally set bundle ID - multiple bundles can be used in views: specific bundles can be referenced with @bundleId.bundleKey.
- `@I18nLocale` - should annotate a string with a property name (and specify properties path) or a default Locale object (not advised). Allows to set initial locale and - if properties are used - save locale in user's preferences. (Done automatically when changing locale with LocaleService.)
- `@Skin` - should annotate string with a path to the skin. If skin atlas stores bitmap fonts, they can be referenced with annotation parameters and loaded from the same atlas. This setting is necessary for `InterfaceService` to work: Autumn MVC application cannot be initiated without a skin.
- `@Preference` - should annotate a string with a path to application preferences. Currently it is used solely to pass the preferences object to the LML parser, so that the preferences could be referenced with # operator.
- `@SoundVolume`, `@MusicVolume` - should annotate either a float with initial sounds or music volumes (not advised), or a string with the preference name where sound volume should be stored. All music properties should be in one preferences. Allows to save and restore sound settings.
- `@SoundEnabled`, `@MusicEnabled` - should annotate either a boolean with initial sounds or music states (not advised), or a string with the preference name where music state should be stored. Passed preferences should be the same ones that were used for volumes.
- `@StageViewport` - can annotate a `ObjectProvider<Stage>` (see Kiwi lazy variables), that provides viewports used upon stage creation. Default viewport type is the `ScreenViewport`, as it works arguably well for interfaces and does not require additional settings (like virtual screen size).
- `@LmlMacro` - can annotate a String or String[] containing paths to LML files with macro declarations. Since macros have to be parsed once (and their custom parser is created dynamically), keeping macros in separate files and parsing them on init is advised.
- `@AvailableLocales` - can annotate a String[] field containing locales available in game. Annotated array will be passed to the LML templates with the chosen argument name (can be iterated over) and each of the locales in array will add an action with the specified prefix that changes the current locale of the application on invocation.
- `@Property` - custom preference stored in a selected LibGDX `Preferences` file. Can also annotate a class implementing `Preference` interface (or extending `AbstractPreference`). Allows to register custom preferences that are accessible in LML templates, read upon application initiation and saved upon destruction.

To register views you can use:

- `@View` - should annotate a class that manages a single view. By using this annotation, the class becomes a view controller - it can either implement `ViewController` interface for full view control or be wrapped by the default controller implementation. To gain some control over the view, classes annotated with @View can implement:
  - `ActionContainer`: all controller's methods (that consume no arguments or a single argument - an object in class hierarchy of calling actor; see LML documentation) will be available in the LML views with `$` prefix (or no prefix if attribute expects an action, like onClick).
  - `ViewRenderer`: takes control over view rendering. Gains access to the stage. Default implementation calls stage acting and drawing.
  - `ViewResizer`: takes control over view resizing. Gains access to the stage. Default implementation updates stage's viewport.
  - `ViewInitializer`: allows to invoke extra actions while initiating and destroying the view. Initializing takes place after parsing LML template and initiating stage.
  - `ViewPauser`: specifies actions on pausing and resuming. No action is taken by default.
  - Note - default renderers, resizers (and so on) can be specified globally with `InterfaceService` static variables.
- `@ViewDialog` - a "simplified" view. Instead of managing a whole screen, this becomes a controller of a single dialog. Registered dialogs will be available through show:dialogId action in LML templates. If ID is not specified in annotation, class' simple name becomes the ID. Dialog controllers can also inject their actors and currently used stage or implement ActionContainer interface to make their actions available in templates, just like view controllers. This allows to globally register a dialog showing method, that uses LML templates to show the window and does not even require a Java method connected to the widget that triggers dialog. View controllers can also implement ViewDialogShower for additional utility.
- `@ViewActionContainer` - allows to register view actions globally. Should be used for common actions to avoid direct action container registration in LML parser stored in InterfaceService (which might be done, but is more verbose). Annotated class has to implement ActionContainer or ActorConsumer interface. Amount of the views that can access the actions can be limited by specifying IDs of the views in the annotation.

In views you can also use these utility annotations:

- `@Asset` - allows to load and inject assets with AssetService. Can annotate a field of loaded asset's type or a wrapper - Lazy container or LibGDX collection (Array, ObjectSet, ObjectMap). If a wrapper is used, type() has to be specified. The most important setting that determines the way injection works is `loadOnDemand`:
  - when true: asset is loaded as soon as the component is constructed and injected into the field. There is no possibility of a fully constructed object to have null variable's value with this setting, but it does make application start-up slower and does not allow to load assets asynchronously with the loading screen - this setting should be used only for crucial assets that has to be loaded before showing the first screen or for delayed asset loading (see note below).
  - when false: asset loading is scheduled and injected when the loading is complete. Fully constructed objects' fields can be null with this setting, this might be problematic for initial screen. Note that either `AssetService.update()` (returning true) or `AssetService.finishLoading()` has to be called for injection to take place - you can `@Inject AssetService` variable and update it on the initial screens (or, for example, transition dialogs, if you plan on using delayed asset loading).
- You can also wrap the @Asset annotated field with a `Lazy<AssetClass>` variable. This changes injection behavior according to loadOnDemand setting:
  - when true: asset is scheduled and immediately loaded on the first Lazy.get() call. This is useful for light, rarely used assets.
  - when false: asset is scheduled for loading when the component is being processed and is injected into the lazy field as soon as loading is done. This is similar to non-wrapped lazy variable with loadOnDemand set to false with one significant difference - if Lazy.get() is called and asset is not loaded, it will be loaded synchronously, never returning null. This is a safer variant (especially on initial screens or views with delayed asset loading) at a cost of a small overhead.
- Note that assets injection are processed when the components are constructed, so you can delay asset loading by storing asset references in classes annotated with `@Component(lazy = true)` and injecting these components into `Lazy<ComponentClass>` fields. If you want to store a collection of assets in a lazy wrapper (it is supported), you have to specify lazyCollection type. For example, `Lazy<Array<Texture>>` would set `type=Texture.class` and `lazyCollection=Array.class`. `Array`, `ObjectSet` and `ObjectMap` are supported.
- `@SkinAsset` - after application's Skin is fully loaded (as in: contains data specified with @Skin annotation), all fields annotated with @SkinAssets will have specific objects from the skin injected as their current values. Class of the annotated field has to match the Skin's mapped object class to be properly injected. You can specify asset key (String) in the Skin with value() annotation parameter - if you won't, it will default to "default".
- `@Inject` - while not unique to Autumn MVC (this is actually a standard Autumn annotation), this is one of the annotations you will use the most. It allows to you inject any component - be it classes annotated with @Component or @View (among others), standard Autumn services, meta annotation processors or even the ContextContainer itself. See [Autumn](../autumn) docs for more info and more useful annotations.
- `@Dispose` - again, this is Autumn annotation that allows to automate the disposal of heavy objects. Basically, when the annotated object is removed from context (which usually takes place when the application is being closed), it will be automatically disposed of. This does not have to annotate injected assets, as AssetService already takes care of asset disposing.
- `@ViewStage` - when used in @View or @ViewDialog, injects current Stage object into the field. MIGHT be null or TURN null, as screens are sometimes reloaded and stages references might be cleared.
- `@LmlActor`, `@LmlAction` - injects actors from LML templates; allows to invoke actions in LML templates. See LML docs.

These are the services that you might want or have to inject from time to time:

- `InterfaceService` - manages screen transitions, contains LML parser. Contains mutable static fields with some defaults that are used when constructing views (and making separate annotations for these settings seemed like an overkill).
- `SkinService` - allows to directly access created `Skin` objects. 
- `LocaleService` - manages I18N.
- `AssetService` - manages an AssetManager, loads, provides and injects assets.
- `MusicService` - manages sounds and sound preferences. This service also registers LML actions that allow to easily modify sound settings: toggleSound and toggleMusic turns sounds/music on and off and setSoundVolume/setMusicVolume can be attached to a Scene2D Slider to modify current volume. You can get current sound settings with `musicOn, soundOn, getMusicVolume, getSoundVolume` in LML templates; default actions names can be changed by setting MusicService static variables (with higher priority than `@Initiate(priority=0)`).

All classes (and annotations) have nearly full javadocs of public API, so everything should be clear. If it isn't, don't hesitate to ask.

## Examples
See [example project](../examples/gdx-autumn-mvc-simple). [Autumn tests project](../examples/gdx-autumn-tests) might also be helpful to get started with dependency injection.

## Contributions
Automatic component scan on iOS is not implemented and it might take me some time before I finally force myself to do it, if ever. If someone already implemented this functionality and is willing to share, I won't mind integrating it into Autumn.

Your opinions, comments and testing can help as well. Don't be afraid to inform me about bugs and functionalities that are missing or the ones you are not a huge fan of.

## Dependencies
Gradle:

```
        compile "com.github.czyzby:gdx-autumn-mvc:$libVersion.$gdxVersion"
```
`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).

When used on GWT, make sure to inherit Autumn module:
```
        <inherits name='com.github.czyzby.autumn.mvc.GdxAutumnMvc' />
```
Check out [gdx-autumn](../autumn) for platform-specific class scanners. [Sample app](../examples/gdx-autumn-mvc-simple) might help you with the initial set up.

## What's new
Make sure to check `gdx-autumn` and `gdx-lml` changes as well - both of these projects are heavily used by the `gdx-autumn-mvc`.

1.6 -> 1.7:

- `StandardViewResizer` now correctly detects how the passed viewport should be updated. `StandardCameraCenteringViewResizer` is now deprecated.
- It is now possible to access `Skin` instances internally stored by a `SkinService`.

1.5 -> 1.6:

- `AssetService` API extended with: `getTotalLoadingProgress`, `getAssetManager`, `getEagerAssetManager`, `registerLoader`.

1.4 -> 1.5:

- Added `getPreference` methods to `PreferencesService`. Now you can also access preferences "manually" instead of injecting them.
- Added `setCreateMissingDependencies` to `AutumnApplication`. Now you can easily turn off injection of not annotated classes without providers.

1.3 -> 1.4:

- Views are now additionally resized before showing, which fixes bugs with some viewports where not every screen was resized properly.
- `@Property` was added along with `PreferencesService`. Now you can add your custom, fully managed preferences. See docs for more info.
- Added additional controller removal methods to `InterfaceService`: `remove`, `removeDialog`, `destroyDialog`.

### Archive
Older change logs are available in `CHANGES.md` file.
