package com.github.czyzby.gdx.idle;

import com.github.czyzby.autumn.annotation.method.Initiate;
import com.github.czyzby.autumn.annotation.stereotype.Configuration;
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
import com.github.czyzby.lml.util.Lml;

/** Application's configuration. Specifies UI skin, bundle path, preferences path and sound settings.
 *
 * @author MJ */
@Configuration
public class Config {
	public static final String PREFERENCES_PATH = "gdx.idle";

	@Skin(fonts = { "ui/antiqua.fnt", "ui/boxy.fnt" }, fontNames = { "default", "small" })
	private String skinPath = "ui/ui";

	@I18nBundle
	private String i18nBundlePath = "i18n/bundle";

	@LmlMacro
	private String macroPath = "templates/macros/macro.lml";

	@Preference
	private String preferencesPath = PREFERENCES_PATH;

	@I18nLocale(propertiesPath = PREFERENCES_PATH, defaultLocale = "en")
	private String localePreference = "locale";

	@AvailableLocales
	private String[] locales = new String[] { "en", "pl" };

	@SoundVolume(preferences = PREFERENCES_PATH, defaultVolume = 0.7f)
	private String soundVolumePreference = "soundVolume";

	@SoundEnabled(preferences = PREFERENCES_PATH)
	private String soundEnabledPreference = "soundOn";

	@MusicVolume(preferences = PREFERENCES_PATH)
	private String musicVolumePreference = "musicVolume";

	@MusicEnabled(preferences = PREFERENCES_PATH)
	private String musicEnabledPreference = "musicOn";

	@Initiate(priority = 5)
	@SuppressWarnings("static-method")
	private void initiate() {
		Lml.EXTRACT_FIELDS_AS_METHODS = false; // Problematic on GWT, see LML docs.
	}
}