package com.github.czyzby.gdx.idle;

import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.mvc.stereotype.preference.AvailableLocales;
import com.github.czyzby.autumn.mvc.stereotype.preference.I18nBundle;
import com.github.czyzby.autumn.mvc.stereotype.preference.I18nLocale;
import com.github.czyzby.autumn.mvc.stereotype.preference.LmlMacro;
import com.github.czyzby.autumn.mvc.stereotype.preference.Preference;
import com.github.czyzby.autumn.mvc.stereotype.preference.Skin;
import com.github.czyzby.autumn.mvc.stereotype.preference.sfx.MusicEnabled;
import com.github.czyzby.autumn.mvc.stereotype.preference.sfx.MusicVolume;
import com.github.czyzby.autumn.mvc.stereotype.preference.sfx.SoundEnabled;
import com.github.czyzby.autumn.mvc.stereotype.preference.sfx.SoundVolume;

/** Application's configuration. Specifies UI skin, bundle path, preferences path and sound settings.
 *
 * @author MJ */
@Component
public class Config {
    public static final String PREFERENCES_PATH = "gdx.idle";

    @Skin(fonts = { "ui/antiqua.fnt", "ui/boxy.fnt" },
            fontNames = { "default", "small" }) private final String skinPath = "ui/ui";

    @I18nBundle private final String i18nBundlePath = "i18n/bundle";

    @LmlMacro private final String macroPath = "templates/macros/macro.lml";

    @Preference private final String preferencesPath = PREFERENCES_PATH;

    @I18nLocale(propertiesPath = PREFERENCES_PATH,
            defaultLocale = "en") private final String localePreference = "locale";

    @AvailableLocales private final String[] locales = new String[] { "en", "pl" };

    @SoundVolume(preferences = PREFERENCES_PATH,
            defaultVolume = 0.7f) private final String soundVolumePreference = "soundVolume";

    @SoundEnabled(preferences = PREFERENCES_PATH) private final String soundEnabledPreference = "soundOn";

    @MusicVolume(preferences = PREFERENCES_PATH) private final String musicVolumePreference = "musicVolume";

    @MusicEnabled(preferences = PREFERENCES_PATH) private final String musicEnabledPreference = "musicOn";
}