package com.github.czyzby.autumn.mvc.component.asset;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.method.Destroy;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.asset.processor.dto.AssetInjection;
import com.github.czyzby.autumn.mvc.component.asset.processor.dto.AssetProvider;
import com.github.czyzby.autumn.mvc.config.AutumnActionPriority;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

/** Wraps around an internal {@link com.badlogic.gdx.assets.AssetManager}, providing utilities for asset
 * loading. Note that some wrapped methods provide additional utility, so direct access to the manager is not
 * advised.
 *
 * @author MJ */
@MetaComponent
public class AssetService extends ComponentFieldAnnotationProcessor {
	private final AssetManager assetManager = new AssetManager();

	private final ObjectMap<String, AssetInjection> assetInjections = GdxMaps.newObjectMap();
	private final Array<String> injectedAssets = GdxArrays.newArray();
	private final Array<Runnable> onLoadActions = GdxArrays.newArray();

	/** Schedules loading of the selected asset, if it was not scheduled already.
	 *
	 * @param assetPath internal path to the asset.
	 * @param assetClass class of the asset. */
	public void load(final String assetPath, final Class<?> assetClass) {
		if (isAssetNotScheduled(assetPath)) {
			assetManager.load(assetPath, assetClass);
		}
	}

	private boolean isAssetNotScheduled(final String assetPath) {
		return !assetManager.isLoaded(assetPath) && !assetInjections.containsKey(assetPath);
	}

	/** Schedules loading of the selected asset, if it was not scheduled already.
	 *
	 * @param assetPath assetPath internal path to the asset.
	 * @param assetClass assetClass class of the asset.
	 * @param loadingParameters specific loading parameters. */
	public <Type> void load(final String assetPath, final Class<Type> assetClass,
			final AssetLoaderParameters<Type> loadingParameters) {
		if (isAssetNotScheduled(assetPath)) {
			assetManager.load(assetPath, assetClass, loadingParameters);
		}
	}

	/** @param assetPath internal path to the asset.
	 * @return true if the asset is fully loaded. */
	public boolean isLoaded(final String assetPath) {
		return assetManager.isLoaded(assetPath);
	}

	/** Schedules disposing of the selected asset.
	 *
	 * @param assetPath internal path to the asset. */
	public void unload(final String assetPath) {
		assetManager.unload(assetPath);
	}

	/** Immediately loads all scheduled assets. */
	public void finishLoading() {
		assetManager.finishLoading();
		injectRequestedAssets();
		invokeOnLoadActions();
	}

	private void invokeOnLoadActions() {
		for (final Runnable action : onLoadActions) {
			if (action != null) {
				action.run();
			}
		}
		onLoadActions.clear();
	}

	/** Immediately loads the chosen assets. Schedules loading of the asset if it wasn't selected to be loaded
	 * already.
	 *
	 * @param assetPath internal path to the asset.
	 * @param assetClass class of the loaded asset.
	 * @return instance of the loaded asset. */
	public <Type> Type finishLoading(final String assetPath, final Class<Type> assetClass) {
		// Ensuring that the asset was actually scheduled to load:
		load(assetPath, assetClass);
		assetManager.finishLoadingAsset(assetPath);
		injectRequestedAssets();
		return get(assetPath, assetClass);
	}

	private void injectRequestedAssets() {
		for (final Entry<String, AssetInjection> assetInjection : assetInjections) {
			if (isLoaded(assetInjection.key)) {
				assetInjection.value.inject(this);
				injectedAssets.add(assetInjection.key);
			}
		}
		if (GdxArrays.isNotEmpty(injectedAssets)) {
			for (final String assetPath : injectedAssets) {
				assetInjections.remove(assetPath);
			}
			injectedAssets.clear();
		}
	}

	/** Manually updates wrapped asset manager.
	 *
	 * @return true if all scheduled assets are loaded. */
	public boolean update() {
		final boolean isLoaded = assetManager.update();
		if (isLoaded) {
			injectRequestedAssets();
			invokeOnLoadActions();
		}
		return isLoaded;
	}

	/** @return progress of asset loading. */
	public float getLoadingProgress() {
		return assetManager.getProgress();
	}

	/** @param assetPath internal path to the asset.
	 * @param assetClass class of the asset.
	 * @return an instance of the loaded asset, if available. */
	public <Type> Type get(final String assetPath, final Class<Type> assetClass) {
		return assetManager.get(assetPath, assetClass);
	}

	@Destroy(priority = AutumnActionPriority.MIN_PRIORITY)
	private void destroy() {
		Disposables.disposeOf(assetManager);
	}

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return Asset.class;
	}

	@Override
	public <Type> void processField(final ContextContainer context, final ContextComponent component,
			final ReflectedField field) {
		final Asset assetData = field.getAnnotation(Asset.class);
		if (field.getFieldType().equals(Lazy.class)) {
			handleLazyAssetInjection(component, field, assetData);
		} else {
			handleRegularAssetInjection(component, field, assetData);
		}
	}

	private void handleLazyAssetInjection(final ContextComponent component, final ReflectedField field,
			final Asset assetData) {
		if (!assetData.loadOnDemand()) {
			load(assetData.value(), assetData.type());
		}
		try {
			field.set(component.getComponent(),
					Lazy.providedBy(new AssetProvider(this, assetData.value(), assetData.type())));
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to inject lazy asset.", exception);
		}
	}

	private void handleRegularAssetInjection(final ContextComponent component, final ReflectedField field,
			final Asset assetData) {
		load(assetData.value(), field.getFieldType());
		if (assetData.loadOnDemand()) {
			// Loaded immediately.
			final Object asset = finishLoading(assetData.value(), field.getFieldType());
			try {
				field.set(component.getComponent(), asset);
			} catch (final ReflectionException exception) {
				throw new AutumnRuntimeException("Unable to inject asset loaded on demand.", exception);
			}
		} else {
			// Scheduled to be loaded, delayed injection.
			assetInjections.put(assetData.value(),
					new AssetInjection(field, assetData.value(), component.getComponent()));
		}
	}

	/** @param action will be executed after all currently scheduled assets are loaded. This requires an
	 *            {@link #update()} or {@link #finishLoading()} call. */
	public void addOnLoadAction(final Runnable action) {
		onLoadActions.add(action);
	}
}