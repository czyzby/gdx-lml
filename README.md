#LibGDX Autumn
Dependency injection mechanism for LibGDX applications.

Actually, this library is the core of Autumn MVC for those who want to use the functionality without the overhead of additional components and forced application structure.

##Basics
Get used to annotations. So-called stereotype annotations on classes allow the context manager to find your application components among other classes and store them in the context, so they can be used (and injected) later. Method annotations can cause the selected methods to be invoked under met conditions with parameters taken directly from the context. Field annotations are used to inject objects from context or invoke specific actions (like automatic destruction of disposable objects).

Everything comes down to a few annotations and classes that might make your life much easier as soon as you get used to the structure.

###Stereotypes

- **@Component**: basic component annotation. The stereotype you'll probably use vary often. Components are scanned for, constructed (see field and method annotations) and managed by the context. Components *have to* have a public, no-argument constructor and cannot be abstract or interfaces in order to work on all platforms.
  * *lazy* - if component is lazy, it will be initiated on the first request (field injection, requested method parameter, manual retrieval from context) rather than on context initiation. Defaults to false.
  * *keepInContext* - if false, component will be destroyed right after initiation. Useful for setting components. Such components cannot be extracted from the context and should not be injected. Default to true.

- **@Configuration**: a specialized Component which is not kept in context. Should be used for settings, configurations etc.

- **@MetaComponent**: should be used to annotate custom processors. Meta components are scanned for before regular components, allowing to register custom stereotypes that will be scanned for like any others. Note that processors can be injected and retrieved like any other component, but they are not processed like other components; their initiation, destruction and event listening methods are not called and injection is limited, as they are processed before other classes. Every meta component has to implement one of these classes:
  * *ComponentMetaAnnotationProcessor* - processes other meta components. Such processors generally should be registered before the proper, full scanning to ensure that all custom meta components are scanned. Mostly for internal use, custom meta components - hopefully - will not be needed.
  * *ComponentTypeAnnotationProcessor* - processes classes (components). Allows to register custom stereotypes. Note that multiple processors for the same annotation type are allowed and generally only one should tell the context to prepare a component instance. Of a component is annotated with multiple annotations that are processed and register new components in the context, it might lead to multiple component initiations - be careful.
  * *ComponentFieldAnnotationProcessor* - processes fields in components that are being initiated. 
  * *ComponentMethodAnnotationProcessor* - processes methods in components that are being initiated. Methods are processed after fields.

###Field annotations

- **@Inject**: again, this is one of the annotations you'll use the most. Allows to inject a component from the context to the variable upon object initiation. Injected fields can - and should - be private (or even final, equal to null; although it isn't advised since I'm not sure if it will always work on GWT or other platforms). Circular references (A injects B, B injects A) are allowed.
  * *value* - class of the injected component. Context components are generally mapped by their whole class tree (every extended class), so this setting will be - hopefully - rarely used. It allows to resolve conflicts when you want or need the field's type to be a interface or abstract class that have multiple component implementations and the context doesn't know which one to inject. 
  * *lazy* - class of the component that should be injected wrapped in a Lazy container (see [Kiwi library](http://github.com/czyzby/gdx-kiwi) for Lazy docs). Allows to keep lazy components truly lazy, by injecting a wrapper that will ask the context to fully initiate the injected component on first use (first get() call). Otherwise, lazy components would be injected and initiated at once, losing their status.
  * *concurrentLazy* - if set to true and a *lazy* class is chosen, ConcurrentLazy will be injected instead of Lazy wrapper.
  * *newInstance* - OK, be careful with this one. It does not extract the component from the context, but instead it will ask the context to fully initiate an instance of the chosen component without actually mapping it in the context. The injected instance will be unique to the variable (unless passed to another object, of course). Note that while the instance is not mapped in context - so it cannot be extracted or injected in another component - it is still managed (as in initiated and destroyed) by the context, so this hopefully does not lead to memory leaks. This allows to have multiple instances of the same class injected to multiple components, without sharing resources. *lazy* setting is honored - *lazy* with  *newInstance* set to true will create an instance of the object with the context on the first get() call.

- **@Dispose**: a simple utility annotation that will dispose of the chosen disposable variable when the component is destroyed. Note that this also works on Lazy wrappers and Iterables (LibGDX Arrays or ObjectSets) that hold disposable variables. Does not work (yet?) with regular Java arrays, but these are usually awkward to store assets anyway, so they are not a priority.

###Methods annotations

- **@Initiate**: invoked after components' fields are fully processed. Methods with this annotation can accept any number of components (lazy or not) which will be taken from the context.
  * *priority* - number; allows to "sort" initiation method invocations. Priority is honored not only among methods in one component, but by *all* invocations in all components (that are initiated right now - not requested lazies will not be initiated on start-up) - this allows to truly manage the order of component initiations.

- **@Destroy**: similarly to **@Initiate**, this annotation allows to destroy selected components. Destroying methods are invoked when the context is being disposed or when a component is requested to be destroyed by the context. These methods can also have any of the components injected as parameters, but keep in mind that some of the components might be already destroyed. 
  * *priority* - order is honored among all currently destroyed objects.

- **@OnEvent**: basically, this annotation turns a method into an event listener. To post an event, get EventProcessor from context (can be injected like any other component) and use postEvent method - after the event is posted, every method that listens to the selected event type will be invoked. Method arguments can consist of context components (will be extracted from context, can be lazy) and event object - in any combinations. If the event class has a registered component in the context with the same type, posted event object takes priority as a method argument, even if multiple arguments of the event class are requested (the same object will be injected multiple times). Since an event can be basically any type of object, this allows to pass complex data to the listener methods as events' fields.
  * *value* - class of the listened event. Required.
  * *removeAfterInvocation* - if set to true, event listener will be removed after first invocation.
  * *forceMainThread* - if set to true, invocation will be posted on the main application thread using Gdx.app.postRunnable. Be careful with this one, as it might lead to weird concurrency bugs, especially if you need to remove the listener after invocation, but multiple events can occur almost instantly.
  * *strict* - if false, exceptions are expected and will be ignored. Defaults to true.

- **@OnMessage**: simplified event listener. Instead of event classes, selected methods will listen to a specific string message, that - once posted - will invoke the method with parameters extracted from context. This might be preferred for simple listeners that process much more events that do not have additional data to be invoked. To post a message, get MessageProcessor from context (can be injected) and use postMessage method.
  * *value* - class of the listened event. Required.
  * *removeAfterInvocation* - if set to true, event listener will be removed after first invocation.
  * *forceMainThread* - if set to true, invocation will be posted on the main application thread using Gdx.app.postRunnable. Be careful with this one, as it might lead to weird concurrency bugs, especially if you need to remove the listener after invocation, but multiple events can occur almost instantly.
  * *strict* - if false, exceptions are expected and will be ignored. Defaults to true.

##Context
**ContextContainer** is the class responsible for context management. It invokes component scanning (thanks to different ClassScanner implementations), uses annotations processors, and prepares, initiates, manages and destroys components. ContextContainer is itself a component, so it can be injected or passed in a annotated method. The most common methods you might need to use:

- *ContextContainer()* - creates a context object with default processors registered.
- *ContextContainer(Class, ClassScanner)* - creates a context object with default processors registered. Scans for components starting with the passed class package, using the passed scanner.
- *registerComponents(Class, ClassScanner)* - manual invocation of what the constructor above does internally. Allows to scan for multiple components in multiple packages with different scanners.
- *getFromContext(Class)* - retrieves component for the given class. Wakes up lazy components. If there are no or multiple components registered for the same class (for example - they share an interface), an exception will be thrown.
- *getMultipleFromContext(Class)* - returns all components for the given class. Might return an empty array.
- *removeFromContext(Class)* - extracts a single component for the given class, destroys it and removes from context. Throws exceptions if there are no or multiple components of the passed class.
- *removeMultipleFromContext(Class)* - extracts all components that share this class, destroys them and removes from context.
- *dispose()* - destroys all managed components, clears collections. ContextContainer should not be used after destruction.

###Scanners
As you might guess, they are used to scan for annotated classes. Each platform requires a custom scanner, unfortunately. Available implementations:

- **FixedClassScanner** - well, it *can* be used on any platform, but you have to manually select the classes it has access to, so it basically removes the concept of true component scan.
- **DesktopClassScanner** - scans binary classes (run from IDE) or jars in the class loader base location (run from a jar). Should be enough for both testing and most regular desktop applications.
- **GwtClassScanner** - registers LibGDX GWT reflection pool to scan through all classes registered for reflection.

Unfortunately, class scanners for Android and iOS are not implemented yet. I didn't have much time to do it yet (nor do I normally target these platforms); I will most likely look into it before releasing the next version, but for now - any help is appreciated. Use *FixedClassScanner*, sorry.

##Downsides
Autumn makes heavy use of reflection. While it doesn't rely on direct calls to class names that might get refactored, and while most of reflection-based invocations are made on start-up, Autumn application can be still somewhat slower than a regular one. (Not noticeably, I hope.) You can speed up the context building by giving the ContextContainer direct access to all Component-annotated classes, but you do have to sacrifice class path component scanning for that; it's much more sensible to keep all reflected classes in one package tree to limit the search.

##Dependency
Gradle dependency:

```
    compile "com.github.czyzby:gdx-autumn:0.6.$gdxVersion"
```
Currently supported LibGDX version is **1.6.0**.

To include Autumn in GWT, see [Autumn GWT](http://github.com/czyzby/gdx-autumn-gwt).

##What's new
0.5 -> 0.6:

- Since LibGDX introduced method annotations in reflection API, custom reflection wrappers (and separate GWT reflection pool, on which I spent quite some time) are no longer needed. Whole reflection API was removed and "native" LibGDX solutions are now used. Sorry for breaking your code - before LibGDX reflection expansion most of Autumn wouldn't be possible to implement without custom reflection.
- Components are no longer mapped by their interfaces as LibGDX reflection API does not provide method that returns implemented interfaces of a class. You can still inject components by their abstract classes (as long as there is only one concrete implementation of the abstract in the context).
- Now only absolutely necessary classes are registered for GWT reflection - less meta-data in JS.

0.4 -> 0.5:

- `OnEvent`- and `OnMessage`-annotated method can now decide whether their listeners should be kept or removed after method invocation at runtime by returning a boolean. *true* stands for removing after invocation, *false* - keeps the listeners for the next upcoming events/messages (just like in the annotation parameter: `removeAfterInvocation`). These values can be accessed statically for code clarity - see `EventProcessor` and `MessageProcessor` static fields.
- Partial injection is implemented for `@MetaComponent`s - as they are usually used only internally, this should be enough. Basically, regular injection is processed after all components are initiated, so there is no possibility of a scanned component to be unavailable for injection - meta components are processed before all others (to allow scanning for new, just registered component types) and they can inject only instances of classes that were already processed... which usually means only other metas. You can, however, inject lazy fields without a problem, as long as you don't call them before they are actually registered in the context. New instance injection is not possible. Meta components' lazy injections, for various reasons, do not trigger full object initiations, so lazy injection of lazy components to metas might be dangerous.
- Fixed GWT dependency bug, now correct modules are inherited.
- Fixed field and method annotations processing, now it should be a little faster and never throw weird LibGDX ObjectMap's errors (happened while registering a lot of custom processors while processing components with nested injections... the strange thing is - it was thrown only like 20% of time and never in debug. I think there might be something wrong with ObjectMap iterators, but oh-well). Now wrapped reflected fields and methods provide a getter for an array containing all present annotations.