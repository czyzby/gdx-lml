package com.github.czyzby.lml.uedi.collections;

import java.lang.reflect.Member;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.uedi.stereotype.Provider;

/** Provides {@link Array} instances without reflection usage.
 *
 * @author MJ
 *
 * @param <Type> type of stored values. */
public class ArrayProvider<Type> implements Provider<Array<Type>> {
    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Array<Type>> getType() {
        return (Class<? extends Array<Type>>) (Object) Array.class;
    }

    @Override
    public Array<Type> provide(final Object target, final Member member) {
        return new Array<Type>();
    }
}
