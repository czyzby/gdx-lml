Newest change logs are in the `README.md` file.

# Autumn 1.X

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

# Autumn 0.X

0.5 -> 0.6:

- Since LibGDX introduced method annotations in reflection API, custom reflection wrappers (and separate GWT reflection pool, on which I spent quite some time) are no longer needed. Whole reflection API was removed and "native" LibGDX solutions are now used. Sorry for breaking your code - before LibGDX reflection expansion most of Autumn wouldn't be possible to implement without custom reflection.
- Components are no longer mapped by their interfaces as LibGDX reflection API does not provide method that returns implemented interfaces of a class. You can still inject components by their abstract classes (as long as there is only one concrete implementation of the abstract in the context).
- Now only absolutely necessary classes are registered for GWT reflection - less meta-data in JS.

0.4 -> 0.5:

- `OnEvent`- and `OnMessage`-annotated method can now decide whether their listeners should be kept or removed after method invocation at runtime by returning a boolean. *true* stands for removing after invocation, *false* - keeps the listeners for the next upcoming events/messages (just like in the annotation parameter: `removeAfterInvocation`). These values can be accessed statically for code clarity - see `EventProcessor` and `MessageProcessor` static fields.
- Partial injection is implemented for `@MetaComponent`s - as they are usually used only internally, this should be enough. Basically, regular injection is processed after all components are initiated, so there is no possibility of a scanned component to be unavailable for injection - meta components are processed before all others (to allow scanning for new, just registered component types) and they can inject only instances of classes that were already processed... which usually means only other metas. You can, however, inject lazy fields without a problem, as long as you don't call them before they are actually registered in the context. New instance injection is not possible. Meta components' lazy injections, for various reasons, do not trigger full object initiations, so lazy injection of lazy components to metas might be dangerous.
- Fixed GWT dependency bug, now correct modules are inherited.
- Fixed field and method annotations processing, now it should be a little faster and never throw weird LibGDX ObjectMap's errors (happened while registering a lot of custom processors while processing components with nested injections... the strange thing is - it was thrown only like 20% of time and never in debug. I think there might be something wrong with ObjectMap iterators, but oh-well). Now wrapped reflected fields and methods provide a getter for an array containing all present annotations.