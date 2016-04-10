package com.github.czyzby.kiwi.util.gdx.asset.lazy.provider;

import com.badlogic.gdx.utils.Array;

/** Utility implementation of ObjectProvider that produces regular or, when constructed with class object, typed arrays.
 * Does not rely on reflection.
 *
 * @author MJ */
public class ArrayObjectProvider<Type> implements ObjectProvider<Array<Type>> {
    private final Class<Type> arrayType;

    /** Produces regular arrays. Note that the object is stateless and immutable, so one instance per application can be
     * used. */
    public ArrayObjectProvider() {
        arrayType = null;
    }

    /** Produces typed arrays. */
    public ArrayObjectProvider(final Class<Type> arrayType) {
        this.arrayType = arrayType;
    }

    @Override
    public Array<Type> provide() {
        if (arrayType != null) {
            return new Array<Type>(arrayType);
        }
        return new Array<Type>();
    }

    /** Produces regular arrays. */
    public static <Type> ArrayObjectProvider<Type> getProvider() {
        return new ArrayObjectProvider<Type>();
    }

    /** Produces typed arrays. */
    public static <Type> ArrayObjectProvider<Type> getProvider(final Class<Type> arrayType) {
        return new ArrayObjectProvider<Type>(arrayType);
    }
}
