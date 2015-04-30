package com.github.czyzby.autumn.mvc.component.asset.processor.dto;

import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;

/** Delayed asset injection container.
 *
 * @author MJ */
public class StandardAssetInjection implements AssetInjection {
	protected final ReflectedField field;
	protected final String assetPath;
	protected final Object component;

	public StandardAssetInjection(final ReflectedField field, final String assetPath, final Object component) {
		this.field = field;
		this.assetPath = assetPath;
		this.component = component;
	}

	@Override
	public boolean inject(final AssetService assetService) {
		if (assetService.isLoaded(assetPath)) {
			try {
				injectAsset(assetService);
				return true;
			} catch (final ReflectionException exception) {
				throw new AutumnRuntimeException("Unable to inject asset into component: " + component + ".",
						exception);
			}
		}
		return false;
	}

	protected void injectAsset(final AssetService assetService) throws ReflectionException {
		field.set(component, assetService.get(assetPath, field.getFieldType()));
	}

	@Override
	public void fillScheduledAssets(final ObjectSet<String> scheduledAssets) {
		scheduledAssets.add(assetPath);
	}

	@Override
	public void removeScheduledAssets(final ObjectSet<String> scheduledAssets) {
		scheduledAssets.remove(assetPath);
	}

	@Override
	public boolean equals(final Object object) {
		return object instanceof StandardAssetInjection
				&& ((StandardAssetInjection) object).assetPath.equals(assetPath)
				&& ((StandardAssetInjection) object).field.equals(field);
	}

	@Override
	public int hashCode() {
		return assetPath.hashCode() ^ field.hashCode();
	}
}
