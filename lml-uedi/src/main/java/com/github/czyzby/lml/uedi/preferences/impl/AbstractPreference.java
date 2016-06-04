package com.github.czyzby.lml.uedi.preferences.impl;

import com.badlogic.gdx.Preferences;
import com.github.czyzby.kiwi.util.gdx.preference.ApplicationPreferences;
import com.github.czyzby.uedi.stereotype.Property;

/** Simple base for properties maintained by {@link Preferences}.
 *
 * @author MJ */
public abstract class AbstractPreference implements Property {
    private final Preferences preferences;

    /** Constructs a new preference using the default application's {@link Preferences}. */
    public AbstractPreference() {
        this(ApplicationPreferences.getPreferences());
    }

    /** @param preferences will be used to extract and save the preference. */
    public AbstractPreference(final Preferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public String getValue() {
        return preferences.getString(getKey(), getDefault());
    }

    @Override
    public String setValue(final String value) {
        final String previous = getValue();
        preferences.putString(getKey(), value);
        return previous;
    }

    /** @return default preference value. */
    public abstract String getDefault();

    /** Forces flushing of the preferences. */
    public void save() {
        preferences.flush();
    }
}
