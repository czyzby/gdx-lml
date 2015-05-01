package com.github.czyzby.autumn.mvc.component.asset.processor.dto.provider;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

/** Asset provider for array injections.
 *
 * @author MJ */
public class ArrayAssetProvider implements ObjectProvider<Array<Object>> {
	private final AssetService assetService;
	private final String[] assetPaths;
	private final Class<?> assetClass;

	public ArrayAssetProvider(final AssetService assetService, final String assetPaths[],
			final Class<?> assetClass) {
		this.assetService = assetService;
		this.assetPaths = assetPaths;
		this.assetClass = assetClass;
	}

	@Override
	public Array<Object> provide() {
		final Array<Object> assets = GdxArrays.newArray();
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