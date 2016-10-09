package com.github.czyzby.lml.uedi.collections;

import java.lang.reflect.Member;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.uedi.stereotype.Provider;

/** Provides {@link ObjectMap} instances without reflection usage.
 *
 * @author MJ
 *
 * @param <Key> type of stored keys.
 * @param <Value> type of stored values. */
public class MapProvider<Key, Value> implements Provider<ObjectMap<Key, Value>> {
    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends ObjectMap<Key, Value>> getType() {
        return (Class<? extends ObjectMap<Key, Value>>) (Object) ObjectMap.class;
    }

    @Override
    public ObjectMap<Key, Value> provide(final Object target, final Member member) {
        return new ObjectMap<Key, Value>();
    }
}
