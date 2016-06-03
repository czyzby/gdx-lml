package com.github.czyzby.lml.uedi.i18n.impl;

import java.util.Locale;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

/** Allows to eagerly load {@link I18NBundle} instances. Thread-safe and prepared to load multiple bundles at once,
 * contrary to the default loader.
 *
 * @author MJ
 * @see com.badlogic.gdx.assets.loaders.I18NBundleLoader */
public class EagerI18NBundleLoader extends AsynchronousAssetLoader<I18NBundle, I18NBundleLoader.I18NBundleParameter> {
    private final ObjectMap<String, I18NBundle> bundles = GdxMaps.newObjectMap();

    /** @param resolver will be used to load bundle files. */
    public EagerI18NBundleLoader(final FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(final AssetManager manager, final String fileName, final FileHandle file,
            final I18NBundleLoader.I18NBundleParameter parameter) {
        I18NBundle bundle;
        if (parameter instanceof EagerI18NBundleParameter && ((EagerI18NBundleParameter) parameter).bundle != null) {
            return;
        } else if (parameter.encoding == null) {
            bundle = I18NBundle.createBundle(file, parameter.locale);
        } else {
            bundle = I18NBundle.createBundle(file, parameter.locale, parameter.encoding);
        }
        synchronized (bundles) {
            bundles.put(fileName, bundle);
        }
    }

    @Override
    public I18NBundle loadSync(final AssetManager manager, final String fileName, final FileHandle file,
            final I18NBundleLoader.I18NBundleParameter parameter) {
        if (parameter instanceof EagerI18NBundleParameter && ((EagerI18NBundleParameter) parameter).bundle != null) {
            return ((EagerI18NBundleParameter) parameter).bundle;
        }
        final I18NBundle bundle;
        synchronized (bundles) {
            bundle = bundles.remove(fileName);
        }
        return bundle;
    }

    @Override
    @SuppressWarnings("rawtypes") // Raw types due to ugly API.
    public Array<AssetDescriptor> getDependencies(final String fileName, final FileHandle file,
            final I18NBundleLoader.I18NBundleParameter parameter) {
        return null;
    }

    /** See {@link com.badlogic.gdx.assets.loaders.I18NBundleLoader.I18NBundleParameter}.
     *
     * @author davebaol
     * @author MJ */
    public static class EagerI18NBundleParameter
            extends com.badlogic.gdx.assets.loaders.I18NBundleLoader.I18NBundleParameter {
        private final I18NBundle bundle;

        /** Loads the bundle with default locale and encoding. */
        public EagerI18NBundleParameter() {
            this(null, null);
        }

        /** @param locale determines bundle locale. */
        public EagerI18NBundleParameter(final Locale locale) {
            this(locale, null);
        }

        /** @param locale determines bundle locale.
         * @param encoding determines how files are loaded. */
        public EagerI18NBundleParameter(final Locale locale, final String encoding) {
            this(locale == null ? Locale.getDefault() : locale, encoding, null);
        }

        /** @param bundle eagerly loaded. Will be immediately returned by the loader. Allows to have a managed bundle
         *            without actually loading it asynchronously with an asset manager. */
        public EagerI18NBundleParameter(final I18NBundle bundle) {
            this(null, null, bundle);
        }

        private EagerI18NBundleParameter(final Locale locale, final String encoding, final I18NBundle bundle) {
            super(locale, encoding);
            this.bundle = bundle;
        }
    }
}