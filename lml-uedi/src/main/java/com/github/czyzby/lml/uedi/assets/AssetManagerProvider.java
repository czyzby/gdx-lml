package com.github.czyzby.lml.uedi.assets;

import java.lang.reflect.Member;

import com.badlogic.gdx.assets.AssetManager;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;
import com.github.czyzby.uedi.stereotype.Destructible;
import com.github.czyzby.uedi.stereotype.Provider;

/** Holds a unique instance of {@link AssetManager} that will be used to load all automatically injected assets. Handles
 * assets disposing.
 *
 * @author MJ */
public class AssetManagerProvider implements Destructible, Provider<AssetManager> {
    public static final int ASSET_DISPOSING_PRIORITY = 10;
    private final InjectingAssetManager assetManager = new InjectingAssetManager();

    @Override
    public Class<? extends AssetManager> getType() {
        return AssetManager.class;
    }

    @Override
    public AssetManager provide(final Object target, final Member member) {
        return assetManager;
    }

    /** @return the only instance of the {@link AssetManager}. */
    public InjectingAssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public int getDestructionOrder() {
        return ASSET_DISPOSING_PRIORITY;
    }

    @Override
    public void destroy() {
        Disposables.gracefullyDisposeOf(assetManager);
    }
}
