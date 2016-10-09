package com.github.czyzby.lml.uedi.assets;

import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.github.czyzby.lml.uedi.assets.impl.AbstractAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;

/** Provides {@link ParticleEffect} instances.
 *
 * @author MJ */
public class Particle3dEffectProvider extends AbstractAssetProvider<ParticleEffect> {
    public static final String[] EXTENSIONS = new String[] { "pfx" };

    /** @param assetManager will be used to load the assets. */
    public Particle3dEffectProvider(final InjectingAssetManager assetManager) {
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
