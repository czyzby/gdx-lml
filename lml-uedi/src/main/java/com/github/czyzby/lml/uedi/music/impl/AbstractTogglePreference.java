package com.github.czyzby.lml.uedi.music.impl;

import com.badlogic.gdx.Preferences;
import com.github.czyzby.kiwi.util.gdx.preference.ApplicationPreferences;
import com.github.czyzby.lml.uedi.preferences.impl.AbstractPreference;

/** Allows to manage a boolean value.
 *
 * @author MJ */
public abstract class AbstractTogglePreference extends AbstractPreference {
    private boolean on;

    /** Uses default preferences. */
    public AbstractTogglePreference() {
        this(ApplicationPreferences.getPreferences());
    }

    /** @param preferences will be used to store the setting. */
    public AbstractTogglePreference(final Preferences preferences) {
        super(preferences);
        on = Boolean.parseBoolean(getValue());
    }

    @Override
    public String getDefault() {
        return String.valueOf(true);
    }

    @Override
    public String setValue(final String value) {
        on = Boolean.parseBoolean(value);
        return super.setValue(value);
    }

    /** @param on turns the setting on or off. */
    public void setOn(final boolean on) {
        this.on = on;
        setValue(String.valueOf(on));
    }

    /** Sets the current value to its opposite. */
    public void toggle() {
        setOn(!on);
    }

    /** @return true if the setting is currently on, false if off. */
    public boolean isOn() {
        return on;
    }
}
