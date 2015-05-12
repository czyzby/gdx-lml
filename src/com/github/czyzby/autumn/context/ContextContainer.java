package com.github.czyzby.autumn.context;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.Method;
import com.github.czyzby.autumn.context.processor.ComponentAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.field.DisposeAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.field.InjectAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.meta.ComponentMetaAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.meta.MetaAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.method.ComponentMethodAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.method.DestroyAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.method.EventProcessor;
import com.github.czyzby.autumn.context.processor.method.InitiateAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.method.MessageProcessor;
import com.github.czyzby.autumn.context.processor.method.event.common.ComponentDestructionEvent;
import com.github.czyzby.autumn.context.processor.method.message.common.AutumnRestrictedMessages;
import com.github.czyzby.autumn.context.processor.type.ComponentTypeAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.type.ConfigurationAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.type.ContextComponentAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.scanner.ClassScanner;
import com.github.czyzby.autumn.scanner.FixedClassScanner;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Context manager. Keeps track of components and annotation processors.
 *
 * @author MJ */
public class ContextContainer implements Disposable {
	/** Contains all components that can be injected or retrieved by their class. */
	private final ObjectMap<Class<?>, ObjectSet<ContextComponent>> context = LazyObjectMap.newMapOfSets();
	private final ObjectSet<ContextComponent> managedComponents = GdxSets.newSet();
	private final ObjectSet<ContextComponent> componentsToRegister = GdxSets.newSet();
	private final ObjectSet<ContextComponent> requestedLazyComponents = GdxSets.newSet();
	private final ObjectSet<ContextComponent> componentsToDestroy = GdxSets.newSet();

	// Registered processors:
	private final ObjectMap<Class<? extends Annotation>, ObjectSet<ComponentFieldAnnotationProcessor>> fieldAnnotationProcessors =
			LazyObjectMap.newMapOfSets();
	private final ObjectMap<Class<? extends Annotation>, ObjectSet<ComponentMethodAnnotationProcessor>> methodAnnotationProcessors =
			LazyObjectMap.newMapOfSets();
	private final ObjectMap<Class<? extends Annotation>, ObjectSet<ComponentTypeAnnotationProcessor>> typeAnnotationProcessors =
			LazyObjectMap.newMapOfSets();
	private final ObjectMap<Class<? extends Annotation>, ObjectSet<ComponentMetaAnnotationProcessor>> metaAnnotationProcessors =
			LazyObjectMap.newMapOfSets();

	private EventProcessor eventProcessor;
	private MessageProcessor messageProcessor;

	/** Creates a new context container with the default annotations processors. To scan for actual context
	 * components, {@link #registerComponents(Class, ClassScanner)} method has to be called. */
	public ContextContainer() {
		registerDefaultAnnotationProcessors();
		registerDefaultComponents();
	}

	/** @param scanningRootClass class in the root folder that determines which passed component classes are
	 *            accepted.
	 * @param componentClasses manually selected classes of components initiated by the context. */
	public ContextContainer(final Class<?> scanningRootClass, final Class<?>... componentClasses) {
		this(scanningRootClass, new FixedClassScanner(componentClasses));
	}

	/** @param scanningRootClass class in the root folder that determines where are the annotated classes taken
	 *            from.
	 * @param classScanner will be used to scan for annotated classes. */
	public ContextContainer(final Class<?> scanningRootClass, final ClassScanner classScanner) {
		this();
		registerComponents(scanningRootClass, classScanner);
		initiateRegisteredComponents();
	}

	private void registerDefaultAnnotationProcessors() {
		registerDefaultMetaAnnotationProcessors();
		registerDefaultMethodAnnotationProcessors();
		registerDefaultTypeAnnnotationProcessors();
		registerDefaultFieldAnnotationProcessors();
	}

	private void registerDefaultMetaAnnotationProcessors() {
		// Registers custom processors:
		registerMetaAnnotationProcessor(new MetaAnnotationProcessor());
	}

	private void registerDefaultMethodAnnotationProcessors() {
		eventProcessor = new EventProcessor();
		messageProcessor = new MessageProcessor();
		// Handles posted events:
		registerMethodAnnotationProcessor(eventProcessor);
		// Handles posted messages:
		registerMethodAnnotationProcessor(messageProcessor);
		// Invokes methods scheduled for initiation after dependencies are injected:
		registerMethodAnnotationProcessor(new InitiateAnnotationProcessor(messageProcessor));
		// Invokes methods scheduled for destruction when component is being destroyed:
		registerMethodAnnotationProcessor(new DestroyAnnotationProcessor(eventProcessor));
	}

	private void registerDefaultTypeAnnnotationProcessors() {
		// Finds actual context components:
		registerTypeAnnotationProcessor(new ContextComponentAnnotationProcessor());
		// Finds utility components, destroyed immediately after creation:
		registerTypeAnnotationProcessor(new ConfigurationAnnotationProcessor());
	}

	private void registerDefaultFieldAnnotationProcessors() {
		// Allows for component injection:
		registerFieldAnnotationProcessor(new InjectAnnotationProcessor());
		// Disposes of annotated fields:
		registerFieldAnnotationProcessor(new DisposeAnnotationProcessor(eventProcessor));
	}

	/** @param processor will be registered as meta annotation processor. If another processor is registered for
	 *            the same annotation class, both processors will be honored. Meta annotations are given to
	 *            classes that are also annotation processors. Meta components are processed before the
	 *            others, allowing to expand context functionalities. */
	public void registerMetaAnnotationProcessor(final ComponentMetaAnnotationProcessor processor) {
		metaAnnotationProcessors.get(processor.getProcessedAnnotationClass()).add(processor);
		registerProcessor(processor);
	}

	/** @param processor will be registered as any other component, allowing it to be injected. */
	private void registerProcessor(final ComponentAnnotationProcessor processor) {
		addComponentToContextClassHierarchyRecursively(processor.toContextComponent());
	}

	private void unregisterProcessor(final ComponentAnnotationProcessor processor) {
		removeComponentFromContextClassHierarchyRecursively(processor.toContextComponent());
	}

	/** @param forAnnotation meta annotation processors will be unregistered for this annotation type. */
	public void unregisterMetaAnnotationProcessors(final Class<? extends Annotation> forAnnotation) {
		final ObjectSet<ComponentMetaAnnotationProcessor> processors =
				metaAnnotationProcessors.get(forAnnotation);
		for (final ComponentAnnotationProcessor processor : processors) {
			unregisterProcessor(processor);
		}
		processors.clear();
	}

	/** @param processor will be registered as method annotation processor. If another processor is registered
	 *            for the same annotation class, both processors will be honored. Upon component initiation,
	 *            methods are processed after fields. */
	public void registerMethodAnnotationProcessor(final ComponentMethodAnnotationProcessor processor) {
		methodAnnotationProcessors.get(processor.getProcessedAnnotationClass()).add(processor);
		registerProcessor(processor);
	}

	/** @param forAnnotation method annotation processors will be unregistered for this annotation type. */
	public void unregisterMethodAnnotationProcessors(final Class<? extends Annotation> forAnnotation) {
		final ObjectSet<ComponentMethodAnnotationProcessor> processors =
				methodAnnotationProcessors.get(forAnnotation);
		for (final ComponentAnnotationProcessor processor : processors) {
			unregisterProcessor(processor);
		}
		processors.clear();
	}

	/** @param processor will be registered as field annotation processor. If another processor is registered
	 *            for the same annotation class, both processors will be honored. Upon component initiation,
	 *            fields are processed first. */
	public void registerFieldAnnotationProcessor(final ComponentFieldAnnotationProcessor processor) {
		fieldAnnotationProcessors.get(processor.getProcessedAnnotationClass()).add(processor);
		registerProcessor(processor);
	}

	/** @param forAnnotation field annotation processors will be unregistered for this annotation type. */
	public void unregisterFieldAnnotationProcessors(final Class<? extends Annotation> forAnnotation) {
		final ObjectSet<ComponentFieldAnnotationProcessor> processors =
				fieldAnnotationProcessors.get(forAnnotation);
		for (final ComponentAnnotationProcessor processor : processors) {
			unregisterProcessor(processor);
		}
		processors.clear();
	}

	/** @param processor will be registered as type annotation processor. If another processor is registered for
	 *            the same annotation class, both processors will be honored. Type annotations are given to
	 *            classes - if a class contains one of processed type annotations, it is considered a
	 *            component and it will be scanned for. */
	public void registerTypeAnnotationProcessor(final ComponentTypeAnnotationProcessor processor) {
		typeAnnotationProcessors.get(processor.getProcessedAnnotationClass()).add(processor);
		registerProcessor(processor);
	}

	/** @param forAnnotation type annotation processors will be unregistered for this annotation type. */
	public void unregisterTypeAnnotationProcessors(final Class<? extends Annotation> forAnnotation) {
		final ObjectSet<ComponentTypeAnnotationProcessor> processors =
				typeAnnotationProcessors.get(forAnnotation);
		for (final ComponentAnnotationProcessor processor : processors) {
			unregisterProcessor(processor);
		}
		processors.clear();
	}

	/** Looks for components to add to the context. Use {@link #initiateRegisteredComponents()} to init scanned
	 * components.
	 *
	 * @param scanningRootClass class in the root folder that determines where are the annotated classes taken
	 *            from. Root package is referenced by one of its classes instead of name to allow easy
	 *            structure refactors.
	 * @param classScanner used to scan for component classes. */
	public void registerComponents(final Class<?> scanningRootClass, final ClassScanner classScanner) {
		// Meta components are processed before the rest of components, as they might contain other component
		// processor with default component annotations that need to be scanned for.
		registerMetaComponents(classScanner.findClassesAnnotatedWith(scanningRootClass,
				getComponentMetaAnnotationClasses()));
		// Regular components processing:
		registerScannedContextComponents(classScanner.findClassesAnnotatedWith(scanningRootClass,
				getComponentTypeAnnotationClasses()));
		for (final ContextComponent component : componentsToRegister) {
			if (!component.isMapped()) {
				addComponentToContextClassHierarchyRecursively(component);
			}
		}
	}

	/** Initiates scanned components that should be added to the context. */
	public void initiateRegisteredComponents() {
		managedComponents.addAll(componentsToRegister);
		initiateComponents(componentsToRegister, false);
		componentsToRegister.clear();
	}

	private void registerScannedContextComponents(
			final ObjectMap<Class<? extends Annotation>, ObjectSet<Class<?>>> componentClasses) {
		for (final Entry<Class<? extends Annotation>, ObjectSet<Class<?>>> componentClassesForAnnotation : componentClasses) {
			for (final ComponentTypeAnnotationProcessor typeProcessor : typeAnnotationProcessors
					.get(componentClassesForAnnotation.key)) {
				for (final Class<?> componentClass : componentClassesForAnnotation.value) {
					typeProcessor.processClass(this, componentClass);
				}
			}
		}
	}

	/** For internal use of type processors only. Use {@link #putInContext(Class)} to manually add a context
	 * component.
	 *
	 * @param component is scheduled for registration. */
	public void registerComponent(final ContextComponent component) {
		componentsToRegister.add(component);
	}

	private void registerDefaultComponents() {
		// Registering itself, allowing context to be injected:
		addComponentToContextClassHierarchyRecursively(new ContextComponent(getClass(), this, false, true,
				true));
	}

	private Iterable<Class<? extends Annotation>> getComponentMetaAnnotationClasses() {
		return GdxArrays.newArray(metaAnnotationProcessors.keys());
	}

	private Iterable<Class<? extends Annotation>> getComponentTypeAnnotationClasses() {
		return GdxArrays.newArray(typeAnnotationProcessors.keys());
	}

	private void registerMetaComponents(
			final ObjectMap<Class<? extends Annotation>, ObjectSet<Class<?>>> metaComponentClasses) {
		for (final Entry<Class<? extends Annotation>, ObjectSet<Class<?>>> classesToProcess : metaComponentClasses) {
			final ObjectSet<ComponentMetaAnnotationProcessor> metaComponentProcessors =
					metaAnnotationProcessors.get(classesToProcess.key);
			for (final Class<?> componentClass : classesToProcess.value) {
				for (final ComponentMetaAnnotationProcessor metaProcessor : metaComponentProcessors) {
					metaProcessor.processClass(this, componentClass);
				}
			}
		}
	}

	/** @param component will be assigned to all its super classes in context, allowing it to be injected as
	 *            implementation of requested interface or extension of required class. */
	private void addComponentToContextClassHierarchyRecursively(final ContextComponent component) {
		Class<?> componentClass = component.getComponentClass();
		component.setMapped(true);
		while (componentClass != null && !componentClass.equals(Object.class)) {
			context.get(componentClass).add(component);
			componentClass = componentClass.getSuperclass();
		}
	}

	/** @param component will be removed from collections mapped to all its super classes in context. */
	private void removeComponentFromContextClassHierarchyRecursively(final ContextComponent component) {
		Class<?> componentClass = component.getComponentClass();
		component.setMapped(false);
		while (componentClass != null && !componentClass.equals(Object.class)) {
			context.get(componentClass).remove(component);
			componentClass = componentClass.getSuperclass();
		}
	}

	/** For internal use and custom annotation processors. Allows to extract a wrapped context component
	 * without triggering its full initiation.
	 *
	 * @param componentClass class of the desired component.
	 * @return wrapped context component. */
	public ContextComponent extractFromContext(final Class<?> componentClass) {
		final ObjectSet<ContextComponent> componentsForClass = context.get(componentClass);
		if (GdxSets.isEmpty(componentsForClass)) {
			throw new AutumnRuntimeException("Component unavailable in context: " + componentClass + ".");
		} else if (componentsForClass.size > 1) {
			throw new AutumnRuntimeException(
					"Ambiguous class reference - multiple components in context for class: " + componentClass
							+ ".");
		}
		return componentsForClass.first();
	}

	/** @return true if there is only one component for the given class. */
	public boolean contains(final Class<?> singleComponentForClass) {
		return context.containsKey(singleComponentForClass)
				&& GdxSets.sizeOf(context.get(singleComponentForClass)) == 1;
	}

	/** Default method for manual component extraction from the context. As a rule of thumb, if you can extract
	 * a component from the context through a injected field or method parameter, you should do that instead
	 * of calling this method.
	 *
	 * @param componentClass class of the desired component.
	 * @return a fully initiated context component of the selected class. */
	public <Type> Type getFromContext(final Class<Type> componentClass) {
		final ContextComponent component = extractFromContext(componentClass);
		if (!component.isInitiated()) {
			initiateComponent(component);
		}
		return component.getComponent(componentClass);
	}

	/** Specialized retrieval method that allows to return all components of the given type stored in the
	 * context.
	 *
	 * @param baseComponentClass class of the desired components.
	 * @return fully initiated context components of the selected class. */
	public <Type> Array<Type> getMultipleFromContext(final Class<Type> baseComponentClass) {
		final ObjectSet<ContextComponent> contextComponents = context.get(baseComponentClass);
		final Array<Type> components = GdxArrays.newArray();
		for (final ContextComponent component : contextComponents) {
			if (!component.isInitiated()) {
				initiateComponent(component);
			}
			components.add(component.getComponent(baseComponentClass));
		}
		return components;
	}

	/** @param method about to be invoked. Requires parameters from context.
	 * @return components from context matching method's required parameter types. */
	public Object[] prepareMethodParameters(final Method method) {
		final Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes.length == 0) {
			return Reflection.EMPTY_OBJECT_ARRAY;
		}
		final Object[] parameters = new Object[parameterTypes.length];
		for (int parameterIndex = 0; parameterIndex < parameters.length; parameterIndex++) {
			parameters[parameterIndex] = getFromContext(parameterTypes[parameterIndex]);
		}
		return parameters;
	}

	/** This method fully initiates an object of the given class, but it does not register it in the main
	 * context container. The instance will still be managed (as in Dispose and Destroy annotations will be
	 * honored on context destruction), but it will not be accessible by {@link #getFromContext(Class)} or
	 * {@link #extractFromContext(Class)}
	 *
	 * @param componentClass class of the component to initiate. This class has to be GWT-reflected in order
	 *            to work properly.
	 * @return a new instance of the component outside of main context container. */
	@SuppressWarnings("unchecked")
	public <Type> Type createWithContext(final Class<Type> componentClass) {
		final ContextComponent component = constructRequestedComponent(componentClass);
		initiateComponent(component);
		return (Type) component.getComponent();
	}

	/** Constructs a context component of the selected class. For internal use only. */
	public ContextComponent constructRequestedComponent(final Class<?> componentClass) {
		ContextComponent component = null;
		for (final ObjectSet<ComponentTypeAnnotationProcessor> typeProcessors : typeAnnotationProcessors
				.values()) {
			for (final ComponentTypeAnnotationProcessor processor : typeProcessors) {
				// Naive approach - asking the first applicable type processor to create the object.
				if (ClassReflection.isAnnotationPresent(componentClass,
						processor.getProcessedAnnotationClass())) {
					component = processor.prepareComponent(this, componentClass);
					if (component != null) {
						break;
					}
				}
			}
		}
		if (component == null) {
			try {
				component = new ContextComponent(componentClass, ClassReflection.newInstance(componentClass));
			} catch (final Throwable exception) {
				throw new AutumnRuntimeException("Unable to prepare a new instance of: " + componentClass
						+ ". Does it have a public no-arg constructor?", exception);
			}
		}
		managedComponents.add(component);
		return component;
	}

	/** @param componentClass an instance of this class will be fully initiated and mapped into the context for
	 *            all of its super class and interfaces. This class has to be GWT-reflected in order to work
	 *            properly. */
	public <Type> void putInContext(final Class<Type> componentClass) {
		final ContextComponent component = constructRequestedComponent(componentClass);
		initiateComponent(component);
		if (component.isKeptInContext()) {
			addComponentToContextClassHierarchyRecursively(component);
		}
	}

	/** @param componentClass class that points to a single component. This component will be destroyed and
	 *            removed from context. */
	public void removeFromContext(final Class<?> componentClass) {
		final ContextComponent component = extractFromContext(componentClass);
		componentsToDestroy.add(component);
		removeComponentsToDestroy();
	}

	/** @param baseComponentClass components of this class - or any of this class' children - will be destroyed
	 *            and removed from context. */
	public void removeMultipleFromContext(final Class<?> baseComponentClass) {
		componentsToDestroy.addAll(context.get(baseComponentClass));
		removeComponentsToDestroy();
	}

	/** Mostly for internal use and custom processors. When component is lazy and not initialized, it can be
	 * requested to wake up by the processor - context initiates such components after regular fields and
	 * methods processing.
	 *
	 * @param lazyComponent will be scheduled to initiate. */
	public void requestToWakeLazyComponent(final ContextComponent lazyComponent) {
		requestedLazyComponents.add(lazyComponent);
	}

	private void initiateComponents(final Iterable<ContextComponent> components, final boolean wakeLazies) {
		invokeInitiationActions(components, wakeLazies);
		postInitiationMessage();
		removeComponentsToDestroy();
	}

	private void initiateComponent(final ContextComponent component) {
		invokeInitiationActionsForComponent(component);
		wakeRequestedLazies();
		postInitiationMessage();
		removeComponentsToDestroy();
	}

	private void removeComponentsToDestroy() {
		if (GdxSets.isNotEmpty(componentsToDestroy)) {
			postDestructionEventFor(componentsToDestroy);
			for (final ContextComponent componentToDestroy : componentsToDestroy) {
				removeComponentFromContextClassHierarchyRecursively(componentToDestroy);
				managedComponents.remove(componentToDestroy);
			}
			componentsToDestroy.clear();
		}
	}

	private void postInitiationMessage() {
		messageProcessor.postMessage(AutumnRestrictedMessages.COMPONENT_INITIATION);
	}

	private void invokeInitiationActions(final Iterable<ContextComponent> components, final boolean wakeLazies) {
		for (final ContextComponent component : components) {
			if (component.isInitiated() || !wakeLazies && component.isLazy()) {
				continue;
			}
			invokeInitiationActionsForComponent(component);
			// Component not kept in context: removing immediately after initiation.
			if (!component.isKeptInContext()) {
				componentsToDestroy.add(component);
			}
		}
		wakeRequestedLazies();
	}

	private void invokeInitiationActionsForComponent(final ContextComponent component) {
		processInitiatedComponentFields(component);
		processInitiatedComponentMethods(component);
		component.setInitiated(true);
	}

	private void wakeRequestedLazies() {
		if (GdxSets.isNotEmpty(requestedLazyComponents)) {
			final ObjectSet<ContextComponent> lazies = GdxSets.newSet(requestedLazyComponents); // Optimize?
			requestedLazyComponents.clear();
			invokeInitiationActions(lazies, true);
		}
	}

	// No, I'm not proud of a triple loop.
	private void processInitiatedComponentFields(final ContextComponent component) {
		for (final Field field : ClassReflection.getDeclaredFields(component.getComponentClass())) {
			for (final com.badlogic.gdx.utils.reflect.Annotation annotation : field.getDeclaredAnnotations()) {
				if (fieldAnnotationProcessors.containsKey(annotation.getAnnotationType())) {
					for (final ComponentFieldAnnotationProcessor processor : fieldAnnotationProcessors
							.get(annotation.getAnnotationType())) {
						processor.processField(this, component, field);
					}
				}
			}
		}
	}

	private void processInitiatedComponentMethods(final ContextComponent component) {
		for (final Method method : ClassReflection.getDeclaredMethods(component.getComponentClass())) {
			try {
				for (final com.badlogic.gdx.utils.reflect.Annotation annotation : method
						.getDeclaredAnnotations()) {
					if (methodAnnotationProcessors.containsKey(annotation.getAnnotationType())) {
						for (final ComponentMethodAnnotationProcessor processor : methodAnnotationProcessors
								.get(annotation.getAnnotationType())) {
							processor.processMethod(this, component, method);
						}
					}
				}
			} catch (final Throwable exception) {
				// Somewhat expected on GWT.
			}
		}
	}

	private void postDestructionEventFor(final Iterable<ContextComponent> componentsToDestroy) {
		final ComponentDestructionEvent destructionEvent =
				new ComponentDestructionEvent(GdxArrays.newArray(componentsToDestroy));
		eventProcessor.postEvent(destructionEvent);
	}

	private void postDestructionEventForAllManagedComponents() {
		postDestructionEventFor(managedComponents);
	}

	@Override
	public void dispose() {
		postDestructionEventForAllManagedComponents();

		clearUtilityComponentCollections();
		clearAnnotationProcessorCollections();
		clearCoreProcessors();

		context.clear();
	}

	private void clearUtilityComponentCollections() {
		GdxSets.clearAll(managedComponents, componentsToRegister, componentsToDestroy,
				requestedLazyComponents);
	}

	private void clearAnnotationProcessorCollections() {
		GdxMaps.clearAll(fieldAnnotationProcessors, metaAnnotationProcessors, methodAnnotationProcessors,
				typeAnnotationProcessors);
	}

	private void clearCoreProcessors() {
		eventProcessor = null;
		messageProcessor = null;
	}
}
