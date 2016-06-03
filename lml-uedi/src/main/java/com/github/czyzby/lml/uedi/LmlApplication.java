package com.github.czyzby.lml.uedi;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.preference.ApplicationPreferences;
import com.github.czyzby.kiwi.util.gdx.viewport.LetterboxingViewport;
import com.github.czyzby.lml.parser.LmlData;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.github.czyzby.lml.uedi.assets.AssetManagerProvider;
import com.github.czyzby.lml.uedi.assets.BitmapFontProvider;
import com.github.czyzby.lml.uedi.assets.ModelProvider;
import com.github.czyzby.lml.uedi.assets.MusicProvider;
import com.github.czyzby.lml.uedi.assets.Particle3dEffectProvider;
import com.github.czyzby.lml.uedi.assets.ParticleEffectProvider;
import com.github.czyzby.lml.uedi.assets.PixmapProvider;
import com.github.czyzby.lml.uedi.assets.SoundProvider;
import com.github.czyzby.lml.uedi.assets.TextureAtlasProvider;
import com.github.czyzby.lml.uedi.assets.TextureProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;
import com.github.czyzby.lml.uedi.collections.ArrayProvider;
import com.github.czyzby.lml.uedi.collections.ListProvider;
import com.github.czyzby.lml.uedi.collections.MapProvider;
import com.github.czyzby.lml.uedi.collections.SetProvider;
import com.github.czyzby.lml.uedi.i18n.I18NBundleProvider;
import com.github.czyzby.lml.uedi.i18n.LocalePreference;
import com.github.czyzby.lml.uedi.impl.LmlContext;
import com.github.czyzby.lml.uedi.logger.LoggerProvider;
import com.github.czyzby.lml.uedi.preferences.PreferencesProvider;
import com.github.czyzby.lml.uedi.ui.BatchProvider;
import com.github.czyzby.lml.uedi.ui.SkinProvider;
import com.github.czyzby.lml.uedi.ui.SpriteBatchProvider;
import com.github.czyzby.lml.uedi.ui.StageProvider;
import com.github.czyzby.lml.uedi.views.View;
import com.github.czyzby.lml.util.Lml;
import com.github.czyzby.lml.util.LmlApplicationListener;
import com.github.czyzby.lml.util.LmlUtilities;
import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.scanner.ClassScanner;

/** Default implementation of {@link com.badlogic.gdx.ApplicationListener ApplicationListener} for UEDI-based
 * applications using LML to manage their views. Additionally to managing LML view controllers, initiates UEDI context
 * and registers some default components and providers.
 *
 * @author MJ */
public class LmlApplication extends LmlApplicationListener {
    private final Class<?> root;
    private final ClassScanner classScanner;
    private final String preferences;
    private Context context;
    // GUI data:
    private Stage stage;
    private boolean centerCamera;
    // Locale data:
    private I18NBundleProvider i18nBundleProvider;
    private LocalePreference localePreference;

    /** @param root a class in the base package of the application. All classes implementing UEDI stereotype interfaces
     *            in its subpackages will be found and initiated.
     * @param classScanner used to automatically scan the classpath. Has to be platform-specific, as there is no unified
     *            way of accessing class pool. */
    public LmlApplication(final Class<?> root, final ClassScanner classScanner) {
        this(root, classScanner, Lml.LOGGER_TAG);
    }

    /** @param root a class in the base package of the application. All classes implementing UEDI stereotype interfaces
     *            in its subpackages will be found and initiated.
     * @param classScanner used to automatically scan the classpath. Has to be platform-specific, as there is no unified
     *            way of accessing class pool.
     * @param preferences default path to application's preferences. Should generally match application's name. Will be
     *            set as default preferences in {@link LmlParser}. */
    public LmlApplication(final Class<?> root, final ClassScanner classScanner, final String preferences) {
        this.root = root;
        this.classScanner = classScanner;
        this.preferences = preferences;
    }

    @Override
    public void create() {
        ApplicationPreferences.setDefaultPreferences(getPreferences());
        context = createContext(classScanner);
        addDefaultComponents();
        context.scan(root);
        doAfterScan();
        super.create();
        setStageViewport();
        final LmlParser parser = getParser();
        parser.getData().setDefaultPreferences(ApplicationPreferences.getPreferences());
        i18nBundleProvider.fill(parser);
        setFirstView();
    }

    /** Called by the {@link #create()} method to set {@link Viewport} instance of the GUI {@link Stage}. */
    protected void setStageViewport() {
        if (context.isAvailable(Viewport.class)) {
            stage.setViewport(context.get(Viewport.class, stage));
        } else {
            Gdx.app.debug(Lml.LOGGER_TAG, "No provider found for Viewport.class. Using default Stage viewport.");
        }
        final Viewport viewport = stage.getViewport();
        centerCamera = viewport instanceof ScreenViewport || viewport instanceof LetterboxingViewport;
    }

    /** Used to construct {@link Context} instance in {@link #create()} method.
     *
     * @param classScanner will be used to scan for components.
     * @return {@link LmlContext} by default. */
    protected Context createContext(final ClassScanner classScanner) {
        return new LmlContext(this, classScanner);
    }

    /** This method is invoked after the context is fully scanned and initiated. Can be safely overridden - the method
     * does nothing by default. */
    protected void doAfterScan() {
    }

    /** It is assumed that the first view was set using {@link #forceCurrentView(View)} method during context
     * initiation. This method ensures smooth showing of the first view.
     *
     * @throws GdxRuntimeException if first view was not set. */
    protected void setFirstView() {
        final AbstractLmlView firstView = getCurrentView();
        if (firstView == null) {
            throw new GdxRuntimeException("No view marked as first. Unable to start application.");
        }
        setCurrentView(null);
        initiateView(firstView);
        setView(firstView);
    }

    @Override
    protected boolean isCenteringCameraOnResize() {
        return centerCamera;
    }

    /** Also available as "setLocale" action in LML views. Will extract locale from actor's ID.
     *
     * @param locale will be set as the current application's locale. If is not equal to the current locale, will hide
     *            current view, reload all referenced {@link com.badlogic.gdx.utils.I18NBundle i18n bundles}, recreate
     *            all views and reshow the current view. */
    public void setLocale(final Locale locale) {
        if (!localePreference.isCurrent(locale)) {
            setView(getCurrentView(), new Action() {
                private boolean reloaded = false;

                @Override
                public boolean act(final float delta) {
                    if (!reloaded) {
                        reloaded = true;
                        localePreference.setLocale(locale);
                        localePreference.save();
                        reloadViews();
                    }
                    return true;
                }
            });
        }
    }

    @Override
    protected AbstractLmlView getInstanceOf(final Class<? extends AbstractLmlView> viewClass) {
        return context.get(viewClass);
    }

    /** Registers multiple providers and singletons that produce LibGDX-related object instances. */
    protected void addDefaultComponents() {
        // Creating components manually to speed up the start-up.
        final boolean mapSuper = context.isMapSuperTypes();
        context.setMapSuperTypes(false);
        // Assets:
        final AssetManagerProvider assetManagerProvider = new AssetManagerProvider();
        context.addProvider(assetManagerProvider);
        context.addDestructible(assetManagerProvider);
        final InjectingAssetManager assetManager = assetManagerProvider.getAssetManager();
        context.addProvider(new BitmapFontProvider(assetManager));
        context.addProvider(new ModelProvider(assetManager));
        context.addProvider(new MusicProvider(assetManager));
        context.addProvider(new Particle3dEffectProvider(assetManager));
        context.addProvider(new ParticleEffectProvider(assetManager));
        context.addProvider(new PixmapProvider(assetManager));
        context.addProvider(new SoundProvider(assetManager));
        context.addProvider(new TextureAtlasProvider(assetManager));
        context.addProvider(new TextureProvider(assetManager));
        // Collections:
        context.addProvider(new ArrayProvider<Object>());
        context.addProvider(new ListProvider<Object>());
        context.addProvider(new MapProvider<Object, Object>());
        context.addProvider(new SetProvider<Object>());
        // I18n:
        i18nBundleProvider = new I18NBundleProvider(assetManager);
        localePreference = new LocalePreference(i18nBundleProvider);
        i18nBundleProvider.setLocalePreference(localePreference);
        context.addProvider(i18nBundleProvider);
        context.addProperty(localePreference);
        context.addProvider(localePreference);
        // Logging:
        context.addProvider(new LoggerProvider());
        // Music:
        // TODO music service
        // Preferences:
        context.addProvider(new PreferencesProvider());
        // UI:
        final SpriteBatchProvider spriteBatchProvider = new SpriteBatchProvider();
        context.addProvider(new BatchProvider(spriteBatchProvider));
        context.addProvider(spriteBatchProvider);
        context.addDestructible(spriteBatchProvider);
        context.addProvider(new SkinProvider(assetManager));
        final StageProvider stageProvider = new StageProvider(spriteBatchProvider.getBatch());
        stage = stageProvider.getStage();
        context.addProvider(stageProvider);

        context.setMapSuperTypes(mapSuper);
        // Application listener:
        context.add(this);
    }

    @Override
    protected void addDefaultActions() {
        super.addDefaultActions();
        final LmlData data = getParser().getData();
        // Extracts ID from the actor, parses it as Locale instance. Tries to change current locale.
        data.addActorConsumer("setLocale", new ActorConsumer<Void, Actor>() {
            @Override
            public Void consume(final Actor actor) {
                setLocale(LocalePreference.fromString(LmlUtilities.getActorId(actor)));
                return null;
            }
        });
        // Returns current locale serialized as string.
        data.addActorConsumer("getLocale", new ActorConsumer<String, Actor>() {
            @Override
            public String consume(final Actor actor) {
                return LocalePreference.toString(localePreference.getLocale());
            }
        });
        // Reload current view. Useful for rapid GUI prototyping.
        data.addActorConsumer("reloadView", new ActorConsumer<Void, Object>() {
            @Override
            public Void consume(final Object actor) {
                reloadView(getCurrentView());
                return null;
            }
        });
    }

    /** @param view will be immediately set as the current view. Note that this should not be generally used to change
     *            screens - to ensure smooth transitions, use {@code setView} methods instead.
     * @see #setView(AbstractLmlView)
     * @see #setView(Class) */
    public void forceCurrentView(final View view) {
        setCurrentView(view);
    }

    /** @return path to application's preferences. Will be set as default preferences in {@link LmlParser}. */
    protected String getPreferences() {
        return preferences;
    }

    @Override
    protected void initiateView(final AbstractLmlView view) {
        getViews().put(view.getClass(), view);
        final String viewId = view.getViewId();
        if (Strings.isNotEmpty(viewId)) {
            addClassAlias(viewId, view.getClass());
        }
        view.setStage(null);
        final Array<Actor> actors = getParser().createView(view, view.getTemplateFile());
        if (view instanceof View) {
            ((View) view).clearActors();
            ((View) view).addActors(actors);
        } else {
            throw new GdxRuntimeException("LmlApplication can handle only View instances.");
        }
        view.setStage(stage);
    }

    @Override
    protected LmlParser createParser() {
        if (!context.isAvailable(LmlParser.class)) {
            throw new GdxRuntimeException(
                    "No singleton, provider or factory producing LmlParser. Unable to construct views.");
        }
        return context.get(LmlParser.class);
    }

    /** @return application's UEDI context, which allows to construct and inject components. */
    public Context getContext() {
        return context;
    }

    @Override
    public void reloadView(final AbstractLmlView lmlView) {
        final View view = (View) lmlView;
        final boolean isCurrent = getCurrentView() == view;
        if (isCurrent) {
            view.getStage().getRoot().clearChildren();
        }
        view.setStage(null);
        view.clearActors();
        getParser().createView(view, view.getTemplateFile());
        view.setStage(stage);
        if (isCurrent) {
            view.show();
        }
    }

    @Override
    public void addClassAlias(final String alias, final Class<? extends AbstractLmlView> viewClass) {
        super.addClassAlias(alias, viewClass);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (context != null) {
            try {
                context.destroy();
            } catch (final Exception exception) {
                Gdx.app.log(Lml.LOGGER_TAG, "Unable to destroy the context.", exception);
            }
        }
    }
}
