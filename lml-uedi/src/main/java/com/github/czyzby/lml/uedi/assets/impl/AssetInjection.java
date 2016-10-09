package com.github.czyzby.lml.uedi.assets.impl;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.Field;
import com.github.czyzby.lml.uedi.assets.Loaded;

/** Contains necessary data to inject an asset into a field.
 *
 * @author MJ */
public class AssetInjection {
    private final String path;
    private final Class<?> type;
    private final Field field;
    private final Object owner;
    private final Loaded loadListener;

    /** @param path path to the asset.
     * @param type type of stored value.
     * @param field reference to the field that needs to be filled.
     * @param owner instance of the class containing the field. */
    public AssetInjection(final String path, final Class<?> type, final Field field, final Object owner) {
        this.path = path;
        this.type = type;
        this.field = field;
        this.owner = owner;
        loadListener = owner instanceof Loaded ? (Loaded) owner : null;
    }

    /** @param asset will be injected into the field. */
    public void inject(final Object asset) {
        field.setAccessible(true);
        try {
            field.set(owner, asset);
        } catch (final Exception exception) {
            throw new GdxRuntimeException("Unable to inject loaded asset: " + path + " into component: " + owner,
                    exception);
        }
        if (loadListener != null) {
            loadListener.onLoad(path, type, asset);
        }
    }

    /** @param assetManager will be used to retrieve the asset.
     * @return true if injection was successful. */
    public boolean inject(final AssetManager assetManager) {
        if (assetManager.isLoaded(path)) {
            inject(assetManager.get(path, type));
            return true;
        }
        return false;
    }
}
