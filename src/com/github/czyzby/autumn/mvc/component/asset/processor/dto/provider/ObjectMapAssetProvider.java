package com.github.czyzby.autumn.mvc.component.asset.processor.dto.provider;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

/** Asset provider for map injections.
 *
 * @author MJ */
public class ObjectMapAssetProvider implements ObjectProvider<ObjectMap<String, Object>> {
	private final AssetService assetService;
	private final String[] assetPaths;
	private final String[] assetKeys;
	private final Class<?> assetClass;

	public ObjectMapAssetProvider(final AssetService assetService, final String assetPaths[],
			final String assetKeys[], final Class<?> assetClass) {
		this.assetService = assetService;
		this.assetPaths = assetPaths;
		this.assetKeys = assetKeys.length == 0 ? assetPaths : assetKeys;
		this.assetClass = assetClass;
	}

	@Override
	public ObjectMap<String, Object> provide() {
		final ObjectMap<String, Object> assets = GdxMaps.newObjectMap();
		for (int assetIndex = 0; assetIndex < assetPaths.length; assetIndex++) {
			if (!assetService.isLoaded(assetPaths[assetIndex])) {
				// This will also schedule loading of the asset if it was previously unloaded.
				assets.put(assetKeys[assetIndex],
						assetService.finishLoading(assetPaths[assetIndex], assetClass));
			} else {
				assets.put(assetKeys[assetIndex], assetService.get(assetPaths[assetIndex], assetClass));
			}
		}
		return assets;
	}
}