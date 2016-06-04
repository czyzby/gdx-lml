package com.github.czyzby.lml.uedi.i18n;

import java.lang.reflect.Member;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.common.Exceptions;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.uedi.assets.Loaded;
import com.github.czyzby.lml.uedi.assets.impl.AbstractAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;
import com.github.czyzby.lml.uedi.i18n.impl.BundleInjection;
import com.github.czyzby.lml.uedi.i18n.impl.EagerI18NBundleLoader.EagerI18NBundleParameter;
import com.github.czyzby.uedi.reflection.impl.FieldMember;

/** Provides {@link I18NBundle} instances. Automatically adds bundles to {@link LmlParser}.
 *
 * @author MJ */
public class I18NBundleProvider extends AbstractAssetProvider<I18NBundle> {
    /** This is the initial default bundle name, which will be set as the main bundle in {@link LmlParser}. */
    public static final String DEFAULT_BUNDLE = "nls";
    public static final String[] EXTENSIONS = new String[] { "" };
    private final ObjectMap<String, I18NBundle> bundles = GdxMaps.newObjectMap();
    private final ObjectMap<String, Array<BundleInjection>> bundlesData = LazyObjectMap.newMapOfArrays();
    private LocalePreference localePreference;
    private LmlParser parser;
    private String encoding = "UTF-8";
    private String defaultBundle = DEFAULT_BUNDLE;

    /** @param assetManager will be used to load the textures. */
    public I18NBundleProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    /** @param localePreference will be used to determine current {@link Locale}. */
    public void setLocalePreference(final LocalePreference localePreference) {
        this.localePreference = localePreference;
    }

    /** @return preference used to determine current {@link Locale}. */
    public LocalePreference getLocalePreference() {
        return localePreference;
    }

    /** @return default bundle name, which will be set as the main bundle in {@link LmlParser}. */
    public String getDefaultBundle() {
        return defaultBundle;
    }

    /** @param defaultBundle default bundle name, which will be set as the main bundle in {@link LmlParser}. */
    public void setDefaultBundle(final String defaultBundle) {
        this.defaultBundle = defaultBundle;
    }

    @Override
    public I18NBundle provide(final Object target, final Member member) {
        if (member == null) {
            throwUnknownPathException();
        }
        final String id = member.getName();
        if (id.isEmpty()) {
            throwUnknownPathException();
        }
        final I18NBundle asset = getOrLoad(id);
        if (member instanceof FieldMember) {
            final BundleInjection injection = new BundleInjection(determinePath(id), ((FieldMember) member).getField(),
                    target);
            if (target instanceof Loaded) {
                ((Loaded) target).onLoad(determinePath(id), I18NBundle.class, asset);
            }
            bundlesData.get(id).add(injection);
        }
        return asset;
    }

    @Override
    public Class<? extends I18NBundle> getType() {
        return I18NBundle.class;
    }

    @Override
    public I18NBundle getOrLoad(final String id) {
        if (bundles.containsKey(id)) {
            return bundles.get(id);
        }
        final String path = determinePath(id);
        final I18NBundle bundle = I18NBundle.createBundle(Gdx.files.internal(path), localePreference.getLocale(),
                encoding);
        bundles.put(id, bundle);
        final EagerI18NBundleParameter parameters = new EagerI18NBundleParameter(bundle);
        getAssetManager().load(path, I18NBundle.class, parameters);
        return bundle;
    }

    /** @return encoding used to read {@link I18NBundle} files. Defaults to UTF-8. */
    public String getEncoding() {
        return encoding;
    }

    /** @param encoding will be used to read {@link I18NBundle} files. */
    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }

    @Override
    protected String getFileName(final String folder, final String id) {
        final StringBuilder builder = new StringBuilder(folder.length() + 1 + id.length());
        builder.append(folder);
        builder.append('/');
        builder.append(id);
        Strings.replace(builder, '_', '/');
        return builder.toString(); // No extension, no dot required.
    }

    /** Should be called after bundles are loaded.
     *
     * @param parser will have the bundles attached in its parsing data. Will be updated on bundle reloads. */
    public void fill(final LmlParser parser) {
        this.parser = parser;
        for (final Entry<String, I18NBundle> bundle : bundles) {
            parser.getData().addI18nBundle(bundle.key, bundle.value);
            if (defaultBundle.equals(bundle.key)) {
                parser.getData().setDefaultI18nBundle(bundle.value);
            }
        }
    }

    /** Reloads all managed {@link I18NBundle} instances according to the current state of {@link LocalePreference}. */
    public void reloadBundles() {
        reloadBundles(localePreference.getLocale());
    }

    /** @param locale will be used to reload all managed {@link I18NBundle} instances. */
    public void reloadBundles(final Locale locale) {
        final AssetManager assetManager = getAssetManager();
        for (final String id : bundles.keys()) {
            final String path = determinePath(id);
            try {
                assetManager.unload(path);
            } catch (final Exception exception) {
                Exceptions.ignore(exception); // Asset not loaded. Somewhat expected.
            }
            final I18NBundle bundle = I18NBundle.createBundle(Gdx.files.internal(path), locale, encoding);
            bundles.put(id, bundle);
            final EagerI18NBundleParameter parameters = new EagerI18NBundleParameter(bundle);
            assetManager.load(path, I18NBundle.class, parameters);
            assetManager.finishLoadingAsset(path);
            for (final BundleInjection injection : bundlesData.get(id)) {
                injection.inject(bundle);
            }
            parser.getData().addI18nBundle(id, bundle);
            if (defaultBundle.equals(id)) {
                parser.getData().setDefaultI18nBundle(bundle);
            }
        }
    }

    @Override
    protected String getFolder() {
        return "i18n";
    }

    @Override
    protected String[] getExtensions() {
        return EXTENSIONS;
    }
}
