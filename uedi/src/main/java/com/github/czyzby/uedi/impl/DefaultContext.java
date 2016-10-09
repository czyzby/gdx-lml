package com.github.czyzby.uedi.impl;

import java.lang.reflect.Member;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.Method;
import com.github.czyzby.kiwi.util.common.Exceptions;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;
import com.github.czyzby.kiwi.util.gdx.collection.pooled.PooledList;
import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.reflection.impl.ConstructorMember;
import com.github.czyzby.uedi.reflection.impl.FieldMember;
import com.github.czyzby.uedi.reflection.impl.Modifier;
import com.github.czyzby.uedi.scanner.ClassScanner;
import com.github.czyzby.uedi.stereotype.Destructible;
import com.github.czyzby.uedi.stereotype.Factory;
import com.github.czyzby.uedi.stereotype.Initiated;
import com.github.czyzby.uedi.stereotype.Property;
import com.github.czyzby.uedi.stereotype.Provider;
import com.github.czyzby.uedi.stereotype.impl.PropertyProvider;
import com.github.czyzby.uedi.stereotype.impl.ProviderManager;
import com.github.czyzby.uedi.stereotype.impl.Providers;
import com.github.czyzby.uedi.stereotype.impl.ReflectionProvider;
import com.github.czyzby.uedi.stereotype.impl.SingletonProvider;
import com.github.czyzby.uedi.stereotype.impl.StringProvider;

/** Core implementation of context management. LibGDX-compatible: uses LibGDX reflection API wrappers rather than
 * regular Java API. Not thread-safe.
 *
 * @author MJ */
public class DefaultContext extends AbstractContext {
    /** These methods will be ignored when processing factories. */
    public static final ObjectSet<String> FORBIDDEN_METHOD_NAMES = GdxSets.newSet();

    private final IdentityMap<Class<?>, Provider<?>> context = GdxMaps.newIdentityMap();
    private final ObjectSet<Destructible> destructibles = GdxSets.newSet();
    private final StringProvider propertyProvider = getPropertyProvider();

    static {
        // Forbidden method names:
        FORBIDDEN_METHOD_NAMES.add("toString");
        FORBIDDEN_METHOD_NAMES.add("wait");
        FORBIDDEN_METHOD_NAMES.add("clone");
        FORBIDDEN_METHOD_NAMES.add("equals");
        FORBIDDEN_METHOD_NAMES.add("finalize");
        FORBIDDEN_METHOD_NAMES.add("notify");
        FORBIDDEN_METHOD_NAMES.add("notifyAll");
        FORBIDDEN_METHOD_NAMES.add("hashCode");
        FORBIDDEN_METHOD_NAMES.add("getClass");
    }

    /** @param classScanner can be null, but {@link #scan(Class)} method will not work correctly. */
    public DefaultContext(final ClassScanner classScanner) {
        super(classScanner);
        addCoreProviders();
    }

    /** @return default provider of {@link String} instances. */
    protected StringProvider getPropertyProvider() {
        return new PropertyProvider();
    }

    /** Registers {@link Context} (so it can be injected) and binds {@link PropertyProvider} to {@link String}
     * injections. */
    protected void addCoreProviders() {
        context.put(String.class, propertyProvider);
        context.put(Context.class, new SingletonProvider<Context>(this));
    }

    @Override
    public void add(final Object component) {
        processProvider(new SingletonProvider<Object>(component));
    }

    @Override
    public boolean isAvailable(final Class<?> type) {
        return context.containsKey(type);
    }

    @Override
    public String getProperty(final String name) {
        return propertyProvider.hasProperty(name) ? propertyProvider.getProperty(name).getValue() : null;
    }

    @Override
    public void setProperty(final String key, final String value) {
        if (propertyProvider.hasProperty(key)) {
            propertyProvider.getProperty(key).setValue(value);
        } else {
            addProperty(new Property() {
                private String property = value;

                @Override
                public String setValue(final String value) {
                    return property = value;
                }

                @Override
                public String getValue() {
                    return property;
                }

                @Override
                public String getKey() {
                    return key;
                }
            });
        }
    }

    @Override
    public void addProperty(final Property property) {
        propertyProvider.addProperty(property);
    }

    @Override
    public void addDestructible(final Destructible destructible) {
        destructibles.add(destructible);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Component> Component get(final Class<Component> type, final Object forObject, final Member member) {
        if (!context.containsKey(type)) {
            if (isFailIfUnknownType()) {
                throw new RuntimeException("Unknown component type: " + type.getName());
            }
            return create(type);
        }
        return (Component) context.get(type).provide(forObject, member);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Component> Component create(final Class<Component> type) {
        final Constructor constructor = getConstructor(type);
        final Object component = createObject(constructor, constructor.getParameterTypes());
        initiate(component);
        return (Component) component;
    }

    @Override
    public void destroy() {
        final Array<Destructible> sortedDestructibles = GdxArrays.newArray(destructibles);
        sortedDestructibles.sort(new Comparator<Destructible>() {
            @Override
            public int compare(final Destructible o1, final Destructible o2) {
                return o1.getDestructionOrder() - o2.getDestructionOrder();
            }
        });
        destructibles.clear();
        try {
            for (final Destructible destructible : sortedDestructibles) {
                destructible.destroy();
            }
        } catch (final Exception exception) {
            throw new RuntimeException("Unable to destroy context.", exception);
        }
    }

    @Override
    public void destroy(final Destructible component) {
        if (component != null) {
            destructibles.remove(component);
            try {
                component.destroy();
            } catch (final Exception exception) {
                throw new RuntimeException("Unable to destroy: " + component, exception);
            }
        }
    }

    @Override
    protected void processClasses(final Iterable<Class<?>> classes) {
        try {
            final Array<Initiated> componentsToInitiate = gatherComponents(gatherConstructors(classes));
            componentsToInitiate.sort(new Comparator<Initiated>() {
                @Override
                public int compare(final Initiated o1, final Initiated o2) {
                    return o1.getInitiationOrder() - o2.getInitiationOrder();
                }
            });
            for (final Initiated initiated : componentsToInitiate) {
                initiated.initiate();
            }
        } catch (final RuntimeException exception) {
            throw exception;
        } catch (final Exception exception) {
            throw new RuntimeException("Unable to create components.", exception);
        }
    }

    /** @param constructors list of gathered constructors. Will be used to create the components.
     * @return sorting collection of components to initiate. Should be initiated.
     * @throws Exception due to reflection issues. */
    protected Array<Initiated> gatherComponents(final PooledList<Constructor> constructors) throws Exception {
        final Array<Initiated> componentsToInitiate = GdxArrays.newArray();
        final Array<Object> components = createComponents(constructors, componentsToInitiate);
        for (final Object component : components) {
            injectFields(component);
        }
        return componentsToInitiate;
    }

    /** @param constructors list of gathered constructors of classes to initiate.
     * @param componentsToInitiate a reference to sorting collection of components to initiate. Should be filled.
     * @return list of constructed components.
     * @throws Exception due to reflection issues. */
    protected Array<Object> createComponents(final PooledList<Constructor> constructors,
            final Array<Initiated> componentsToInitiate) throws Exception {
        final Array<Object> components = GdxArrays.newArray();
        for (int index = 0, iterations = getIterationsAmount(); constructors.isNotEmpty()
                && index < iterations; index++) {
            for (final Constructor constructor : constructors) {
                final Object component;
                if (constructor.getParameterTypes().length == 0) {
                    component = constructor.newInstance(Providers.EMPTY_ARRAY);
                } else {
                    final Class<?>[] parameterTypes = constructor.getParameterTypes();
                    if (isAnyProviderMissing(parameterTypes)) {
                        continue;
                    }
                    component = createObject(constructor, parameterTypes);
                }
                processScannedComponent(component, componentsToInitiate);
                components.add(component);
                constructors.remove();
            }
        }
        if (!constructors.isEmpty()) {
            if (isFailIfUnknownType()) {
                final Array<String> classNames = GdxArrays.newArray();
                for (final Constructor constructor : constructors) {
                    classNames.add(constructor.getDeclaringClass().getName());
                }
                throw new RuntimeException(
                        "Unknown or circular dependencies detected. Unable to create instances of: " + classNames);
            }
            for (final Constructor constructor : constructors) {
                final Object component = createObject(constructor, constructor.getParameterTypes());
                processScannedComponent(component, componentsToInitiate);
                components.add(component);
            }
        }
        return components;
    }

    /** @param constructor will be used to construct the instance.
     * @param parameterTypes will be used to extract constructor parameters from the context.
     * @return an instance of the class.
     * @throws RuntimeException due to reflection issues. */
    protected Object createObject(final Constructor constructor, final Class<?>[] parameterTypes) {
        try {
            if (parameterTypes.length == 0) {
                return constructor.newInstance(Providers.EMPTY_ARRAY);
            }
            final Object[] dependencies = new Object[parameterTypes.length];
            for (int index = 0, length = dependencies.length; index < length; index++) {
                dependencies[index] = get(parameterTypes[index], null, new ConstructorMember(constructor));
            }
            return constructor.newInstance(dependencies);
        } catch (final Exception exception) {
            throw new RuntimeException("Unable to create an instance of: " + constructor.getDeclaringClass(),
                    exception);
        }
    }

    /** @param types array of requested types.
     * @return true if context currently has no provider that could supply an instance of any of the passed classes. */
    protected boolean isAnyProviderMissing(final Class<?>... types) {
        for (final Class<?> type : types) {
            if (!context.containsKey(type)) {
                return true;
            }
        }
        return false;
    }

    /** @param component its interfaces will be inspected. Depending on its type, it might be initiated, scheduled for
     *            destruction or registered as a factory, provider or property.
     * @param componentsToInitiate will be used to schedule initiations. */
    protected void processScannedComponent(final Object component, final Array<Initiated> componentsToInitiate) {
        processProvider(new SingletonProvider<Object>(component));
        if (component instanceof Destructible) {
            destructibles.add((Destructible) component);
        }
        if (component instanceof Factory) {
            processFactory(component);
        }
        if (component instanceof Initiated) {
            componentsToInitiate.add((Initiated) component);
        }
        if (component instanceof Property) {
            propertyProvider.addProperty((Property) component);
        }
        if (component instanceof Provider<?>) {
            processProvider((Provider<?>) component);
        }
    }

    /** @param classes will have their constructors extracted. Should not contain interfaces or abstract classes.
     * @return a collection of constructors allowing to create passed classes' instances. */
    protected PooledList<Constructor> gatherConstructors(final Iterable<Class<?>> classes) {
        final PooledList<Constructor> constructors = PooledList.newList();
        for (final Class<?> componentClass : classes) {
            constructors.add(getConstructor(componentClass));
        }
        return constructors;
    }

    /** @param componentClass is requested to be constructed.
     * @return the first found constructor for the class. */
    protected Constructor getConstructor(final Class<?> componentClass) {
        final Constructor[] constructors = ClassReflection.getConstructors(componentClass);
        if (constructors.length == 0) {
            throw new RuntimeException("No public constructors found for component class: " + componentClass);
        }
        return constructors[0];
    }

    /** Note: this method should be invoked only with externally registered components.
     *
     * @param component will be initiated.
     * @see #processScannedComponent(Object, Array) */
    @Override
    protected void processComponent(final Object component) {
        injectFields(component);
        if (component instanceof Initiated) {
            try {
                ((Initiated) component).initiate();
            } catch (final Exception exception) {
                throw new RuntimeException("Unable to initiate component: " + component, exception);
            }
        }
        if (component instanceof Destructible) {
            destructibles.add((Destructible) component);
        }
    }

    /** @return direct reference to component providers. */
    protected IdentityMap<Class<?>, Provider<?>> getComponentProviders() {
        return context;
    }

    /** @param component its injectable fields will be filled with values provided by the context.
     * @see #isInjectable(Field, Object) */
    @SuppressWarnings("unchecked")
    protected void injectFields(final Object component) {
        Class<?> processedClass = component.getClass();
        try {
            while (processedClass != null && processedClass != Object.class) {
                for (final Field field : ClassReflection.getDeclaredFields(processedClass)) {
                    if (isInjectable(field, component)) {
                        field.set(component, get(field.getType(), component, new FieldMember(field)));
                    }
                }
                if (!isProcessSuperFields()) {
                    break;
                }
                processedClass = processedClass.getSuperclass();
            }
        } catch (final Exception exception) {
            throw new RuntimeException("Unable to inject fields of component: " + component, exception);
        }
    }

    /** @param field reflected field data.
     * @param component owner of the field.
     * @return true if the field is empty, accepted by the modifier filter, does not match modifier signature, not
     *         primitive and - if strings are ignored - not a string.
     * @throws Exception due to reflection issues. */
    protected boolean isInjectable(final Field field, final Object component) throws Exception {
        try {
            if (field.isSynthetic() || field.getType().isPrimitive()
                    || isIgnoreStrings() && field.getType() == String.class) {
                return false;
            }
        } catch (final Exception exception) {
            Exceptions.ignore(exception); // GWT compatibility.
            Gdx.app.debug("UEDI", "Unable to access field of component: " + component, exception);
            return false;
        }
        final int modifier = Modifier.getModifiers(field);
        if ((modifier & getFieldsIgnoreFilter()) != 0 || modifier == getFieldsIgnoreSignature()) {
            return false;
        }
        field.setAccessible(true);
        return field.get(component) == null;
    }

    @Override
    protected void processProvider(final Provider<?> provider) {
        if (!isMapSuperTypes()) {
            putProvider(provider.getType(), provider);
            return;
        }
        final PooledList<Class<?>> classesToProcess = new PooledList<Class<?>>();
        classesToProcess.add(provider.getType());
        while (classesToProcess.isNotEmpty()) {
            final Class<?> processed = classesToProcess.removeFirst();
            putProvider(processed, provider);
            final Class<?> parent = processed.getSuperclass();
            if (parent != null && parent != Object.class) {
                classesToProcess.add(parent);
            }
        }
    }

    /** @param key provided class type.
     * @param provider will be assigned as a provider of the chosen class instances. */
    protected void putProvider(final Class<?> key, final Provider<?> provider) {
        final Provider<?> currentProvider = context.get(key);
        if (currentProvider == null) { // Unique - setting as the default provider:
            context.put(key, provider);
        } else if (currentProvider instanceof ProviderManager<?>) { // Already ambiguous - adding another provider:
            ((ProviderManager<?>) currentProvider).addProvider(provider);
        } else {
            @SuppressWarnings({ "rawtypes", "unchecked" }) // Ambiguous - switching to manager:
            final ProviderManager<?> manager = new ProviderManager(key, this);
            // Registering existing providers:
            manager.addProvider(currentProvider);
            manager.addProvider(provider);
            // Replacing current provider with the manager:
            context.put(key, manager);
        }
    }

    @Override
    public void remove(final Class<?> type) {
        context.remove(type);
    }

    @Override
    public <Type> void replace(final Class<Type> type, final Provider<? extends Type> provider) {
        remove(type);
        putProvider(type, provider);
    }

    @Override
    protected void processFactory(final Object factory) {
        // Registering public methods as providers:
        for (final Method method : ClassReflection.getMethods(factory.getClass())) {
            if (isValidFactoryMethod(method)) {
                processProvider(newFactoryMethodWrapper(factory, method));
            }
        }
    }

    /** @param method cannot be synthetic, return void, have a forbidden name or have any filtered modifiers.
     * @return true if the method is valid and should be converted to a provider. */
    protected boolean isValidFactoryMethod(final Method method) {
        final int modifiers = Modifier.getModifiers(method);
        return (modifiers & getMethodsIgnoreFilter()) == 0 && modifiers != getMethodsIgnoreSignature()
                && method.getReturnType() != void.class && method.getReturnType() != Void.class
                && !FORBIDDEN_METHOD_NAMES.contains(method.getName());
    }

    /** @param factory owner of the method.
     * @param method should be wrapped.
     * @return method wrapped in a {@link Provider} implementation. */
    protected Provider<?> newFactoryMethodWrapper(final Object factory, final Method method) {
        return new ReflectionProvider(this, factory, method);
    }

    @Override
    public boolean isParameterAware() {
        return false;
    }

    @Override
    public void clear(final Class<?> classTree) {
        if (!isMapSuperTypes()) {
            remove(classTree);
            return;
        }
        final PooledList<Class<?>> classesToProcess = new PooledList<Class<?>>();
        classesToProcess.add(classTree);
        while (classesToProcess.isNotEmpty()) {
            final Class<?> processed = classesToProcess.removeFirst();
            remove(processed);
            final Class<?> parent = processed.getSuperclass();
            if (parent != null && parent != Object.class) {
                classesToProcess.add(parent);
            }
        }
    }

    @Override
    public void clear() {
        context.clear();
        addCoreProviders();
    }
}
