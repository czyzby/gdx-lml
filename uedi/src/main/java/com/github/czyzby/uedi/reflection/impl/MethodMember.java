package com.github.czyzby.uedi.reflection.impl;

import java.lang.reflect.Member;

import com.badlogic.gdx.utils.reflect.Method;

/** Wraps around {@link Method} to mock {@link Member} interface.
 *
 * @author MJ */
public class MethodMember implements Member {
    private final Method method;

    /** @param method will be wrapped. */
    public MethodMember(final Method method) {
        this.method = method;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return method.getDeclaringClass();
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public int getModifiers() {
        return Modifier.getModifiers(method);
    }

    @Override
    public boolean isSynthetic() {
        return false;
    }

    /** @return wrapped {@link Method} instance. */
    public Method getMethod() {
        return method;
    }
}
