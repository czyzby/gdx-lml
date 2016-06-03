package com.github.czyzby.uedi.stereotype.impl;

import java.lang.reflect.Member;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.common.Exceptions;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.preference.ApplicationPreferences;
import com.github.czyzby.uedi.stereotype.Default;
import com.github.czyzby.uedi.stereotype.Property;

/** Provides string properties using {@link Property} API. When property for the given string is not present, fallbacks
 * to values from main application {@link Preferences}.
 *
 * @author MJ */
public class PropertyProvider implements Default, StringProvider {
    private final ObjectMap<String, Property> properties = GdxMaps.newObjectMap();
    private final Preferences preferences = getPreferences();

    protected Preferences getPreferences() {
        try {
            return ApplicationPreferences.getPreferences();
        } catch (final Exception exception) {
            Exceptions.ignore(exception);
            return null;
        }
    }

    @Override
    public boolean hasProperty(final String key) {
        return properties.containsKey(key) || preferences != null && preferences.contains(key);
    }

    @Override
    public Property getProperty(final String key) {
        if (properties.containsKey(key)) {
            return properties.get(key);
        }
        return new Property() {
            @Override
            public String setValue(final String value) {
                final String previous = preferences.getString(key);
                preferences.putString(key, value);
                return previous;
            }

            @Override
            public String getValue() {
                return preferences.getString(key);
            }

            @Override
            public String getKey() {
                return key;
            }
        };
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
        return preferences.getString(key, null);
    }
}
