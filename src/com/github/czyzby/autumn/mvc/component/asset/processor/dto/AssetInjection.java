package com.github.czyzby.autumn.mvc.component.asset.processor.dto;

import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;

/** Delayed asset injection container.
 *
 * @author MJ */
public class AssetInjection {
	protected final ReflectedField field;
	protected final String assetPath;
	protected final Object component;

	public AssetInjection(final ReflectedField field, final String assetPath, final Object component) {
		this.field = field;
		this.assetPath = assetPath;
		this.component = component;
	}

	/** Injects the value of annotated field.
	 *
	 * @param assetService provides the asset. */
	public void inject(final AssetService assetService) {
		try {
			injectAsset(assetService);
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to inject asset.", exception);
		}
	}

	protected void injectAsset(final AssetService assetService) throws ReflectionException {
		field.set(component, assetService.get(assetPath, field.getFieldType()));
	}
}
