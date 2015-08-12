package com.github.czyzby.autumn.mvc.application;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.mvc.component.AutumnScanningRoot;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.i18n.LocaleService;
import com.github.czyzby.autumn.mvc.component.i18n.processor.AvailableLocalesAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.i18n.processor.I18nBundleAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;
import com.github.czyzby.autumn.mvc.component.sfx.processor.MusicEnabledAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.sfx.processor.MusicVolumeAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.sfx.processor.SoundEnabledAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.sfx.processor.SoundVolumeAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.SkinService;
import com.github.czyzby.autumn.mvc.component.ui.processor.LmlMacroAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.processor.PreferenceAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.processor.SkinAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.processor.SkinAssetAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.processor.StageViewportAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.processor.ViewActionContainerAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.processor.ViewActorAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.processor.ViewAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.processor.ViewDialogAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.processor.ViewStageAnnotationProcessor;
import com.github.czyzby.autumn.scanner.ClassScanner;
import com.github.czyzby.autumn.scanner.FixedClassScanner;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.tuple.immutable.Pair;

/** Default application listener implementation for Autumn MVC applications. Manages context.
 *
 * @author MJ */
public class AutumnApplication implements ApplicationListener {
	private Array<Pair<Class<?>, ClassScanner>> componentScanners;
	private ContextContainer contextContainer;
	private InterfaceService interfaceService;

	public AutumnApplication(final ClassScanner componentScanner, final Class<?> scanningRoot) {
		componentScanners = GdxArrays.newArray();
		registerComponents(getDefaultScanner(), getDefaultRoot());
		registerComponents(componentScanner, scanningRoot);
	}

	/** @param componentScanner used to scan for annotated classes.
	 * @param scanningRoot root of the scanning. */
	protected void registerComponents(final ClassScanner componentScanner, final Class<?> scanningRoot) {
		componentScanners.add(new Pair<Class<?>, ClassScanner>(scanningRoot, componentScanner));
	}

	/** @return scanning root for core Autumn components. Defaults to
	 *         {@link com.github.czyzby.autumn.mvc.component.AutumnScanningRoot}. */
	protected Class<?> getDefaultRoot() {
		return AutumnScanningRoot.class;
	}

	/** @return scanner aware of all core Autumn components. By default, returns
	 *         {@link com.github.czyzby.autumn.scanner.FixedClassScanner} so it could work on all platforms. */
	protected ClassScanner getDefaultScanner() {
		// Manually selecting all component classes so it could work on all platforms.
		return new FixedClassScanner(
				// Assets:
				AssetService.class,
				SkinAssetAnnotationProcessor.class,
				// Locale:
				LocaleService.class,
				// SFX:
				MusicService.class,
				MusicEnabledAnnotationProcessor.class,
				MusicVolumeAnnotationProcessor.class,
				SoundEnabledAnnotationProcessor.class,
				SoundVolumeAnnotationProcessor.class,
				// Settings:
				I18nBundleAnnotationProcessor.class,
				PreferenceAnnotationProcessor.class,
				SkinAnnotationProcessor.class,
				StageViewportAnnotationProcessor.class,
				// Interface:
				InterfaceService.class, SkinService.class, ViewAnnotationProcessor.class,
				ViewDialogAnnotationProcessor.class, ViewActionContainerAnnotationProcessor.class,
				ViewActorAnnotationProcessor.class, ViewStageAnnotationProcessor.class,
				LmlMacroAnnotationProcessor.class, AvailableLocalesAnnotationProcessor.class);
	}

	@Override
	public void create() {
		initiateContext();
		assignKeyComponents();
		clearComponentScanners();
	}

	private void initiateContext() {
		contextContainer = new ContextContainer();
		for (final Pair<Class<?>, ClassScanner> componentScanner : componentScanners) {
			contextContainer.registerComponents(componentScanner.getFirst(), componentScanner.getSecond());
		}
		contextContainer.initiateRegisteredComponents();
	}

	private void assignKeyComponents() {
		interfaceService = contextContainer.getFromContext(InterfaceService.class);
	}

	private void clearComponentScanners() {
		componentScanners.clear();
		componentScanners = null;
	}

	@Override
	public void resize(final int width, final int height) {
		interfaceService.resize(width, height);
	}

	@Override
	public void render() {
		GdxUtilities.clearScreen();
		interfaceService.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void pause() {
		interfaceService.pause();
	}

	@Override
	public void resume() {
		interfaceService.resume();
	}

	@Override
	public void dispose() {
		Disposables.disposeOf(contextContainer);
	}

	/** @return a reference to main application's context. */
	protected ContextContainer getContext() {
		return contextContainer;
	}
}
