package com.github.czyzby.lml.uedi.assets;

import com.badlogic.gdx.audio.Sound;
import com.github.czyzby.lml.uedi.assets.impl.AbstractAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;

/** Provides {@link Sound} instances.
 *
 * @author MJ */
public class SoundProvider extends AbstractAssetProvider<Sound> {
    public static final String[] EXTENSIONS = new String[] { "ogg", "mp3", "wav" };

    /** @param assetManager will be used to load the assets. */
    public SoundProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public Class<? extends Sound> getType() {
        return Sound.class;
    }

    @Override
    protected String getFolder() {
        return "sound";
    }

    @Override
    protected String[] getExtensions() {
        return EXTENSIONS;
    }
}
