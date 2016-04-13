package com.github.czyzby.lml.util;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;
import com.github.czyzby.lml.parser.LmlData;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;

/** An {@link ApplicationListener} implementation that manages a list of {@link AbstractLmlView LML views}. Forces the
 * user to prepare a {@link LmlParser} with {@link #createParser()} method. Ensures smooth view transitions. Adds
 * default actions with {@link #addDefaultActions()} method: "exit" closes the application after smooth screen hiding,
 * "close" is a no-op utility method for dialogs and "setView" changes the current view according to the actor's ID (the
 * ID has to match name of a class extending {@link AbstractLmlView}). Most of its settings are customizable - go
 * through protected methods API for more info.
 *
 * <p>
 * {@link AbstractLmlView} instances managed by this class are required to properly implement
 * {@link AbstractLmlView#getTemplateFile()}. Note that the views are likely to be accessed with reflection, so make
 * sure to include their classes in GWT reflection mechanism.
 *
 * <p>
 * What LibGDX {@link com.badlogic.gdx.Game Game} is to {@link com.badlogic.gdx.Screen Screen}, this class is the same
 * thing to {@link AbstractLmlView}. Except it adds much more functionalities.
 *
 * @author MJ */
public abstract class LmlApplicationListener implements ApplicationListener {
    private final ObjectMap<Class<?>, AbstractLmlView> views = GdxMaps.newObjectMap();
    private final ViewChangeRunnable viewChangeRunnable = new ViewChangeRunnable();
    private AbstractLmlView currentView;
    private LmlParser lmlParser;

    /** @return {@link LmlParser} instance created with {@link #createParser()} method.
     * @see LmlParser */
    public LmlParser getParser() {
        return lmlParser;
    }

    /** @return currently displayed {@link AbstractLmlView}. Can be null. */
    public AbstractLmlView getCurrentView() {
        return currentView;
    }

    /** @return direct reference to {@link AbstractLmlView} instances cache. Views (map values) are mapped by their
     *         classes (map keys). */
    protected ObjectMap<Class<?>, AbstractLmlView> getViews() {
        return views;
    }

    /** Called when application is created.
     *
     * <p>
     * Prepares {@link LmlParser} with {@link #createParser()} method. Adds default actions present in all LML
     * templates. When overridden, make sure to call super. */
    @Override
    public void create() {
        lmlParser = createParser();
        addDefaultActions();
    }

    /** Called by {@link #create()} after creation of the {@link LmlParser} using {@link #createParser()} method.
     * Registers default actions available in all views. */
    protected void addDefaultActions() {
        final LmlData data = lmlParser.getData();
        // Closes the application after screen transition.
        data.addActorConsumer("exit", new ActorConsumer<Void, Object>() {
            @Override
            public Void consume(final Object actor) {
                GdxUtilities.clearInputProcessor();
                currentView.getStage().addAction(Actions.sequence(getViewHidingAction(currentView),
                        Actions.run(GdxUtilities.getApplicationClosingRunnable())));
                return null;
            }
        });
        // Does nothing. Utility for dialogs: <dialog> ... <textButton onResult="close"> ...
        data.addActorConsumer("close", new ActorConsumer<Void, Object>() {
            @Override
            public Void consume(final Object actor) {
                return null;
            }
        });
        // Changes current view. Uses actor ID to determine view's class.
        data.addActorConsumer("setView", new ActorConsumer<Void, Actor>() {
            @Override
            @SuppressWarnings("unchecked")
            public Void consume(final Actor actor) {
                final String viewClassName = LmlUtilities.getActorId(actor);
                try {
                    final Class<? extends AbstractLmlView> viewClass = ClassReflection.forName(viewClassName);
                    setView(viewClass);
                } catch (final ReflectionException exception) {
                    throw new LmlParsingException("Unable to determine view class: " + viewClassName, exception);
                }
                return null;
            }
        });
    }

    /** @return a new customized instance of {@link LmlParser} used to process LML templates.
     * @see Lml
     * @see LmlParserBuilder */
    protected abstract LmlParser createParser();

    /** Calls {@link AbstractLmlView#resize(int, int, boolean)} on current view if it isn't empty.
     *
     * @param width current application width.
     * @param height current application height.
     * @see #isCenteringCameraOnResize() */
    @Override
    public void resize(final int width, final int height) {
        if (currentView != null) {
            currentView.resize(width, height, isCenteringCameraOnResize());
        }
    }

    /** @return if true, camera will be centered when resize event occurs. Defaults to false. When using certain
     *         viewports (like {@link com.badlogic.gdx.utils.viewport.ScreenViewport screen viewport}), this method
     *         should return true.
     * @see #resize(int, int) */
    protected boolean isCenteringCameraOnResize() {
        return false;
    }

    /** Clears the screen using {@link GdxUtilities#clearScreen()}. Calls {@link AbstractLmlView#render(float)} on
     * current view if it isn't empty. */
    @Override
    public void render() {
        GdxUtilities.clearScreen();
        if (currentView != null) {
            currentView.render(Gdx.graphics.getDeltaTime());
        }
    }

    /** Calls {@link AbstractLmlView#pause()} on current view if it isn't empty. */
    @Override
    public void pause() {
        if (currentView != null) {
            currentView.pause();
        }
    }

    /** Calls {@link AbstractLmlView#resume()} on current view if it isn't empty. */
    @Override
    public void resume() {
        if (currentView != null) {
            currentView.pause();
        }
    }

    /** Calls {@link AbstractLmlView#dispose()} on each stored view. */
    @Override
    public void dispose() {
        Disposables.disposeOf(views.values());
    }

    /** @param viewClass {@link AbstractLmlView} extension that represents a single view.
     * @return an instance of the view. If the instance is not currently cached, it will be created using default
     *         no-argument constructor with reflection.
     * @see #initiateView(AbstractLmlView) */
    protected AbstractLmlView getView(final Class<? extends AbstractLmlView> viewClass) {
        if (!views.containsKey(viewClass)) {
            // Cached version is not present - asking the parser to create and fill view:
            final AbstractLmlView view = getInstanceOf(viewClass);
            initiateView(view);
            return view;
        }
        return views.get(viewClass);
    }

    /** Disposes of {@link AbstractLmlView} instances. Clears views cache. Note that {@link #getCurrentView() current
     * view} will be set as null, so invoking this method is advised after the current view is already hidden. */
    public void clearViews() {
        Disposables.disposeOf(views.values());
        currentView = null;
        views.clear();
    }

    /** @param viewClass {@link AbstractLmlView} extension that represents a single view. If an instance of this view
     *            class is managed by the application listener, it will be {@link AbstractLmlView#dispose() disposed}
     *            and removed from views cache. If the view is currently displayed, {@link #getCurrentView() current
     *            view} will be set to null. */
    public void clearView(final Class<? extends AbstractLmlView> viewClass) {
        final AbstractLmlView view = views.get(viewClass);
        if (view != null) {
            view.dispose();
            views.remove(viewClass);
            validateCurrentView(view);
        }
    }

    private void validateCurrentView(final AbstractLmlView view) {
        if (view == currentView) {
            currentView = null;
        }
    }

    /** @param view {@link AbstractLmlView} extension that represents a single view. Will be
     *            {@link AbstractLmlView#dispose() disposed} and removed from views cache. If the view is currently
     *            displayed, {@link #getCurrentView() current view} will be set to null. */
    public void clearView(final AbstractLmlView view) {
        view.dispose();
        views.remove(view.getClass());
        validateCurrentView(view);
    }

    /** All currently cached views will be reloaded using {@link #reloadView(AbstractLmlView)} method. Note that this
     * method should be called when the current view is hidden, as parsing of multiple templates might cause some delays
     * (especially on slower devices). Useful for reloading localized texts after i18n bundle change. */
    public void reloadViews() {
        for (final AbstractLmlView view : views.values()) {
            reloadView(view);
        }
    }

    /** @param view will receive {@link AbstractLmlView#clear()} call. Its template file accessed by
     *            {@link AbstractLmlView#getTemplateFile()} will be parsed by the {@link LmlParser} and used to fill the
     *            view. */
    public void reloadView(final AbstractLmlView view) {
        view.clear();
        lmlParser.createView(view, view.getTemplateFile());
    }

    /** @param view its instance will be cached and returned each time it is requested with {@link #getView(Class)}
     *            method. Its template file accessed by {@link AbstractLmlView#getTemplateFile()} will be parsed by the
     *            {@link LmlParser} and used to fill the view. */
    protected void initiateView(final AbstractLmlView view) {
        lmlParser.createView(view, view.getTemplateFile());
        views.put(view.getClass(), view);
    }

    /** @param viewClass {@link AbstractLmlView} extension that represents a single view. Its instance is requested.
     * @return a new instance of the passed class. By default, the instance is created using the default no-argument
     *         constructor using reflection. */
    protected AbstractLmlView getInstanceOf(final Class<? extends AbstractLmlView> viewClass) {
        return Reflection.newInstance(viewClass);
    }

    /** @param viewClass {@link AbstractLmlView} extension that represents a single view. An instance of this class will
     *            become the current view after view transition.
     * @see #setView(AbstractLmlView) */
    public void setView(final Class<? extends AbstractLmlView> viewClass) {
        setView(getView(viewClass));
    }

    /** @param view will be set as the current view after view transition. Current screen (if any exists) will receive a
     *            {@link AbstractLmlView#hide()} call. The new screen will be resized using
     *            {@link AbstractLmlView#resize(int, int, boolean)} and then will receive a
     *            {@link AbstractLmlView#show()} call.
     * @see #getViewShowingAction(AbstractLmlView)
     * @see #getViewHidingAction(AbstractLmlView) */
    public void setView(final AbstractLmlView view) {
        // Note that this method uses static imports of actions.
        if (currentView != null) {
            viewChangeRunnable.setView(view);
            currentView.hide();
            currentView.getStage()
                    .addAction(Actions.sequence(getViewHidingAction(currentView), Actions.run(viewChangeRunnable)));
        } else {
            currentView = view;
            currentView.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), isCenteringCameraOnResize());
            currentView.show();
            Gdx.input.setInputProcessor(currentView.getStage());
            currentView.getStage().addAction(getViewShowingAction(view));
        }
    }

    /** @param view is about to be hidden.
     * @return {@link Action} instance used to hide the view. A simple fade-out action by default.
     * @see #getViewTransitionDuration() */
    protected Action getViewHidingAction(final AbstractLmlView view) {
        return Actions.fadeOut(getViewTransitionDuration(), Interpolation.fade);
    }

    /** @param view is about to be shown.
     * @return {@link Action} instance used to show the view. By default, makes sure that the view is transparent and
     *         begins a simple fade-in action.
     * @see #getViewTransitionDuration() */
    protected Action getViewShowingAction(final AbstractLmlView view) {
        return Actions.sequence(Actions.alpha(0f), Actions.fadeIn(getViewTransitionDuration(), Interpolation.fade));
    }

    /** @return length of a single view hiding or showing action used by default view transition actions. In seconds.
     * @see #getViewShowingAction(AbstractLmlView)
     * @see #getViewHidingAction(AbstractLmlView) */
    protected float getViewTransitionDuration() {
        return 0.4f;
    }

    /** {@link Action} utility. Used to change the current view thanks to {@link Actions#run(Runnable)}.
     *
     * @author MJ */
    protected class ViewChangeRunnable implements Runnable {
        private AbstractLmlView view;

        /** @param view should be shown. */
        public void setView(final AbstractLmlView view) {
            this.view = view;
        }

        @Override
        public void run() {
            currentView = null;
            LmlApplicationListener.this.setView(view);
        }
    }
}
