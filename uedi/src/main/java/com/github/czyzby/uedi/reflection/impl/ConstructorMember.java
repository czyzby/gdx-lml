package com.github.czyzby.uedi.reflection.impl;

import java.lang.reflect.Member;

import com.badlogic.gdx.utils.reflect.Constructor;

/** Wraps around {@link Constructor} to mock {@link Member} interface.
 *
 * @author MJ */
public class ConstructorMember implements Member {
    private final Constructor constructor;

    /** @param constructor will be wrapped. */
    public ConstructorMember(final Constructor constructor) {
        this.constructor = constructor;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return constructor.getDeclaringClass();
    }

    @Override
    public String getName() {
        return constructor.getDeclaringClass().getSimpleName();
    }

    @Override
    public int getModifiers() {
        return Modifier.PUBLIC; // Unknown.
    }

    @Override
    public boolean isSynthetic() {
        return false;
    }

    /** @return wrapped {@link Constructor} instance. */
    public Constructor getConstructor() {
        return constructor;
    }
}
