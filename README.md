#LibGDX Autumn MVC
Totally not a Spring rip-off (TM).

If you're used to Spring MVC, you may feel like home. Dependency injection, component scan, view templates and a lot of internals handled for you. The goal of Autumn is to let you focus on game's logic rather than the overall project structure: you define the assets, views and their controllers - Autumn MVC makes it easy to manage them with just a few lines of code and a whole lot of annotations.

The original idea was to make my first LibGDX utilities public, but after I realized how much effort it takes to actually set up all of my managers, I decided that I *need* to make it simpler to use for everyone - including myself. This is the result.

Autumn MVC tries to be as flexible as possible, but it does force a specific project structure and approach. If you want only some parts of the "framework", that's completely fine: [Kiwi](https://github.com/czyzby/gdx-kiwi) is a set of Guava-inspired utilities for general use (in LibGDX applications), [LML](https://github.com/czyzby/gdx-lml) is a pretty powerful HTML-like markup language that allows to easily build complex Scene2D UIs, and [Autumn](https://github.com/czyzby/gdx-autumn) provides annotation-processing mechanism that allows for dependency injection with component scan out of the box. You can use each and every of them in any combination (knowing that both LML and Autumn use Kiwi internally and LML uses Autumn for GWT method reflection, that is). However, excluding some of Autumn MVC components is usually not an option, as most of them depend on each other.

Note that currently this is still a WORK IN PROGRESS, I'm in the middle of doing final tweaks, finishing documentation and writing a simple, example project. After that, this lib will be available through Maven Central.

##Why should you use Autumn MVC
Simply put - to save your time. While a pure Java application without reflection might start or even work slightly faster, it requires you to handle a lot of stuff... stuff that usually LibGDX makes pretty easy to implement, to be honest. On the other hand, some of this _stuff_ is awkward to use or not fully supported out of the box. Autumn support goes a step further in - hopefully - the right direction. Autumn takes care of:

- asset management,
- i18n,
- preferences,
- UI building complexity,
- objects initiation,
- objects' dependencies,
- heavy object disposing,
- event posting,
- screen transitions,
- dialog management,
- music themes.

Normally, you would have to implement some kind of system that manages screena, internationalization, asset management, overall application initiation, and possibly a UI builder too. With Autumn MVC, all you care about is creating a configuration file with basic assets paths (bundles, skin) and a few classes annotated with `@View` that reference LML files.

### But I don't like LML...
I can understand that not everyone might be a fan of HTML-like syntax and tedious refactoring. Personally, I find UIs created in Java less readable and too verbose, but if for some reason that's the way you want to go, make your `@View` implement `ViewController` and you will have full control over the screen, without losing the asset management, component injection and so on. There's even an abstract base for views without LML: `AbstractViewController`. However, screen transition mechanism relies on actions, so dropping Scene2D is a no-go.

## How to start
Read on [LML](https://github.com/czyzby/gdx-lml) to see how the views are created. Check out [Autumn](https://github.com/czyzby/gdx-autumn) to find out more about the components system. You may also want to know a thing or two about [Kiwi](https://github.com/czyzby/gdx-kiwi), as these utilities might make your programming a bit easier.

### Depedencies
Coming soon. Snapshots are available for the impatient.

### Application
Instead of implementing `ApplicationListener` or extending `ApplicationAdapter`, extend `AutumnApplication` - or even use pass it to application initiation methods without extending, this is not an abstract class. Initiating this object requires you to pass a root scanning class (which will usually be the class in the bottom of your package hierarchy) and a class scanner, which is (usually) platform-specific. You also have to register your components' classes/packages for custom Autumn GWT reflection - see [Autumn](https://github.com/czyzby/gdx-autumn) documentation.

After that, you might want to create a single `@Configuration` class that will be initiated and destroyed when the context is being built. By annotating its fields, you can choose skin, i18n bundles, preferences (and so on) that will be used by LML parser. Common configurations:

- @I18nBundle - should annotate a string with a path the I18nBundle or a FileHandle pointing to a bundle. Can optionally set bundle ID - multiple bundles can be used in views: specific bundles can be referenced with @bundleId.bundleKey.
- @I18nLocale - should annotate a string with a property name (and specify properties path) or a default Locale object (not advised). Allows to set initial locale and - if properties are used - save locale in user's preferences. (Done automatically when changing locale with LocaleService.)
- @Skin - should annotate string with a path to the skin. If skin atlas stores bitmap fonts, they can be referenced with annotation parameters and loaded from the same atlas. This setting is necessary for `InterfaceService` to work: Autumn MVC application cannot be initiated without a skin.
- @Preference - should annotate a string with a path to application preferences. Currently it is used solely to pass the preferences object to the LML parser, so that the preferences could be referenced with # operator.
- @SoundVolume, @MusicVolume - should annotate either a float with initial sounds or music volumes (not advised), or a string with the preference name where sound volume should be stored. All music properties should be in one preferences. Allows to save and restore sound settings.
- @SoundEnabled, @MusicEnabled - should annotate either a boolean with initial sounds or music states (not advised), or a string with the preference name where music state should be stored. Passed preferences should be the same ones that were used for volumes.
- @StageViewport - can annotate a ObjectProvider<Stage> (see Kiwi lazy variables), that provides viewports used upon stage creation. Default viewport type is the ScreenViewport, as it works arguably well for interfaces and does not require additional settings (like virtual screen size).

To register views you can use:

- @View - should annotate a class that manages a single view. By using this annotation, the class becomes a view controller - it can either implement `ViewController` interface for full view control or be wrapped by the default controller implementation. To gain some control over the view, classes annotated with @View can implement:
  - ActionContainer: all controller's methods (that consume no arguments or a single argument - an object in class hierarchy of calling actor; see LML documentation) will be available in the LML views with & prefix or no prefix if referenced in attribute that expects an action, like onClick.
  - ViewRenderer: takes control over view rendering. Gains access to the stage. Default implementation calls stage acting and drawing.
  - ViewResizer: takes control over view resizing. Gains access to the stage. Default implementation updates stage's viewport.
  - ViewInitializer: allows to invoke extra actions while initiating and destroying the view. Initializing takes place after parsing LML template and initiating stage.
  - ViewPauser: specifies actions on pausing and resuming. No action is taken by default.
  - Note - default renderers, resizers (and so on) can be specified globally with `InterfaceService` static variables.
- TODO: @ViewDialog, @ViewActionContainer

In views you can also use these utility annotations:

- @Asset - allows to load and inject assets with AssetService. The most important setting that determines the way injection works is `loadOnDemand`:
  - when true: asset is loaded as soon as the component is constructed and injected into the field. There is no possibility of a fully constructed object to have null variable's value with this setting, but it does make application start-up slower and does not allow to load assets asynchronously with the loading screen - this setting should be used only for crucial assets that has to be loaded before showing the first screen or for delayed asset loading (see note below).
  - when false: asset loading is scheduled and injected when the loading is complete. Fully constructed objects' fields can be null with this setting, this might be problematic for initial screen. Note that either AssetService.update() (returning true) or AssetService.finishLoading() has to be called for injection to take place - you can @Inject AssetService variable and update it on the initial screens (or, for example, transition dialogs, if you plan on using delayed asset loading).
- You can also wrap the @Asset annotated field with a Lazy<AssetClass> variable. This changes injection behavior according to loadOnDemand setting:
  - when true: asset is scheduled and immediately loaded on the first Lazy.get() call. This is useful for light, rarely used assets.
  - when false: asset is scheduled for loading when the component is being processed and is injected into the lazy field as soon as loading is done. This is similar to non-wrapped lazy variable with loadOnDemand set to false with one significant difference - if Lazy.get() is called and asset is not loaded, it will be loaded synchronously, never returning null. This is a safer variant (especially on initial screens or views with delayed asset loading) at a cost of a small overhead.
Note that assets injection are processed when the components are constructed, so you can delay asset loading by storing asset references in classes annotated with `@Component(lazy = true)` and injecting these components into Lazy<ComponentClass> fields.
- TODO: @Inject, @ViewStage, @ViewActor

## Example project
Coming soon.

## Contributions
Automatic component scan on Android and iOS is not implemented and it might take me some time before I finally force myself to do it. It will probably be in a separate library that depends on Autumn, so no changes in MVC itself are required. If someone already implemented this functionality and is willing to share, I won't mind integrating it into Autumn.

Your opinions, comments and testing can help as well. Don't hesitate to inform me about bugs and functionalities that are missing or the ones you are not a huge fan of.