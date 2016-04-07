package com.github.czyzby.websocket.serialization.impl;

import com.badlogic.gdx.utils.reflect.ArrayReflection;
import com.github.czyzby.websocket.serialization.ArrayProvider;

/** Uses reflection to construct typed arrays.
 *
 * @author MJ
 *
 * @param <Type> type of values stored in the array. */
public class ReflectionArrayProvider<Type> implements ArrayProvider<Type> {
    private final Class<Type> arrayType;

    /** @param arrayType type of arrays produced by the provider. */
    public ReflectionArrayProvider(final Class<Type> arrayType) {
        this.arrayType = arrayType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type[] getArray(final int size) {
        return (Type[]) ArrayReflection.newInstance(arrayType, size);
    }
}
