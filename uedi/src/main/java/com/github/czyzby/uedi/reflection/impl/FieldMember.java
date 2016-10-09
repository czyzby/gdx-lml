package com.github.czyzby.uedi.reflection.impl;

import java.lang.reflect.Member;

import com.badlogic.gdx.utils.reflect.Field;

/** Wraps around {@link Field} to mock {@link Member} interface.
 *
 * @author MJ */
public class FieldMember implements Member {
    private final Field field;

    /** @param field will be wrapped. */
    public FieldMember(final Field field) {
        this.field = field;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return field.getDeclaringClass();
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public int getModifiers() {
        return Modifier.getModifiers(field);
    }

    @Override
    public boolean isSynthetic() {
        return field.isSynthetic();
    }

    /** @return wrapped {@link Field} instance. */
    public Field getField() {
        return field;
    }
}
