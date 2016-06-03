package com.github.czyzby.lml.uedi.assets;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.github.czyzby.lml.uedi.assets.impl.AbstractAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;

/** Provides {@link ParticleEffect} instances.
 *
 * @author MJ */
public class ParticleEffectProvider extends AbstractAssetProvider<ParticleEffect> {
    public static final String[] EXTENSIONS = new String[] { "p", "pfx" };
    // TODO $ to separate name from atlas

    /** @param assetManager will be used to load the assets. */
    public ParticleEffectProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public Class<? extends ParticleEffect> getType() {
        return ParticleEffect.class;
    }

    @Override
    protected String getFolder() {
        return "effect";
    }

    @Override
    protected String[] getExtensions() {
        return EXTENSIONS;
    }
}
