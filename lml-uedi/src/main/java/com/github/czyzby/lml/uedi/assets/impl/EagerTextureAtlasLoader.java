package com.github.czyzby.lml.uedi.assets.impl;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/** Allows to eagerly load a {@link TextureAtlas}.
 *
 * @author MJ */
public class EagerTextureAtlasLoader extends TextureAtlasLoader {
    public EagerTextureAtlasLoader(final FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public TextureAtlas load(final AssetManager assetManager, final String fileName, final FileHandle file,
            final TextureAtlasParameter parameter) {
        if (parameter instanceof EagerTextureAtlasParameter) {
            final TextureAtlas atlas = ((EagerTextureAtlasParameter) parameter).atlas;
            if (atlas != null) {
                return atlas;
            }
        }
        return super.load(assetManager, fileName, file, parameter);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Array<AssetDescriptor> getDependencies(final String fileName, final FileHandle atlasFile,
            final TextureAtlasParameter parameter) {
        if (parameter instanceof EagerTextureAtlasParameter && ((EagerTextureAtlasParameter) parameter).atlas != null) {
            return null;
        }
        return super.getDependencies(fileName, atlasFile, parameter);
    }

    /** Allows to eagerly load the assets.
     *
     * @author MJ */
    public static class EagerTextureAtlasParameter extends TextureAtlasLoader.TextureAtlasParameter {
        private final TextureAtlas atlas;

        /** @param atlas was eagerly loaded, but should still be managed by an asset manager. */
        public EagerTextureAtlasParameter(final TextureAtlas atlas) {
            this.atlas = atlas;
        }
    }
}
