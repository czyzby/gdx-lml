package com.github.czyzby.lml.uedi.preferences;

import java.lang.reflect.Member;

import com.badlogic.gdx.Preferences;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.preference.ApplicationPreferences;
import com.github.czyzby.uedi.stereotype.Provider;

/** Provides {@link Preferences} instances.
 *
 * @author MJ */
public class PreferencesProvider implements Provider<Preferences> {
    @Override
    public Class<? extends Preferences> getType() {
        return Preferences.class;
    }

    @Override
    public Preferences provide(final Object target, final Member member) {
        return member == null || Strings.isEmpty(member.getName()) ? getPreferences()
                : getPreferences(member.getName());
    }

    /** @return the default {@link Preferences} instance. */
    public Preferences getPreferences() {
        return ApplicationPreferences.getPreferences();
    }

    /** @param path unique ID of the preferences.
     * @return {@link Preferences} instance with the selected path. */
    public Preferences getPreferences(final String path) {
        return ApplicationPreferences.getPreferences(path);
    }
}
