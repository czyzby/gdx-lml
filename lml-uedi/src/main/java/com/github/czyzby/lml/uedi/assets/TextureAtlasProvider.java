package com.github.czyzby.lml.uedi.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.uedi.assets.impl.AbstractAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;

/** Provides {@link TextureAtlas} instances.
 *
 * @author MJ */
public class TextureAtlasProvider extends AbstractAssetProvider<TextureAtlas> {
    public static final String ATLAS_FOLDER = "atlas";
    public static final String[] EXTENSIONS = new String[] { "atlas", "pack" };

    /** @param assetManager will be used to load the assets. */
    public TextureAtlasProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public Class<? extends TextureAtlas> getType() {
        return TextureAtlas.class;
    }

    @Override
    protected String getFolder() {
        return ATLAS_FOLDER;
    }

    @Override
    protected String[] getExtensions() {
        return EXTENSIONS;
    }

    /** @param id ID of the texture atlas file.
     * @return path of the atlas with the default extension in the default atlas folder. */
    public static String getTextureAtlasPath(final String id) {
        final String extension = EXTENSIONS[0];
        final StringBuilder builder = new StringBuilder(
                ATLAS_FOLDER.length() + 1 + id.length() + 1 + extension.length());
        builder.append(ATLAS_FOLDER).append('/').append(id);
        Strings.replace(builder, '_', '/');
        builder.append('.').append(extension);
        return builder.toString();
    }
}
