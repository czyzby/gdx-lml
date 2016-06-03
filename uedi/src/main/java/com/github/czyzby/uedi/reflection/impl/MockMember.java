package com.github.czyzby.uedi.reflection.impl;

import java.lang.reflect.Member;

/** Mock up implementation of {@link Member} that allows to store and retrieve a chosen member name.
 *
 * @author MJ */
public final class MockMember implements Member {
    private final String name;

    /** @param name will be always returned by {@link #getName()}. */
    public MockMember(final String name) {
        this.name = name;
    }

    /** @return always null. */
    @Override
    public Class<?> getDeclaringClass() {
        return null;
    }

    /** @return stored member name.
     * @see #MockMember(String) */
    @Override
    public String getName() {
        return name;
    }

    /** @return always none (0). */
    @Override
    public int getModifiers() {
        return 0;
    }

    /** @return always {@code true}. */
    @Override
    public boolean isSynthetic() {
        return true;
    }
}
