package com.github.czyzby.autumn.mvc.config;

import com.badlogic.gdx.assets.AssetLoaderParameters;

/** Contains all messages posted by Autumn MVC components using a
 * {@link com.github.czyzby.autumn.context.processor.method.MessageProcessor}.
 *
 * @author MJ */
public class AutumnMessage {
	/** Can be extended to contain all application's messages, but should not be initiated. */
	protected AutumnMessage() {
	}

	/** Posted each time SOME assets scheduled with
	 * {@link com.github.czyzby.autumn.mvc.component.asset.AssetService} are loaded. This is not posted if
	 * assets where loaded on demand with
	 * {@link com.github.czyzby.autumn.mvc.component.asset.AssetService#finishLoading(String, Class)} or
	 * {@link com.github.czyzby.autumn.mvc.component.asset.AssetService#finishLoading(String, Class, AssetLoaderParameters)}
	 * methods. */
	public static final String ASSETS_LOADED = "AMVC_assetsLoaded";

	/** Posted when application's {@link com.badlogic.gdx.scenes.scene2d.ui.Skin} is fully loaded. */
	public static final String SKIN_LOADED = "AMVC_skinLoaded";
}