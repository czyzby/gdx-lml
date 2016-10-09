package com.github.czyzby.lml.uedi.assets.impl;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.czyzby.kiwi.util.gdx.collection.pooled.PooledList;
import com.github.czyzby.lml.uedi.i18n.impl.EagerI18NBundleLoader;
import com.github.czyzby.lml.uedi.ui.impl.EagerSkinLoader;

/** Hooks up a semi-listener to {@link #update()} method, allowing to inject into fields as soon as assets are loaded.
 *
 * @author MJ */
public class InjectingAssetManager extends AssetManager {
    private final PooledList<AssetInjection> injections = PooledList.newList();

    /** Creates a new {@link InjectingAssetManager} using the {@link InternalFileHandleResolver}. */
    public InjectingAssetManager() {
        super(new InternalFileHandleResolver(), true); // Using default loaders.
        final FileHandleResolver resolver = getFileHandleResolver();
        setLoader(I18NBundle.class, new EagerI18NBundleLoader(resolver));
        setLoader(Skin.class, new EagerSkinLoader(resolver));
        setLoader(TextureAtlas.class, new EagerTextureAtlasLoader(resolver));
    }

    @Override
    public synchronized boolean update() {
        final boolean result = super.update();
        if (result && injections.isNotEmpty()) {
            for (final AssetInjection injection : injections) {
                if (injection.inject(this)) {
                    injections.remove();
                }
            }
        }
        return result;
    }

    /** @param injection will be scheduled and executed as soon as the requested assets are loaded. The assets must have
     *            been already scheduled for loading. */
    public void addInjection(final AssetInjection injection) {
        injections.add(injection);
    }
}
