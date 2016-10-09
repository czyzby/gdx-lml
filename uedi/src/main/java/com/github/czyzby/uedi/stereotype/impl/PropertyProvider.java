package com.github.czyzby.uedi.stereotype.impl;

import java.lang.reflect.Member;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.uedi.stereotype.Default;
import com.github.czyzby.uedi.stereotype.Property;

/** Provides string properties using {@link Property} API.
 *
 * @author MJ */
public class PropertyProvider implements Default, StringProvider {
    private final ObjectMap<String, Property> properties = GdxMaps.newObjectMap();

    @Override
    public boolean hasProperty(final String key) {
        return properties.containsKey(key);
    }

    @Override
    public Property getProperty(final String key) {
        return properties.get(key);
    }

    @Override
    public void addProperty(final Property property) {
        properties.put(property.getKey(), property);
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }

    @Override
    public String provide(final Object target, final Member member) {
        final String key = Providers.getName(member);
        if (properties.containsKey(key)) {
            return properties.get(key).getValue();
        }
        return null;
    }
}
