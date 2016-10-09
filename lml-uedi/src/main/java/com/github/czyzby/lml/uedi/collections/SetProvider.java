package com.github.czyzby.lml.uedi.collections;

import java.lang.reflect.Member;

import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.uedi.stereotype.Provider;

/** Provides {@link ObjectSet} instances without reflection usage.
 *
 * @author MJ
 *
 * @param <Type> type of stored values. */
public class SetProvider<Type> implements Provider<ObjectSet<Type>> {
    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends ObjectSet<Type>> getType() {
        return (Class<? extends ObjectSet<Type>>) (Object) ObjectSet.class;
    }

    @Override
    public ObjectSet<Type> provide(final Object target, final Member member) {
        return new ObjectSet<Type>();
    }
}
