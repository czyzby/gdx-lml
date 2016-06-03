package com.github.czyzby.uedi.reflection.impl;

import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.Method;

/** Mocks {@link java.lang.reflect.Modifier} API.
 *
 * @author MJ */
public class Modifier {
    /** The {@code int} value representing the {@code public} modifier. */
    public static final int PUBLIC = 0x00000001;
    /** The {@code int} value representing the {@code private} modifier. */
    public static final int PRIVATE = 0x00000002;
    /** The {@code int} value representing the {@code protected} modifier. */
    public static final int PROTECTED = 0x00000004;
    /** The {@code int} value representing the {@code static} modifier. */
    public static final int STATIC = 0x00000008;
    /** The {@code int} value representing the {@code final} modifier. */
    public static final int FINAL = 0x00000010;
    /** The {@code int} value representing the {@code synchronized} modifier. */
    public static final int SYNCHRONIZED = 0x00000020;
    /** The {@code int} value representing the {@code volatile} modifier. */
    public static final int VOLATILE = 0x00000040;
    /** The {@code int} value representing the {@code transient} modifier. */
    public static final int TRANSIENT = 0x00000080;
    /** The {@code int} value representing the {@code native} modifier. */
    public static final int NATIVE = 0x00000100;
    /** The {@code int} value representing the {@code interface} modifier. */
    public static final int INTERFACE = 0x00000200;
    /** The {@code int} value representing the {@code abstract} modifier. */
    public static final int ABSTRACT = 0x00000400;
    /** The {@code int} value representing the {@code strictfp} modifier. */
    public static final int STRICT = 0x00000800;

    /** @param method will be inspected.
     * @return modifiers representing the method state. */
    public static int getModifiers(final Method method) {
        int modifiers = 0;
        if (method.isAbstract()) {
            modifiers |= ABSTRACT;
        }
        if (method.isFinal()) {
            modifiers |= FINAL;
        }
        if (method.isNative()) {
            modifiers |= NATIVE;
        }
        if (method.isPrivate()) {
            modifiers |= PRIVATE;
        }
        if (method.isProtected()) {
            modifiers |= PROTECTED;
        }
        if (method.isPublic()) {
            modifiers |= PUBLIC;
        }
        if (method.isStatic()) {
            modifiers |= STATIC;
        }
        return modifiers;
    }

    /** @param field will be inspected.
     * @return modifiers represending field's state. */
    public static int getModifiers(final Field field) {
        int modifiers = 0;
        if (field.isFinal()) {
            modifiers |= FINAL;
        }
        if (field.isPrivate()) {
            modifiers |= PRIVATE;
        }
        if (field.isProtected()) {
            modifiers |= PROTECTED;
        }
        if (field.isPublic()) {
            modifiers |= PUBLIC;
        }
        if (field.isStatic()) {
            modifiers |= STATIC;
        }
        if (field.isTransient()) {
            modifiers |= TRANSIENT;
        }
        if (field.isVolatile()) {
            modifiers |= VOLATILE;
        }
        return modifiers;
    }
}
