package com.github.czyzby.uedi.stereotype.impl;

import java.lang.reflect.Member;

import com.github.czyzby.uedi.stereotype.Default;
import com.github.czyzby.uedi.stereotype.Named;

/** Always provides the same object.
 *
 * @author MJ
 *
 * @param <Type> type of singleton instance. */
public class SingletonProvider<Type> implements DelegateProvider<Type>, Named {
    private final Type instance;
    private final Class<Type> type;
    private final String name;
    private final boolean isDefault;

    /** @param instance will always be provided. */
    @SuppressWarnings("unchecked")
    public SingletonProvider(final Type instance) {
        this.instance = instance;
        type = (Class<Type>) instance.getClass();
        name = Providers.getName(instance);
        isDefault = instance instanceof Default;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public Object getWrappedObject() {
        return instance;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<? extends Type> getType() {
        return type;
    }

    @Override
    public Type provide(final Object target, final Member member) {
        return instance;
    }
}
