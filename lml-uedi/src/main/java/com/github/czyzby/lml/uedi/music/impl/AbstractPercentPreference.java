package com.github.czyzby.lml.uedi.music.impl;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.github.czyzby.kiwi.util.gdx.preference.ApplicationPreferences;
import com.github.czyzby.lml.uedi.preferences.impl.AbstractPreference;

/** Allows to manage a float value in range of [0, 1].
 *
 * @author MJ */
public abstract class AbstractPercentPreference extends AbstractPreference {
    private float percent;

    /** Uses default preferences. */
    public AbstractPercentPreference() {
        this(ApplicationPreferences.getPreferences());
    }

    /** @param preferences will be used to store the setting. */
    public AbstractPercentPreference(final Preferences preferences) {
        super(preferences);
        percent = MathUtils.clamp(Float.parseFloat(getValue()), 0f, 1f);
    }

    @Override
    public String getDefault() {
        return String.valueOf(1f);
    }

    @Override
    public String setValue(final String value) {
        percent = MathUtils.clamp(Float.parseFloat(value), 0f, 1f);
        return super.setValue(String.valueOf(percent));
    }

    /** @param percent will become current setting value. */
    public void setPercent(final float percent) {
        this.percent = MathUtils.clamp(percent, 0f, 1f);
        setValue(String.valueOf(this.percent));
    }

    /** @return current preference value. */
    public float getPercent() {
        return percent;
    }
}
