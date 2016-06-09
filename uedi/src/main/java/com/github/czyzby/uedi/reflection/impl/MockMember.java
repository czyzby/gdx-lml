package com.github.czyzby.uedi.reflection.impl;

import java.lang.reflect.Member;

/** Mock up implementation of {@link Member} that allows to store and retrieve a chosen member name. Reusable.
 *
 * @author MJ */
public final class MockMember implements Member {
    private String name;

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

    /** @param name will become current member name.
     * @see #getName() */
    public void setName(final String name) {
        this.name = name;
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
