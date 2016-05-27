# LibGDX Autumn
Dependency injection mechanism with component scan for LibGDX applications.

Actually, this library is the core of [Autumn MVC](../mvc), extracted as a separate library for those who want to use the functionality without the overhead of additional components and forced application structure.

## Basics
Get used to annotations. So-called stereotype annotations on classes allow the context manager to find your application components among other classes and store them in the context, so they can be used (and injected) later. Method annotations can cause the selected methods to be invoked under met conditions with parameters taken directly from the context. Field annotations are used to inject objects from context or invoke specific actions (like automatic destruction of disposable objects).

Everything comes down to a few annotations and classes that might make your life much easier as soon as you get used to the structure.

### Getting started
See [tests project](../examples/gdx-autumn-tests) for usage examples of huge part of Autumn API, including custom annotation processor.

Autumn's main goal is to let you quickly set up your application. It does that by creating a "context" in which every of your registered classes is a "component". To create a new context, you need an instance of `ContextInitializer` (you can get that with convenience factory methods: `Context.builder()` or `ContextInitializer.newContext`). After getting an initializer, you should:

- Register your components (`addComponent`, `addComponents`). Autumn aims to remove context meta-data after initiation, which means that no references are kept to uninjected components and they are hopefully garbage-collected. This basically means that if you don't provide your own components that you want to keep, the context will be constructed and... immediately destroyed. Don't worry though - usually you need only one component (which will inject others, preserving their references) and all you need to do is invoke its constructor; its annotated fields and methods will still be processed.
- Register your custom annotation processors (`addProcessor`, `addProcessors`) and custom annotations to scan for (`scanFor`). Processors are actually scanned for and initiated by the context, but you can also register them manually. You can skip this step if you don't need any extra annotation processed.
- Add scanners (`scan`). Provide a scanning root (package to be scanned) and a platform-specific scanner.
- Set up specific properties, if you need to (`createMissingDependencies`, `maxInitiationIterationsAmount`, `clearProcessors`).
- Initiate context (`initiate`). Now your added packages will be scanned for annotated classes, instances of these classes will be created using reflection (with constructor dependency injection) and their fields and methods will be processed. `ContextDestroyer` instance will be returned - it is a `Disposable` object that invokes destruction methods, if you registered any.

### Scanners
As you might guess, they are used to scan for annotated classes. Each platform requires a custom scanner, unfortunately. Available implementations:

- **FixedClassScanner** - well, it *can* be used on any platform, but you have to manually select the classes it has access to, so it basically removes the concept of true component scan.
- **FallbackDesktopClassScanner** - scans binary classes (run from IDE) or jars in the class loader base location (run from a jar). No external dependencies, but it relies on reflection and class loading to scan packages. Use when necessary.
- **DesktopClassScanner** - uses `fast-classpath-scanner` library for non-reflection-based, efficient component scanning. Does not load tested classes. Works only on desktop. Available in `gdx-autumn-fcs` [library](natives/fcs).
- **GwtClassScanner** - scans through all classes registered for GWT LibGDX reflection pool. Available in `gdx-autumn-gwt` [library](natives/gwt).
- **AndroidClassScanner** - uses Android Java API to scan through all available classes. Available in `gdx-autumn-android` [library](natives/android).
- **JTranscClassScanner** - uses `JTranscReflection` to access names of all generated classes. Available in `gdx-autumn-jtransc` [library](natives/jtransc).

Unfortunately, class scanner iOS is not implemented yet. For now, you might try to make your own implementation (based on Android scanner?) or use *FixedClassScanner*. Sorry.

### Annotations
By default, Autumn supports:

- `@Component`: default annotation that marks the class to be scanned for.
- `@Processor`: marks the class as annotation processor. Processors are initiated before other components and they are used to process annotated classes, fields and methods.
- `@Provider`: turns the class into an object provider. If a requested dependency class is not a component in the context, providers can be used to get its instance - not necessarily using reflection. Like processors, providers are initiated before the rest of components, as they are often used to resolve dependencies.
- `@Inject`: allows to inject field values. If field type matches a superclass of a component (and there is only ONE component mapped to this type - otherwise context doesn't know which one to inject), it will set field's value with the component instance. Otherwise it uses a provider for this type or creates a new instance of the object using no-arg constructor.
- `@Initiate`, `@Destroy`: method annotations. After components' fields are processed, `@Initiate`-annotated methods are invoked to finish component initiation. `@Destroy` methods are invoked by `ContextDestroyer#dispose()`. Both of these annotation contain a `priority` field which is honored among all components, allowing you to fully control the flow of initiation. These methods have their arguments injected from context (there's support for injecting components, objects received from providers and new instances created by no-arg constructor).
- `@Dispose`: marks that the field or Disposable-implementing type should be disposed on `ContextDestroyer#dispose()`.
- `@OnEvent`: turns the type or method into a listener of events of selected type. Each time the selected event object is posted on `EventDispatcher` (injectable component), the method is invoked with parameters injected from context. If event is one of the parameters, it will be also injected, effectively allowing to pass messages between components using this mechanism. Does not necessarily depend on reflection: if the annotated type implements `EventListener`, it will be invoked like any other POJO. `@OnEvent`-annotated classes are regular components that will be fully initiated, with their fields and methods processed.
- `@OnMessage`: simplified event listener. Instead of a event class, these methods and types are listening to a specifing string message which can be posted on `MessageDispatcher`. Since they cannot pass actual messages between components, these are usually used to notify about some kind of typical event that have occurred (like "the assets are loaded" or "we've connected with the server). Again, these do not have to rely on reflection.

See annotations' docs for more informations.

## Downsides
Autumn makes heavy use of reflection. While it doesn't rely on direct calls to class names that might get refactored, and while most of reflection-based invocations are made on start-up, Autumn application can be still somewhat slower than regular ones. (Not noticeably, I hope.) It's sensible to keep all reflected and annotated classes in one package tree to limit the initial scan for components.

## Dependencies
Gradle dependency:
```
        compile "com.github.czyzby:gdx-autumn:$libVersion.$gdxVersion"
```
`$libVersion` is the current version of the library, usually following `MAJOR.MINOR` schema. `$gdxVersion` is the LibGDX version used to build (and required by) the library. You can check the current library version [here](http://search.maven.org/#search|ga|1|g%3A%22com.github.czyzby%22) - or you can use the [snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/czyzby/).

To include Autumn in GWT, see [Autumn GWT](natives/gwt).
To include Autumn in Android applications, see [Autumn Android](natives/android).
To include Autumn in JTransc application, see [Autumn JTransc](natives/jtransc).
For efficient class scanning on desktop, see [Autumn FCS](natives/fcs).

##What's new

1.6 -> 1.7

- Experimental [JTransc](https://github.com/jtransc/gdx-backend-jtransc) support through [a new library](natives/jtransc).

1.5 -> 1.6

- Fixed `@Dispose` annotation behavior.
- Added [`gdx-autumn-tests` project](../examples/gdx-autumn-tests), using a considerate part of Autumn API. Make sure to check it out.

1.3 -> 1.5

- Exceptions thrown during annotation checks are now logged. This will basically affect only GWT platform - exceptions were ignored there due to clumsy reflection mechanism, but it turns out that some of them are actually relevant from time to time. If your application fails to initiate a component, make sure that all of its extended classes (and implemented interfaces) are reflected. If not, try to include them.
- Added `AutumnRoot` class in root Autumn package for class scanners utility.

1.2 -> 1.3

- Annotated methods (`@Initiate`, `@Destroy`) no longer keep component references for invocation if they are static. If your static `@Destroy` method is annotated, you can safely assume that the component will still be properly garbage collected after context initiation, as long as you didn't keep its reference anywhere.
- Android class scanner in [Autumn Android](natives/android).

### Archive
Older change logs are available in `CHANGES.md` file.
