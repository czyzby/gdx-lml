package com.github.czyzby.kiwi.util.gdx.preference;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.asset.Asset;

/** Allows to easily access game's preferences. Contains cached accessed preferences.
 *
 * @author MJ */
public class ApplicationPreferences {
	private ApplicationPreferences() {
	}

	/** Caches all accessed preferences. */
	private final static ObjectMap<String, Preferences> PREFERENCES = new ObjectMap<String, Preferences>();
	/** If set, returns preferences with this path. */
	private static String defaultPreferences;

	/** @return default user's preferences of the application. Note that default preferences had to be set
	 *         before calling this method. */
	public static Preferences getPreferences() {
		if (defaultPreferences == null) {
			throw new IllegalStateException(
					"Default preferences path was not set. Cannot access default preferences.");
		}
		return getPreferences(defaultPreferences);
	}

	/** @param preferenceAsset will be set as the default preferences file. Preferences connected with this
	 *            asset will be returned by the no parameter method. */
	public static void setDefaultPreferences(final Asset preferenceAsset) {
		setDefaultPreferences(preferenceAsset.getPath());
	}

	/** @param preferencePath will be set as the default preferences file path. Preferences connected with this
	 *            asset will be returned by the no parameter method. */
	public static void setDefaultPreferences(final String preferencePath) {
		defaultPreferences = preferencePath;
	}

	/** @return preferences with the selected path. Will be cached in map - the next access returns the same
	 *         object. */
	public static Preferences getPreferences(final String preferencePath) {
		if (preferencePath == null) {
			throw new IllegalArgumentException("Path cannot be empty.");
		}
		if (PREFERENCES.containsKey(preferencePath)) {
			return PREFERENCES.get(preferencePath);
		} else {
			final Preferences preferences = Gdx.app.getPreferences(preferencePath);
			PREFERENCES.put(preferencePath, preferences);
			return preferences;
		}
	}

	/** @return preferences with the selected path. Will be cached in map - the next access returns the same
	 *         object. */
	public static Preferences getPreferences(final Asset preference) {
		return getPreferences(preference.getPath());
	}

	/** Saves all preferences currently cached in the map. */
	public static void saveAllPreferences() {
		for (final Preferences preferences : PREFERENCES.values()) {
			save(preferences);
		}
	}

	/** @param preferences will be flushed. */
	public static void save(final Preferences preferences) {
		preferences.flush();
	}
}
