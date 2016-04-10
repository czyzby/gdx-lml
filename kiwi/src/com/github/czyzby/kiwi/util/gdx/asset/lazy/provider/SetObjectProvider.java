package com.github.czyzby.kiwi.util.gdx.asset.lazy.provider;

import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;

/** Utility implementation of ObjectProvider that produces object sets. Does not rely on reflection. Note that the
 * object is stateless and immutable, so one instance per application can be used.
 *
 * @author MJ */
public class SetObjectProvider<Type> implements ObjectProvider<ObjectSet<Type>> {
    @Override
    public ObjectSet<Type> provide() {
        return new ObjectSet<Type>();
    }

    /** Produces object sets. */
    public static <Type> SetObjectProvider<Type> getProvider() {
        return new SetObjectProvider<Type>();
    }
}