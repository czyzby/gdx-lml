package com.github.czyzby.lml.uedi.assets;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.github.czyzby.lml.uedi.assets.impl.AbstractAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;

/** Provides {@link BitmapFont} instances.
 *
 * @author MJ */
public class BitmapFontProvider extends AbstractAssetProvider<BitmapFont> {
    public static final String[] EXTENSIONS = new String[] { "fnt" };

    /** @param assetManager will be used to load the assets. */
    public BitmapFontProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public Class<? extends BitmapFont> getType() {
        return BitmapFont.class;
    }

    @Override
    protected String getFolder() {
        return "font";
    }

    @Override
    protected String[] getExtensions() {
        return EXTENSIONS;
    }
}
