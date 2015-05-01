package com.github.czyzby.autumn.mvc.component.asset.processor.dto.provider;

import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;

/** Asset provider for set injections.
 *
 * @author MJ */
public class ObjectSetAssetProvider implements ObjectProvider<ObjectSet<Object>> {
	private final AssetService assetService;
	private final String[] assetPaths;
	private final Class<?> assetClass;

	public ObjectSetAssetProvider(final AssetService assetService, final String assetPaths[],
			final Class<?> assetClass) {
		this.assetService = assetService;
		this.assetPaths = assetPaths;
		this.assetClass = assetClass;
	}

	@Override
	public ObjectSet<Object> provide() {
		final ObjectSet<Object> assets = GdxSets.newSet();
		for (final String assetPath : assetPaths) {
			if (!assetService.isLoaded(assetPath)) {
				// This will also schedule loading of the asset if it was previously unloaded.
				assets.add(assetService.finishLoading(assetPath, assetClass));
			} else {
				assets.add(assetService.get(assetPath, assetClass));
			}
		}
		return assets;
	}
}