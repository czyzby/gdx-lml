package com.github.czyzby.lml.uedi.assets.impl;

import java.lang.reflect.Member;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.common.Exceptions;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.uedi.assets.Loaded;
import com.github.czyzby.lml.util.Lml;
import com.github.czyzby.uedi.reflection.impl.FieldMember;
import com.github.czyzby.uedi.stereotype.Provider;

/** Abstract base for asset providers.
 *
 * @author MJ
 *
 * @param <Asset> type of provided assets. */
public abstract class AbstractAssetProvider<Asset> implements Provider<Asset> {
    private final InjectingAssetManager assetManager;
    private final ObjectMap<String, String> idsToPaths = GdxMaps.newObjectMap();

    /** @param assetManager will be used to load the assets and schedule field injections. */
    public AbstractAssetProvider(final InjectingAssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Asset provide(final Object target, final Member member) {
        if (member == null) {
            throwUnknownPathException();
        }
        final String id = member.getName();
        if (Strings.isEmpty(id)) {
            throwUnknownPathException();
        }
        final Asset asset = getOrLoad(id);
        if (member instanceof FieldMember) {
            if (asset == null) {
                assetManager.addInjection( // Delayed asset injection - field will be filled when assets are loaded:
                        new AssetInjection(idsToPaths.get(id), getType(), ((FieldMember) member).getField(), target));
            } else if (target instanceof Loaded) {
                ((Loaded) target).onLoad(idsToPaths.get(id), getType(), asset);
            }
        } else if (asset == null) {
            Gdx.app.log(Lml.LOGGER_TAG, "Warn: requested instance of unloaded asset: " + id);
        }
        return asset;
    }

    /** @throws GdxRuntimeException on each call. */
    protected void throwUnknownPathException() {
        throw new GdxRuntimeException("Unable to determine asset path.");
    }

    /** @param id name of the asset file (without extension or folder). "_" are replaced with "/" to support nested
     *            folders. Cannot be empty or null.
     * @return an instance of the asset (if its already loaded) or null if it was scheduled for loading.
     * @throws GdxRuntimeException if ID is invalid. */
    public Asset getOrLoad(final String id) {
        final String path = determinePath(id);
        if (assetManager.isLoaded(path)) {
            return assetManager.get(path, getType());
        }
        assetManager.load(path, getType());
        return null;
    }

    /** @param id name of the asset file (without extension or folder). "_" are replaced with "/" to support nested
     *            folders. Cannot be empty or null.
     * @return a cached path assigned to the ID or a new determined path to an existing file.
     * @throws GdxRuntimeException if unable to find existing file. */
    protected String determinePath(final String id) {
        String path = idsToPaths.get(id);
        if (path != null) {
            return path;
        }
        final String fileName = getFileName(getFolder(), id);
        for (final String extension : getExtensions()) {
            path = fileName + extension;
            try {
                final FileHandle file = Gdx.files.internal(path);
                if (file.exists()) {
                    idsToPaths.put(id, path);
                    return path;
                }
            } catch (final Exception exception) {
                Exceptions.ignore(exception); // Invalid FileHandle implementation, throws errors for missing files.
            }
        }
        throw new GdxRuntimeException("Unable to find file in folder: '" + getFolder() + "' matching name: '" + id
                + "' with any of the supported extensions.");
    }

    /** @param folder can contain the asset.
     * @param id might contain '_', which should be converted to '/' to support nested paths.
     * @return file name (supporting nested paths) proceeded with a slash and with a dot appended at the end. */
    protected String getFileName(final String folder, final String id) {
        final StringBuilder builder = new StringBuilder(folder.length() + 1 + id.length() + 1);
        builder.append(folder);
        builder.append('/');
        builder.append(id);
        Strings.replace(builder, '_', '/');
        builder.append('.');
        return builder.toString();
    }

    /** @return an array of supported file extensions. */
    protected abstract String[] getExtensions();

    /** @return the expected path of the asset. */
    protected abstract String getFolder();

    /** @return instance of {@link InjectingAssetManager} managing the assets. */
    protected InjectingAssetManager getAssetManager() {
        return assetManager;
    }
}
