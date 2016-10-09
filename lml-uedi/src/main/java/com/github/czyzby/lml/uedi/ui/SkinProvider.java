package com.github.czyzby.lml.uedi.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.czyzby.lml.uedi.assets.TextureAtlasProvider;
import com.github.czyzby.lml.uedi.assets.impl.AbstractEagerAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.EagerTextureAtlasLoader;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;
import com.github.czyzby.lml.uedi.ui.impl.EagerSkinLoader;

/** Provides {@link Skin} instances.
 *
 * @author MJ */
public class SkinProvider extends AbstractEagerAssetProvider<Skin> {
    public static final String[] EXTENSIONS = new String[] { "json" };

    /** @param assetManager will be used to load the assets. */
    public SkinProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public Class<? extends Skin> getType() {
        return Skin.class;
    }

    @Override
    protected String getFolder() {
        return "skin";
    }

    @Override
    protected String[] getExtensions() {
        return EXTENSIONS;
    }

    @Override
    protected Skin load(final String id, final String path, final AssetManager assetManager) {
        final String atlasPath = TextureAtlasProvider.getTextureAtlasPath(id);
        final TextureAtlas atlas = new TextureAtlas(atlasPath);
        assetManager.load(atlasPath, TextureAtlas.class, new EagerTextureAtlasLoader.EagerTextureAtlasParameter(atlas));
        final Skin skin = new Skin(Gdx.files.internal(path), atlas);
        assetManager.load(path, Skin.class, new EagerSkinLoader.EagerSkinParameter(skin));
        return skin;
    }
}
