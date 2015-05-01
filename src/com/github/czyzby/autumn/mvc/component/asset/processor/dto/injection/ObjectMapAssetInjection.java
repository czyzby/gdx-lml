package com.github.czyzby.autumn.mvc.component.asset.processor.dto.injection;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

/** Handles delayed asset injection into {@link com.badlogic.gdx.utils.ObjectMap} field.
 *
 * @author MJ */
public class ObjectMapAssetInjection implements AssetInjection {
	private final String[] assetPaths;
	private final String[] assetKeys;
	private final Class<?> assetType;
	private final ReflectedField field;
	private final Object component;

	public ObjectMapAssetInjection(final String[] assetPaths, final String[] assetKeys,
			final Class<?> assetType, final ReflectedField field, final Object component) {
		this.assetPaths = assetPaths;
		this.assetKeys = assetKeys.length == 0 ? assetPaths : assetKeys;
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
			ObjectMap map = (ObjectMap) field.get(component);
			if (map == null) {
				map = GdxMaps.newObjectMap();
			}
			for (int assetIndex = 0; assetIndex < assetPaths.length; assetIndex++) {
				map.put(assetKeys[assetIndex], assetService.get(assetPaths[assetIndex], assetType));
			}
			field.set(component, map);
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to inject map of assets into component: " + component
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