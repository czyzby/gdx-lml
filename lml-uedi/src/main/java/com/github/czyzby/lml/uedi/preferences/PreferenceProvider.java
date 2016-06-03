package com.github.czyzby.lml.uedi.preferences;

import java.lang.reflect.Member;

import com.badlogic.gdx.Preferences;
import com.github.czyzby.uedi.stereotype.Property;
import com.github.czyzby.uedi.stereotype.impl.PropertyProvider;

/** Default {@link String} provider. Stores and retrieves preferences from {@link Preferences} object specific to each
 * platform.
 *
 * @author MJ */
public class PreferenceProvider extends PropertyProvider {
    private final Preferences preferences;

    /** @param preferences will be used to store the properties. */
    public PreferenceProvider(final Preferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public String provide(final Object target, final Member member) {
        final String property = super.provide(target, member);
        return property == null ? preferences.getString(member.getName(), null) : property;
    }

    @Override
    public boolean hasProperty(final String key) {
        return super.hasProperty(key) || preferences.contains(key);
    }

    @Override
    public Property getProperty(final String key) {
        final Property property = super.getProperty(key);
        return property != null ? property : new AbstractPreference() {
            @Override
            public String getDefault() {
                return null;
            }

            @Override
            public String getKey() {
                return key;
            }
        };
    }

    /** Saves all currently stored preferences. */
    public void save() {
        preferences.flush();
    }
}
