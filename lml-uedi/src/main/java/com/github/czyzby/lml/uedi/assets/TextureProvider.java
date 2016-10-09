package com.github.czyzby.lml.uedi.assets;

import com.badlogic.gdx.graphics.Texture;
import com.github.czyzby.lml.uedi.assets.impl.AbstractAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;

/** Provides {@link Texture} instances.
 *
 * @author MJ */
public class TextureProvider extends AbstractAssetProvider<Texture> {
    public static final String[] EXTENSIONS = new String[] { "png", "jpg" };

    /** @param assetManager will be used to load the textures. */
    public TextureProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public Class<? extends Texture> getType() {
        return Texture.class;
    }

    @Override
    protected String getFolder() {
        return "image";
    }

    @Override
    protected String[] getExtensions() {
        return EXTENSIONS;
    }
}
