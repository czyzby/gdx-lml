#LibGDX Autumn
Dependency injection mechanism with component scan for LibGDX applications.

Actually, this library is the core of Autumn MVC, extracted as a separate library for those who want to use the functionality without the overhead of additional components and forced application structure.

##Basics
Get used to annotations. So-called stereotype annotations on classes allow the context manager to find your application components among other classes and store them in the context, so they can be used (and injected) later. Method annotations can cause the selected methods to be invoked under met conditions with parameters taken directly from the context. Field annotations are used to inject objects from context or invoke specific actions (like automatic destruction of disposable objects).

Everything comes down to a few annotations and classes that might make your life much easier as soon as you get used to the structure.

### Getting started

Autumn's main goal is to let you quickly set up your application. It does that by creating a "context" in which every of your registered classes is a "component". To create a new context, you need an instance of `ContextInitializer` (you can get that with convenience factory methods: `Context.builder()` or `ContextInitializer.newContext`). After getting an initializer, you should:

- Register your components (`addComponent`, `addComponents`). Autumn aims to remove context meta-data after initiation, which means that no references are kept to uninjected components and they are hopefully garbage-collected. This basically means that if you don't provide your own components that you want to keep, the context will be constructed and... immediately destroyed. Don't worry though - usually you need only one component (which will inject others, preserving their references) and all you need to do is invoke its constructor; its annotated fields and methods will still be processed.
- Register your custom annotation processors (`addProcessor`, `addProcessors`) and custom annotations to scan for (`scanFor`). Processors are actually scanned for and initiated by the context, but you can also register them manually. You can skip this step if you don't need any extra annotation processed.
- Add scanners (`scan`). Provide a scanning root (package to be scanned) and a platform-specific scanner.
- Set up specific properties, if you need to (`createMissingDependencies`, `maxInitiationIterationsAmount`, `clearProcessors`).
- Initiate context (`initiate`). Now your added packages will be scanned for annotated classes, instances of these classes will be created using reflection (with constructor dependency injection) and their fields and methods will be processed. `ContextDestroyer` instance will be returned - it is a `Disposable` object that invokes destruction methods, if you registered any.

###Scanners
As you might guess, they are used to scan for annotated classes. Each platform requires a custom scanner, unfortunately. Available implementations:

- **FixedClassScanner** - well, it *can* be used on any platform, but you have to manually select the classes it has access to, so it basically removes the concept of true component scan.
- **FallbackDesktopClassScanner** - scans binary classes (run from IDE) or jars in the class loader base location (run from a jar). No external dependencies, but it relies on reflection and class loading to scan packages. Use when necessary.
- **DesktopClassScanner** - uses `fast-classpath-scanner` library for non-reflection-based, efficient component scanning. Does not load tested classes. Works only on desktop. Available in `gdx-autumn-fcs` library.
- **GwtClassScanner** - scans through all classes registered for GWT LibGDX reflection pool. Available in `gdx-autumn-gwt` library.

Unfortunately, class scanners for Android and iOS are not implemented yet. I didn't have much time to do it yet (nor do I normally target these platforms); I do have an untested Android component scanner implementation, if anyone is interested. But, for now, use *FixedClassScanner*. Sorry.

### Annotations

By default, Autumn supports:

- `@Component`: default annotation that marks the class to be scanned for.
- `@Processor`: marks the class as annotation processor. Processors are initiated before other components and they are used to process annotated classes, fields and methods.
- `@Provider`: turns the class into an object provider. If a requested dependency class is not a component in the context, providers can be used to get its instance - not necessarily using reflection. Like processors, providers are initiated before the rest of components, as they are often used to resolve dependencies.
- `@Inject`: allows to inject field values. If field type matches a superclass of a component (and there is only ONE component mapped to this type - otherwise context doesn't know which one to inject), it will set field's value with the component instance. Otherwise it uses a provider for this type or creates a new instance of the object using no-arg constructor.
- `@Initiate`, `@Destroy`: annotate methods. After components' fields are processed, `@Initiate`-annotated methods are invoked to finish component initiation. `@Destroy` methods are invoked by `ContextDestroyer#dispose()`. Both of these annotation contain a `priority` field which is honored among all components, allowing you to fully control the flow of initiation. These methods have their arguments injected from context (components, providers, new no-arg instances).
- `@Dispose`: marks that the field or Disposable-implementing type should be disposed on `ContextDestroyer#dispose()`.
- `@OnEvent`: turns the type or method into a listener of events of selected type. Each time the selected event object is posted on `EventDispatcher` (injectable component), the method is invoked with parameters injected from context. If event is one of the parameters, it will be also injected, effectively allowing to pass messages between components using this mechanism. Does not necessarily depend on reflection: if the annotated type implements `EventListener`, it will be invoked like any other POJO. `@OnEvent`-annotated classes are regular components that will be fully initiated, with their fields and methods processed.
- `@OnMessage`: simplified event listener. Instead of a event class, these methods and types are listening to a specifing string message which can be posted on `MessageDispatcher`. Since they cannot pass actual messages between components, these are usually used to notify about some kind of typical event that have occurred (like "the assets are loaded" or "we've connected with the server). Again, these do not have to rely on reflection.

See annotations' docs for more informations.

##Downsides
Autumn makes heavy use of reflection. While it doesn't rely on direct calls to class names that might get refactored, and while most of reflection-based invocations are made on start-up, Autumn application can be still somewhat slower than regular ones. (Not noticeably, I hope.) It's sensible to keep all reflected and annotated classes in one package tree to limit the initial scan for components.

##Dependency
Gradle dependency:

```
    compile "com.github.czyzby:gdx-autumn:1.2.$gdxVersion"
```
Currently supported LibGDX version is **1.7.1**.

To include Autumn in GWT, see [Autumn GWT](http://github.com/czyzby/gdx-autumn-gwt).
For efficient class scanning on desktop, see [Autumn FCS](http://github.com/czyzby/gdx-autumn-fcs).

##What's new

1.1 -> 1.2:

- Message and event listener exceptions are additionally logged, as LibGDX applications seem to ignore some exceptions that are thrown when posting runnables.

0 -> 1:

Autumn was rewritten from scratch, so many annotations changed their original methods and packages. Spring-inspired context, available through the whole life of application, turned out to be an overkill for games. After working on a few projects with Autumn MVC, I have never had the need to actually access context object and create any objects at runtime: usually initial dependency injection and component creation was more than enough. Not to mention that context meta-data was a lot of extra overhead for something as simple as initiating an application.

Autumn 1 has a different approach to context: you can keep context meta-data if you want to access certain classes by their type or initiate objects at runtime, but it is cleared after initiation by default. This is basically how Autumn 1 works:

- Scanning for meta-components.
- Creating meta-components: annotation processors and dependency providers. Resolving constructor dependencies (unless circular, of course). Processing their fields and methods with existing processors (they can even process... themselves).
- Scanning for regular components. After this, usually scanners are cleared.
- Creating regular components. Resolving constructor dependencies.
- Processing types annotations. Registering `@OnEvent` and `@OnMessage` listeners.
- Processing fields. This is where main dependency injection happens thanks to `@Inject`. Scheduling disposal of `@Dispose`-annotated fields.
- Processing methods. Registering `@OnEvent` and `@OnMessage` method listeners. Invoking `@Initiate` methods and scheduling `@Destroy` methods.
- By default: clearing processors and context. Context is now unusable, initializer cannot process any other components. All unnecessary, initiation-only components are garbage collected - same goes for context meta-data overhead. Only injected dependencies with references from objects that you kept are truly available.

This allows you to quickly initiate your application thanks to automatic component scanning and dependency injection through reflection, but as soon as the thing is ready, all extra data is cleared and the whole thing runs as plain-old-Java (unless you WANT to use reflection, of course). There is support for keeping the context running after initiation, but for most applications this isn't required or desired.

Changes:

- Package refactor. Annotations were moved, old processors got removed or refactored.
- Added support for constructor dependencies. Unless dependencies are circular, you can use a single public constructor with any arguments that will be provided by the context.
- `@Dispose` annotation can now be used on classes as well as on fields. Annotated classes have to implement `Disposable` interface.
- `@OnEvent` and `@OnMessage` can now annotate classes as well as fields. Classes have to implement `EventListener` or `MessageListener` interfaces. `@OnEvent`- and `@OnMessage`-annotated classes are treated as components and fully initiated. Actually, since these listeners do not rely on reflection at runtime and can benefit from dependency injection as well as any other components, this is the advised way to register event and message listeners. At least those that are used very often.
- `@Component` annotation got simplified. There are no lazy components (since context is destroyed after initiation) and removal after initiation option got removed, as this is the default behavior anyway: if you don't keep the reference to the component during initiation, it gets garbage-collected, just like `@Component(keepInContext=false)` did before.
- `MessageProcessor` and `EventProcessor` got renamed to `MessageDispatcher` and `EventDispatcher`.
- Previous `DesktopClassScanner` was renamed to `FallbackDesktopClassScanner`. Now default class scanner is in `gdx-autumn-fcs` library and is much faster than the fallback implementation (since it does not rely on reflection and does not load the scanned classes).
- `AutumnRuntimeException` removed. Instead, `ContextInitiationException` is thrown if unable to initiate context due to common context errors; `GdxRuntimeException` is thrown if unable to invoke listeners, extract value from context, caught reflection exception or for generally unexpected errors.
- `@Provider` added. Before, only components could be injected. Now, any type of requested object can be provided by customized providers. If `@Provider` annotates `DependencyProvider` implementation, it will use it without reflection to provide a certain type of objects. If `@Provider` annotates another object, ALL its non-void-returning methods will be turned into providers, with their arguments supplied from the context. Providers are considered meta-components, are they are often used for dependencies of regular components, including their constructors. While they are normally processed (as in: their fields will be injected, methods invoked, etc.), they are initiated BEFORE other components. Do not inject dependencies to providers unless they are other meta-components or you are 100% sure that their instances will be available in the context (for example: you can provide them manually).
- `ContextInitializer` is builder-like object with chainable methods that should be used to initiate context in Autumn 1. Context container is now `Context`, but it generally should be accessed only by annotation processors and only during context initiation. It is injectable, but by default, it will be cleared after just full context initiation.
- Annotation processors refactored, as they were a mess. A much more powerful interface and abstract implementation are now provided: `AnnotationProcessor` and `AbstractAnnotationProcessor`. Scanning for annotated components now requires invoking `scanFor` in `ContextInitializer` instead of creating a new processor. If you do need to handle a new functionality, extend `AbstractAnnotationProcessor` and register it in the initializer or just annotate it as `@Processor` and let the initializer do the work.
- `@MetaComponent` removed. Use `@Processor` and `@Provider` instead.