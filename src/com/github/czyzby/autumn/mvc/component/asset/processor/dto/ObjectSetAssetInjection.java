package com.github.czyzby.autumn.mvc.component.asset.processor.dto;

import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;

/** Handles delayed asset injection into {@link com.badlogic.gdx.utils.ObjectSet} field.
 *
 * @author MJ */
public class ObjectSetAssetInjection implements AssetInjection {
	private final String[] assetPaths;
	private final Class<?> assetType;
	private final ReflectedField field;
	private final Object component;

	public ObjectSetAssetInjection(final String[] assetPaths, final Class<?> assetType,
			final ReflectedField field, final Object component) {
		this.assetPaths = assetPaths;
		this.assetType = assetType;
		this.field = field;
		this.component = component;
	}

	@Override
	public boolean inject(final AssetService assetService) {
		for (final String assetPath : assetPaths) {
			if (!assetService.isLoaded(assetPath)) {
				return false;
			}
		}
		injectAssets(assetService);
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void injectAssets(final AssetService assetService) {
		try {
			ObjectSet set = (ObjectSet) field.get(component);
			if (set == null) {
				set = GdxSets.newSet();
			}
			for (final String assetPath : assetPaths) {
				set.add(assetService.get(assetPath, assetType));
			}
			field.set(component, set);
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to inject set of assets into component: " + component
					+ ".", exception);
		}
	}

	@Override
	public void fillScheduledAssets(final ObjectSet<String> scheduledAssets) {
		for (final String assetPath : assetPaths) {
			scheduledAssets.add(assetPath);
		}
	}

	@Override
	public void removeScheduledAssets(final ObjectSet<String> scheduledAssets) {
		for (final String assetPath : assetPaths) {
			scheduledAssets.remove(assetPath);
		}
	}
}