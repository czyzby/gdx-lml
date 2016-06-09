package com.github.czyzby.uedi.impl;

import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.reflection.impl.MockMember;
import com.github.czyzby.uedi.reflection.impl.Modifier;
import com.github.czyzby.uedi.scanner.ClassScanner;
import com.github.czyzby.uedi.stereotype.Factory;
import com.github.czyzby.uedi.stereotype.Property;
import com.github.czyzby.uedi.stereotype.Provider;
import com.github.czyzby.uedi.stereotype.Singleton;

/** Implements common functionalities of {@link Context} implementations. Holds preference.
 *
 * @author MJ
 * @see DefaultContext */
public abstract class AbstractContext implements Context {
    private ClassScanner classScanner;

    private int fieldsIgnoreFilter = Modifier.TRANSIENT | Modifier.STATIC;
    private int fieldsIgnoreSignature = Modifier.TRANSIENT;
    private int methodsIgnoreFilter = Modifier.STATIC | Modifier.NATIVE;
    private int methodsIgnoreSignature = Modifier.PUBLIC | Modifier.STATIC;
    private int iterationsAmount = 15;
    private boolean failIfUnknown = true;
    private boolean failIfAmbiguous = true;
    private boolean processSuperFields = true;
    private boolean mapSuperTypes = true;
    private boolean ignoreStrings;

    // Limits unnecessary objects creation:
    private final MockMember helperMember = new MockMember(Strings.EMPTY_STRING);

    /** @param classScanner can be null, but {@link #scan(Class)} method will not work correctly. */
    public AbstractContext(final ClassScanner classScanner) {
        this.classScanner = classScanner;
    }

    @Override
    public ClassScanner getClassScanner() {
        return classScanner;
    }

    @Override
    public void setClassScanner(final ClassScanner classScanner) {
        this.classScanner = classScanner;
    }

    @Override
    public void scan(final Class<?> root) {
        if (classScanner == null) {
            throw new RuntimeException("Unable to scan root package: " + root.getName() + " without a scanner.");
        }
        processClasses(classScanner.getClassesImplementing(root, Factory.class, Property.class, Provider.class,
                Singleton.class));
    }

    /** @param classes were scanned for. Should be created, initiated and processed. */
    protected abstract void processClasses(Iterable<Class<?>> classes);

    @Override
    public Context initiate(final Object component) {
        processComponent(component);
        return this;
    }

    /** @param component its non-primitive, empty, non-transient fields should be injected. Must be included for
     *            initiation and destruction if implements any life cycle interfaces. */
    protected abstract void processComponent(Object component);

    @Override
    public void addProvider(final Provider<?> provider) {
        add(provider);
        processProvider(provider);
    }

    /** @param provider was registered as a provider component. Must be bound to the class tree of the supported
     *            type. */
    protected abstract void processProvider(Provider<?> provider);

    @Override
    public void addFactory(final Object factory) {
        add(factory);
        processFactory(factory);
    }

    /** @param factory was registered as a factory component. Its public methods much be converted into providers. */
    protected abstract void processFactory(Object factory);

    @Override
    public <Component> Component get(final Class<Component> type) {
        return get(type, null, null);
    }

    @Override
    public <Component> Component get(final String id, final Class<Component> type) {
        helperMember.setName(id);
        return get(type, null, helperMember);
    }

    @Override
    public <Component> Component get(final Class<Component> type, final Object forObject) {
        return get(type, forObject, null);
    }

    @Override
    public <Component> Component getOrElse(final Class<Component> type, final Component alternative) {
        final Component component = get(type);
        return component != null ? component : alternative;
    }

    @Override
    public void setFailIfUnknownType(final boolean fail) {
        failIfUnknown = fail;
    }

    @Override
    public boolean isFailIfAmbiguousDependency() {
        return failIfAmbiguous;
    }

    @Override
    public void setFailIfAmbiguousDependency(final boolean fail) {
        failIfAmbiguous = fail;
    }

    @Override
    public boolean isFailIfUnknownType() {
        return failIfUnknown;
    }

    @Override
    public void setProcessSuperFields(final boolean process) {
        processSuperFields = process;
    }

    @Override
    public boolean isProcessSuperFields() {
        return processSuperFields;
    }

    @Override
    public void setMapSuperTypes(final boolean process) {
        mapSuperTypes = process;
    }

    @Override
    public boolean isMapSuperTypes() {
        return mapSuperTypes;
    }

    @Override
    public int getFieldsIgnoreFilter() {
        return fieldsIgnoreFilter;
    }

    @Override
    public void setFieldsIgnoreFilter(final int filter) {
        fieldsIgnoreFilter = filter;
    }

    @Override
    public int getFieldsIgnoreSignature() {
        return fieldsIgnoreSignature;
    }

    @Override
    public void setFieldsIgnoreSignature(final int signature) {
        fieldsIgnoreSignature = signature;
    }

    @Override
    public void setIgnoreStrings(final boolean ignore) {
        ignoreStrings = ignore;
    }

    @Override
    public boolean isIgnoreStrings() {
        return ignoreStrings;
    }

    @Override
    public void setIterationsAmount(final int iterations) {
        iterationsAmount = iterations;
    }

    @Override
    public void setMethodsIgnoreFilter(final int filter) {
        methodsIgnoreFilter = filter;
    }

    @Override
    public void setMethodsIgnoreSignature(final int signature) {
        methodsIgnoreSignature = signature;
    }

    @Override
    public int getMethodsIgnoreFilter() {
        return methodsIgnoreFilter;
    }

    @Override
    public int getMethodsIgnoreSignature() {
        return methodsIgnoreSignature;
    }

    @Override
    public int getIterationsAmount() {
        return iterationsAmount;
    }
}
