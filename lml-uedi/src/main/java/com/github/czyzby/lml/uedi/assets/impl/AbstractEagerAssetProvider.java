package com.github.czyzby.lml.uedi.assets.impl;

import java.lang.reflect.Member;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.uedi.assets.Loaded;
import com.github.czyzby.uedi.reflection.impl.FieldMember;

/** Loads assets immediately. Caches for further reuse.
 *
 * @author MJ
 *
 * @param <Asset> type of loaded assets. */
public abstract class AbstractEagerAssetProvider<Asset> extends AbstractAssetProvider<Asset> {
    private final ObjectMap<String, Asset> idsToAssets = GdxMaps.newObjectMap();

    public AbstractEagerAssetProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public Asset provide(final Object target, final Member member) {
        if (member == null) {
            throwUnknownPathException();
        }
        final String id = member.getName();
        if (Strings.isEmpty(id)) {
            throwUnknownPathException();
        }
        final Asset asset = getOrLoad(id);
        if (member instanceof FieldMember && target instanceof Loaded) {
            ((Loaded) target).onLoad(determinePath(id), getType(), asset);
        }
        return asset;
    }

    @Override
    public Asset getOrLoad(final String id) {
        if (idsToAssets.containsKey(id)) {
            return idsToAssets.get(id);
        }
        final Asset asset = load(id, determinePath(id), getAssetManager());
        idsToAssets.put(id, asset);
        return asset;
    }

    /** @param id ID of the asset.
     * @param path determined asset path.
     * @param assetManager asset manager, which should include the asset.
     * @return loaded asset instance. */
    protected abstract Asset load(String id, String path, AssetManager assetManager);
}
