package com.github.czyzby.lml.uedi.ui.impl;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/** Allows to pass an eagerly constructed {@link Skin} instance to an asset manager.
 *
 * @author MJ */
public class EagerSkinLoader extends SkinLoader {
    /** @param resolver will be used to resolve skin files. */
    public EagerSkinLoader(final FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    @SuppressWarnings("rawtypes") // Raw types due to ugly API.
    public Array<AssetDescriptor> getDependencies(final String fileName, final FileHandle file,
            final SkinParameter parameter) {
        if (parameter instanceof EagerSkinParameter && ((EagerSkinParameter) parameter).skin != null) {
            return null;
        }
        return super.getDependencies(fileName, file, parameter);
    }

    @Override
    public Skin loadSync(final AssetManager manager, final String fileName, final FileHandle file,
            final SkinParameter parameter) {
        if (parameter instanceof EagerSkinParameter) {
            final Skin skin = ((EagerSkinParameter) parameter).skin;
            if (skin != null) {
                return skin;
            }
        }
        return super.loadSync(manager, fileName, file, parameter);
    }

    /** Allows to pass eagerly loaded skin into the asset manager.
     *
     * @author MJ */
    public static class EagerSkinParameter extends SkinLoader.SkinParameter {
        private final Skin skin;

        /** @param skin was eagerly loaded, but should still be managed by an asset manager. */
        public EagerSkinParameter(final Skin skin) {
            this.skin = skin;
        }
    }
}
