package com.github.czyzby.lml.uedi.assets;

import com.badlogic.gdx.graphics.Pixmap;
import com.github.czyzby.lml.uedi.assets.impl.AbstractAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;

/** Provides {@link Pixmap} instances.
 *
 * @author MJ */
public class PixmapProvider extends AbstractAssetProvider<Pixmap> {
    public static final String[] EXTENSIONS = new String[] { "png", "jpg" };

    /** @param assetManager will be used to load the assets. */
    public PixmapProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public Class<? extends Pixmap> getType() {
        return Pixmap.class;
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
