package com.github.czyzby.lml.uedi.collections;

import java.lang.reflect.Member;

import com.github.czyzby.kiwi.util.gdx.collection.pooled.PooledList;
import com.github.czyzby.uedi.stereotype.Provider;

/** Provides {@link PooledList} instances without reflection usage.
 *
 * @author MJ
 *
 * @param <Type> type of stored values. */
public class ListProvider<Type> implements Provider<PooledList<Type>> {
    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends PooledList<Type>> getType() {
        return (Class<? extends PooledList<Type>>) (Object) PooledList.class;
    }

    @Override
    public PooledList<Type> provide(final Object target, final Member member) {
        return new PooledList<Type>();
    }
}
