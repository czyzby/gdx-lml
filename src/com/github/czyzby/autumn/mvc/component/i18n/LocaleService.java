package com.github.czyzby.autumn.mvc.component.i18n;

import java.lang.annotation.Annotation;
import java.util.Locale;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.preference.I18nLocale;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;
import com.github.czyzby.kiwi.util.gdx.preference.ApplicationPreferences;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;
import com.github.czyzby.kiwi.util.tuple.mutable.MutableSingle;

/** Manages i18n. Reads locale from preferences and allows to easily save it. Provides informations about
 * current locale.
 *
 * @author MJ */
@MetaComponent
public class LocaleService extends ComponentFieldAnnotationProcessor {
	/** Regex used to split locales extracted from preferences or string variables containing locales, and to
	 * convert {@link java.util.Locale} objects to strings. Defaults to "-". Can be changed globally, before
	 * context loading. */
	public static String DEFAULT_LOCALE_SEPARATOR = "-";
	/** Default locale used when no other value is given. Can be changed globally. */
	public static String DEFAULT_LANGUAGE = "en";

	@Inject
	private ContextContainer context;
	@Inject(lazy = InterfaceService.class)
	private Lazy<InterfaceService> interfaceService;

	private final MutableSingle<Locale> currentLocale = MutableSingle.of(null);
	private ObjectProvider<Locale> defaultLocaleProvider = new ObjectProvider<Locale>() {
		@Override
		public Locale provide() {
			return toLocale(DEFAULT_LANGUAGE);
		}
	};
	private Runnable doOnLocaleChange = new Runnable() {
		@Override
		public void run() {
			if (Strings.isNotEmpty(localePreferenceName) && Strings.isNotEmpty(localePreferencesPath)) {
				saveLocaleInPreferences();
			}
			interfaceService.get().reload();
		}
	};

	private String localePreferencesPath;
	private String localePreferenceName;

	/** @param locale string containing locale with parts separated with {@link #DEFAULT_LOCALE_SEPARATOR}
	 * @return locale constructed with the processed string.
	 * @throws AutumnRuntimeException if string is invalid. */
	public static Locale toLocale(final String locale) {
		final String[] localeParts = locale.split(DEFAULT_LOCALE_SEPARATOR);
		switch (localeParts.length) {
			case 3:
				return new Locale(localeParts[0], localeParts[1], localeParts[2]);
			case 2:
				return new Locale(localeParts[0], localeParts[1]);
			case 1:
				return new Locale(localeParts[0]);
			default:
				throw new AutumnRuntimeException("Invalid locale preference. Received: " + locale);
		}
	}

	/** @param locale will be converted to string.
	 * @return string with locale data separated with {@link #DEFAULT_LOCALE_SEPARATOR}; */
	public static String fromLocale(final Locale locale) {
		final StringBuilder localeBuilder = new StringBuilder();
		localeBuilder.append(locale.getLanguage());
		if (Strings.isNotEmpty(locale.getCountry())) {
			localeBuilder.append(DEFAULT_LOCALE_SEPARATOR).append(locale.getCountry());
			if (Strings.isNotEmpty(locale.getVariant())) {
				localeBuilder.append(DEFAULT_LOCALE_SEPARATOR).append(locale.getVariant());
			}
		}
		return localeBuilder.toString();
	}

	/** @return current locale of the application. */
	public Locale getCurrentLocale() {
		if (!currentLocale.isPresent()) {
			currentLocale.set(defaultLocaleProvider.provide());
		}
		return currentLocale.get();
	}

	/** @param currentLocale will become the current locale of the application. Triggers on local change action.
	 * @see #setActionOnLocaleChange(Runnable) */
	public void setCurrentLocale(final Locale currentLocale) {
		if (!currentLocale.equals(this.currentLocale.get())) {
			this.currentLocale.set(currentLocale);
			if (doOnLocaleChange != null) {
				doOnLocaleChange.run();
			}
		}
	}

	/** @param doOnLocaleChange if not null, will be executed each time locale is changed. Defaults to reloading
	 *            the screens with {@link com.github.czyzby.autumn.mvc.component.ui.InterfaceService#reload()}
	 *            method. */
	public void setActionOnLocaleChange(final Runnable doOnLocaleChange) {
		this.doOnLocaleChange = doOnLocaleChange;
	}

	/** @param defaultLocaleProvider provides default locale if it is not defined in properties. */
	public void setDefaultLocaleProvider(final ObjectProvider<Locale> defaultLocaleProvider) {
		this.defaultLocaleProvider = defaultLocaleProvider;
	}

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return I18nLocale.class;
	}

	@Override
	public <Type> void processField(final ContextContainer context, final ContextComponent component,
			final Field field) {
		try {
			final I18nLocale localeData = Reflection.getAnnotation(field, I18nLocale.class);
			final Object locale = Reflection.getFieldValue(field, component.getComponent());
			if (locale instanceof Locale) {
				currentLocale.set((Locale) locale);
			} else if (locale instanceof String) {
				extractLocaleFromAnnotatedString(localeData, (String) locale);
			}
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to extract application's locale.", exception);
		}

	}

	private void extractLocaleFromAnnotatedString(final I18nLocale localeData, final String locale) {
		if (Strings.isEmpty(localeData.propertiesPath())) {
			currentLocale.set(toLocale(locale));
		} else {
			localePreferencesPath = localeData.propertiesPath();
			localePreferenceName = locale;
			loadLocaleFromPreferences(localeData);
		}
	}

	private void loadLocaleFromPreferences(final I18nLocale localeData) {
		final Preferences localePreferences = ApplicationPreferences.getPreferences(localePreferencesPath);
		currentLocale.set(getLocaleFromPreferences(localePreferences, localePreferenceName,
				localeData.defaultLocale()));
	}

	private static Locale getLocaleFromPreferences(final Preferences localePreferences,
			final String preferenceName, final String defaultLocale) {
		final String locale = localePreferences.getString(preferenceName, defaultLocale);
		return toLocale(locale);
	}

	/** Saves current locale in the preferences. Note that a string configuration variable with name of a
	 * preference has to be annotated with
	 * {@link com.github.czyzby.autumn.mvc.stereotype.preference.I18nLocale} and given a proper path to
	 * preferences for this method to work - otherwise it cannot determine path to the preference.
	 *
	 * @see #saveLocaleInPreferences(String, String) */
	public void saveLocaleInPreferences() {
		saveLocaleInPreferences(localePreferencesPath, localePreferenceName);
	}

	/** Saves current locale in the selected preferences.
	 *
	 * @param preferencesPath used to retrieve preferences with
	 *            {@link com.github.czyzby.kiwi.util.gdx.preference.ApplicationPreferences#getPreferences(String)}
	 *            method.
	 * @param preferenceName name of the locale setting in the preferences. */
	public void saveLocaleInPreferences(final String preferencesPath, final String preferenceName) {
		if (Strings.isEmpty(preferencesPath) || Strings.isEmpty(preferenceName)) {
			throw new AutumnRuntimeException(
					"Preference path and name cannot be empty! These are set automatically if you annotate a path to preference with @I18nBundle and pass a corrent path to the preferences.");
		}
		final Preferences preferences = ApplicationPreferences.getPreferences(preferencesPath);
		preferences.putString(preferenceName, fromLocale(currentLocale.get()));
		preferences.flush();
	}

	/** Utility method. Provides direct access to default {@link com.badlogic.gdx.utils.I18NBundle}.
	 *
	 * @return bundle stored in LML parser with the default key. Might be null. */
	public I18NBundle getI18nBundle() {
		return interfaceService.get().getParser().getDefaultI18nBundle();
	}

	/** Utility method. Provides direct access to {@link com.badlogic.gdx.utils.I18NBundle} with selected name.
	 *
	 * @param forName ID of the bundle as it appears in the LML parser.
	 * @return bundle stored in LML parser with the selected key. Might be null. */
	public I18NBundle getI18nBundle(final String forName) {
		return interfaceService.get().getParser().getI18nBundle(forName);
	}
}