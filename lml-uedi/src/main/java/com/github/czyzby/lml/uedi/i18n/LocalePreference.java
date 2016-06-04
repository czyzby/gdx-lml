package com.github.czyzby.lml.uedi.i18n;

import java.lang.reflect.Member;
import java.util.Locale;

import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.preference.ApplicationPreferences;
import com.github.czyzby.lml.uedi.preferences.impl.AbstractPreference;
import com.github.czyzby.uedi.stereotype.Provider;

/** Manages application's locale preference. Reloads i18n bundles on preference change.
 *
 * @author MJ */
public class LocalePreference extends AbstractPreference implements Provider<Locale> {
    private final I18NBundleProvider i18nBundleProvider;
    private Locale locale;

    /** Constructs locale preference with the default preferences object.
     *
     * @param i18nBundleProvider will be used to reload i18n bundles on locale change. */
    public LocalePreference(final I18NBundleProvider i18nBundleProvider) {
        super(ApplicationPreferences.getPreferences());
        locale = fromString(getValue());
        this.i18nBundleProvider = i18nBundleProvider;
    }

    @Override
    public String getKey() {
        return "locale";
    }

    @Override
    public String getDefault() {
        return "en";
    }

    @Override
    public Class<? extends Locale> getType() {
        return Locale.class;
    }

    @Override
    public Locale provide(final Object target, final Member member) {
        return locale;
    }

    @Override
    public String setValue(final String value) {
        final String previous = super.setValue(Strings.isEmpty(value) ? getDefault() : value);
        if (!Strings.equals(previous, value)) {
            setLocale(fromString(value));
        }
        return previous;
    }

    /** Reloads {@link com.badlogic.gdx.utils.I18NBundle i18n bundles} managed by the application if the locale changed.
     *
     * @param locale will become current locale. */
    public void setLocale(final Locale locale) {
        if (!equals(this.locale, locale)) {
            this.locale = locale;
            i18nBundleProvider.reloadBundles(locale);
        }
    }

    /** @return current locale used by the {@link com.badlogic.gdx.utils.I18NBundle i18n bundles}. */
    public Locale getLocale() {
        return locale;
    }

    /** @param locale will be checked.
     * @return true if the passed locale is current application's locale. */
    public boolean isCurrent(final Locale locale) {
        return equals(this.locale, locale);
    }

    /** A safe, cross-platform way of converting serialized locale string to {@link Locale} instance. Matches
     * {@link #toString(Locale)} serialization implementation.
     *
     * @param locale locale converted to string.
     * @return {@link Locale} stored the deserialized data. */
    public static Locale fromString(final String locale) {
        final String[] data = Strings.split(locale, '_');
        if (data.length == 1) {
            return new Locale(data[0]);
        } else if (data.length == 2) {
            return new Locale(data[0], data[1]);
        } else if (data.length == 3) {
            return new Locale(data[0], data[1], data[2]);
        }
        throw new IllegalArgumentException("Invalid locale string: " + locale);
    }

    /** A safe, cross-platform way of converting {@link Locale} to string. Matches {@link #fromString(String)}
     * deserialization implementation.
     *
     * @param locale will be serialized to string.
     * @return passed locale serialized as string using '_' to separate locale parts. */
    public static String toString(final Locale locale) {
        // String language, String country, String variant
        if (Strings.isEmpty(locale.getCountry())) {
            return locale.getLanguage();
        } else if (Strings.isEmpty(locale.getVariant())) {
            return locale.getLanguage() + '_' + locale.getCountry();
        }
        return locale.getLanguage() + '_' + locale.getCountry() + "_" + locale.getVariant();
    }

    /** Safe cross-platform way of comparing locales.
     *
     * @param localeA will be compared.
     * @param localeB will be compared.
     * @return true if the passed locales are the same object or represent the same locale. */
    public static boolean equals(final Locale localeA, final Locale localeB) {
        return localeA == localeB || Strings.equals(localeA.getLanguage(), localeB.getLanguage())
                && Strings.equals(localeA.getCountry(), localeB.getCountry())
                && Strings.equals(localeA.getVariant(), localeB.getVariant());
    }
}
