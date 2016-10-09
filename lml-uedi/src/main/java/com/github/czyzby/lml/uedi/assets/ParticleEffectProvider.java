package com.github.czyzby.lml.uedi.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.uedi.assets.impl.AbstractAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;

/** Provides {@link ParticleEffect} instances.
 *
 * @author MJ */
public class ParticleEffectProvider extends AbstractAssetProvider<ParticleEffect> {
    public static final String[] EXTENSIONS = new String[] { "p", "pfx" };

    /** @param assetManager will be used to load the assets. */
    public ParticleEffectProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public Class<? extends ParticleEffect> getType() {
        return ParticleEffect.class;
    }

    @Override
    public ParticleEffect getOrLoad(final String id) {
        final String[] data = Strings.split(id, '$');
        if (data.length == 0) {
            throwUnknownPathException();
        }
        final String path = determinePath(data[0]);
        getIdsToPaths().put(id, path);
        final AssetManager assetManager = getAssetManager();
        if (assetManager.isLoaded(path)) {
            return assetManager.get(path, getType());
        }
        if (data.length > 1) {
            final String atlasName = TextureAtlasProvider.getTextureAtlasPath(data[1]);
            final ParticleEffectParameter parameters = new ParticleEffectParameter();
            parameters.atlasFile = atlasName;
            assetManager.load(path, ParticleEffect.class, parameters);
        } else {
            assetManager.load(path, ParticleEffect.class);
        }
        return null;
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
