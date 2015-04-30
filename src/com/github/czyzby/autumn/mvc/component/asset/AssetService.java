package com.github.czyzby.autumn.mvc.component.asset;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.ObjectSet.ObjectSetIterator;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.method.Destroy;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.asset.processor.dto.ArrayAssetInjection;
import com.github.czyzby.autumn.mvc.component.asset.processor.dto.AssetInjection;
import com.github.czyzby.autumn.mvc.component.asset.processor.dto.AssetProvider;
import com.github.czyzby.autumn.mvc.component.asset.processor.dto.ObjectMapAssetInjection;
import com.github.czyzby.autumn.mvc.component.asset.processor.dto.ObjectSetAssetInjection;
import com.github.czyzby.autumn.mvc.component.asset.processor.dto.StandardAssetInjection;
import com.github.czyzby.autumn.mvc.config.AutumnActionPriority;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;

/** Wraps around an internal {@link com.badlogic.gdx.assets.AssetManager}, providing utilities for asset
 * loading. Note that some wrapped methods provide additional utility, so direct access to the manager is not
 * advised.
 *
 * @author MJ */
@MetaComponent
public class AssetService extends ComponentFieldAnnotationProcessor {
	private final AssetManager assetManager = new AssetManager();

	private final ObjectSet<AssetInjection> assetInjections = GdxSets.newSet();
	private final ObjectSet<String> scheduledAssets = GdxSets.newSet();
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
		return !assetManager.isLoaded(assetPath) && !scheduledAssets.contains(assetPath);
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
		for (final ObjectSetIterator<AssetInjection> iterator = assetInjections.iterator(); iterator
				.hasNext();) {
			final AssetInjection assetInjection = iterator.next();
			if (assetInjection.inject(this)) {
				assetInjection.removeScheduledAssets(scheduledAssets);
				iterator.remove();
			}
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
		validateAssetData(component, field, assetData);
		if (field.getFieldType().equals(Lazy.class)) {
			handleLazyAssetInjection(component, field, assetData);
		} else if (field.getFieldType().equals(Array.class)) {
			handleArrayInjection(component, field, assetData);
		} else if (field.getFieldType().equals(ObjectSet.class)) {
			handleSetInjection(component, field, assetData);
		} else if (field.getFieldType().equals(ObjectMap.class)) {
			handleMapInjection(component, field, assetData);
		} else {
			handleRegularAssetInjection(component, field, assetData);
		}
	}

	private void validateAssetData(final ContextComponent component, final ReflectedField field,
			final Asset assetData) {
		if (assetData.value().length == 0) {
			throw new AutumnRuntimeException(
					"Asset paths array cannot be empty. Found empty array in field: " + field
							+ " of component: " + component.getComponent() + ".");
		}
		if (assetData.keys().length != 0 && assetData.value().length != assetData.keys().length) {
			throw new AutumnRuntimeException(
					"In @Asset annotation, keys() array length (if specified) has to match value() array length. Found different lengths in field: "
							+ field + " of component: " + component.getComponent() + ".");
		}
	}

	private void handleLazyAssetInjection(final ContextComponent component, final ReflectedField field,
			final Asset assetData) {
		if (assetData.value().length != 1) {
			throw new AutumnRuntimeException(
					"Lazy wrapper can currently contain only one asset. Found multiple assets in field: "
							+ field + " of component: " + component.getComponent());
		}
		final String assetPath = assetData.value()[0];
		if (!assetData.loadOnDemand()) {
			load(assetPath, assetData.type());
		}
		try {
			field.set(component.getComponent(),
					Lazy.providedBy(new AssetProvider(this, assetPath, assetData.type())));
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to inject lazy asset.", exception);
		}
	}

	private void handleRegularAssetInjection(final ContextComponent component, final ReflectedField field,
			final Asset assetData) {
		if (assetData.value().length != 1) {
			throw new AutumnRuntimeException(
					"Regular fields can store only 1 asset. If the field is a collection, its type is not currently supported: only LibGDX Array, ObjectSet and ObjectMap are permitted. Regular arrays will not be supported. Found multiple assets in field: "
							+ field + " of component: " + component.getComponent());
		}
		final String assetPath = assetData.value()[0];
		load(assetPath, field.getFieldType());
		if (assetData.loadOnDemand()) {
			// Loaded immediately.
			final Object asset = finishLoading(assetPath, field.getFieldType());
			try {
				field.set(component.getComponent(), asset);
			} catch (final ReflectionException exception) {
				throw new AutumnRuntimeException("Unable to inject asset loaded on demand.", exception);
			}
		} else {
			// Scheduled to be loaded, delayed injection.
			assetInjections.add(new StandardAssetInjection(field, assetPath, component.getComponent()));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleArrayInjection(final ContextComponent component, final ReflectedField field,
			final Asset assetData) {
		if (assetData.loadOnDemand()) {
			try {
				Array assets = (Array) field.get(component.getComponent());
				if (assets == null) {
					assets = GdxArrays.newArray();
				}
				for (final String assetPath : assetData.value()) {
					assets.add(finishLoading(assetPath, assetData.type()));
				}
				field.set(component.getComponent(), assets);
			} catch (final ReflectionException exception) {
				throw new AutumnRuntimeException("Unable to inject array of assets into: "
						+ component.getComponent(), exception);
			}
		} else {
			for (final String assetPath : assetData.value()) {
				load(assetPath, assetData.type());
			}
			// Scheduled to be loaded, delayed injection.
			assetInjections.add(new ArrayAssetInjection(assetData.value(), assetData.type(), field, component
					.getComponent()));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleSetInjection(final ContextComponent component, final ReflectedField field,
			final Asset assetData) {
		if (assetData.loadOnDemand()) {
			try {
				ObjectSet assets = (ObjectSet) field.get(component.getComponent());
				if (assets == null) {
					assets = GdxSets.newSet();
				}
				for (final String assetPath : assetData.value()) {
					assets.add(finishLoading(assetPath, assetData.type()));
				}
				field.set(component.getComponent(), assets);
			} catch (final ReflectionException exception) {
				throw new AutumnRuntimeException("Unable to inject set of assets into: "
						+ component.getComponent(), exception);
			}
		} else {
			for (final String assetPath : assetData.value()) {
				load(assetPath, assetData.type());
			}
			// Scheduled to be loaded, delayed injection.
			assetInjections.add(new ObjectSetAssetInjection(assetData.value(), assetData.type(), field,
					component.getComponent()));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handleMapInjection(final ContextComponent component, final ReflectedField field,
			final Asset assetData) {
		if (assetData.loadOnDemand()) {
			final String[] assetPaths = assetData.value();
			final String[] assetKeys = assetData.keys().length == 0 ? assetData.value() : assetData.keys();
			try {
				ObjectMap assets = (ObjectMap) field.get(component.getComponent());
				if (assets == null) {
					assets = GdxMaps.newObjectMap();
				}
				for (int assetIndex = 0; assetIndex < assetPaths.length; assetIndex++) {
					assets.put(assetKeys[assetIndex], finishLoading(assetPaths[assetIndex], assetData.type()));
				}
				field.set(component.getComponent(), assets);
			} catch (final ReflectionException exception) {
				throw new AutumnRuntimeException("Unable to inject array of assets into: "
						+ component.getComponent(), exception);
			}
		} else {
			for (final String assetPath : assetData.value()) {
				load(assetPath, assetData.type());
			}
			// Scheduled to be loaded, delayed injection.
			assetInjections.add(new ObjectMapAssetInjection(assetData.value(), assetData.keys(), assetData
					.type(), field, component.getComponent()));
		}
	}

	/** @param action will be executed after all currently scheduled assets are loaded. This requires an
	 *            {@link #update()} or {@link #finishLoading()} call. */
	public void addOnLoadAction(final Runnable action) {
		onLoadActions.add(action);
	}
}