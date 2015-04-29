package com.github.czyzby.autumn.mvc.component.asset.processor.dto;

import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;

/** Default provider for delayed asset injections.
 *
 * @author MJ */
public class AssetProvider implements ObjectProvider<Object> {
	private final AssetService assetService;
	private final String assetPath;
	private final Class<?> assetClass;

	public AssetProvider(final AssetService assetService, final String assetPath, final Class<?> assetClass) {
		this.assetService = assetService;
		this.assetPath = assetPath;
		this.assetClass = assetClass;
	}

	@Override
	public Object provide() {
		if (!assetService.isLoaded(assetPath)) {
			// This will also schedule loading of the asset if it was previously unloaded.
			return assetService.finishLoading(assetPath, assetClass);
		}
		return assetService.get(assetPath, assetClass);
	}
}
