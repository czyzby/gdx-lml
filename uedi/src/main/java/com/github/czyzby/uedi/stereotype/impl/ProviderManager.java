package com.github.czyzby.uedi.stereotype.impl;

import java.lang.reflect.Member;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.stereotype.Provider;

/** Allows to resolve ambiguous dependencies.
 *
 * @author MJ
 *
 * @param <Type> type of provided objects. */
public class ProviderManager<Type> implements Provider<Type> {
    private final ObjectMap<String, Provider<Type>> providers = GdxMaps.newObjectMap();
    private final Class<Type> type;
    private final Context context;
    private Provider<Type> defaultProvider;

    /** @param type type of provided values.
     * @param context parent context. Used to resolve depedendencies. */
    public ProviderManager(final Class<Type> type, final Context context) {
        this.type = type;
        this.context = context;
    }

    /** @param provider will be included in the providers collection. Must provide the same type of values. */
    @SuppressWarnings("unchecked")
    public void addProvider(final Provider<?> provider) {
        providers.put(Providers.getName(provider), (Provider<Type>) provider);
        if (Providers.isDefault(provider)) {
            defaultProvider = (Provider<Type>) provider;
        }
    }

    @Override
    public Class<? extends Type> getType() {
        return type;
    }

    @Override
    public Type provide(final Object target, final Member member) {
        if (member != null) {
            final Provider<Type> provider = providers.get(Providers.getName(member));
            if (provider != null) {
                return provider.provide(target, member);
            }
        }
        if (defaultProvider == null) {
            if (context.isFailIfAmbiguousDependency() || context.isFailIfUnknownType()) {
                throw new RuntimeException("Ambiguous dependency: '" + target + "' component requested '"
                        + type.getName() + "' instance, found multiple providers and no default one.");
            }
            return context.create(type);
        }
        return defaultProvider.provide(target, member);
    }
}
