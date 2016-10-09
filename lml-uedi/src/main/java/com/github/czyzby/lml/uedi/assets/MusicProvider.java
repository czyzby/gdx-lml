package com.github.czyzby.lml.uedi.assets;

import com.badlogic.gdx.audio.Music;
import com.github.czyzby.lml.uedi.assets.impl.AbstractAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;

/** Provides {@link Music} instances.
 *
 * @author MJ */
public class MusicProvider extends AbstractAssetProvider<Music> {
    public static final String[] EXTENSIONS = new String[] { "ogg", "mp3", "wav" };

    /** @param assetManager will be used to load the assets. */
    public MusicProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public Class<? extends Music> getType() {
        return Music.class;
    }

    @Override
    protected String getFolder() {
        return "music";
    }

    @Override
    protected String[] getExtensions() {
        return EXTENSIONS;
    }
}
