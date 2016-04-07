package com.github.czyzby;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.actions.GlobalActions;
import com.github.czyzby.kiwi.util.gdx.AbstractApplicationListener;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.github.czyzby.lml.vis.util.VisLml;
import com.github.czyzby.views.Menu;
import com.kotcrab.vis.ui.VisUI;

/** Core of the application. Maintains a map of all application's views.
 *
 * @author MJ */
// Note that this class contains some static utility methods, which could be considered bad practice (equivalent of
// global variables... in a way). This is just an example of a simple application: to avoid overuse of static methods,
// while maintaining simplicity and ease of coding, you could use a dependency injection library like Dagger or
// gdx-autumn, both of which allow to create cleaner scoped singletons.
public class Core extends AbstractApplicationListener {
    /** Default window size. Size of the virtual stage viewport. */
    public static final int WIDTH = 640, HEIGHT = 480;
    private static final float FADE_DURATION = 0.5f;

    private final ObjectMap<Class<?>, AbstractLmlView> views = GdxMaps.newObjectMap();
    private LmlParser lmlParser;
    private AbstractLmlView currentView;
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Loading VisUI skin:
        VisUI.load();

        // Preparing parser:
        lmlParser = VisLml.parser()
                // Registering global actions, available in all views:
                .actions("global", new GlobalActions())
                // Adding i18n bundle - all LML strings proceeded with @ will be taken from this bundle:
                .i18nBundle(I18NBundle.createBundle(Gdx.files.internal("i18n/nls")))
                // Creating a new parser instance:
                .build();

        // Processing global LML macros, available in all views:
        lmlParser.parseTemplate(Gdx.files.internal("views/macros/Global.lml"));

        // Setting first view:
        setView(Menu.class);
    }

    @Override
    public void resize(final int width, final int height) {
        currentView.resize(width, height);
    }

    @Override
    protected void render(final float deltaTime) {
        currentView.render(deltaTime);
    }

    @Override
    public void dispose() {
        // Null-safe disposing of heavy objects:
        Disposables.disposeOf(views.values());
        Disposables.disposeOf(batch);
        VisUI.dispose();
    }

    // Heavy objects utilities:

    /** @return the only instance of {@link SpriteBatch} in the application. */
    public static Batch getBatch() {
        return ((Core) Gdx.app.getApplicationListener()).batch;
    }

    /** @return a new instance of {@link Stage} using {@link ExtendViewport} and application's only {@link Batch}
     *         instance. */
    public static Stage newStage() {
        return new Stage(new FitViewport(WIDTH, HEIGHT), getBatch());
    }

    // Screen changing methods:

    /** Lazy initiation of LML view.
     *
     * @param viewClass will create instance of this view or return cached value.
     * @return the only instance of selected view. */
    protected AbstractLmlView getView(final Class<? extends AbstractLmlView> viewClass) {
        if (!views.containsKey(viewClass)) {
            // Cached version is not present - asking the parser to create and fill view:
            final AbstractLmlView view = lmlParser.createView(viewClass,
                    Gdx.files.internal("views/" + viewClass.getSimpleName() + ".lml"));
            views.put(viewClass, view);
            return view;
        }
        return views.get(viewClass);
    }

    /** Static view change utility.
     *
     * @param viewClass will find view instance with this class and set it as the current view. */
    public static void changeView(final Class<? extends AbstractLmlView> viewClass) {
        final Core core = (Core) Gdx.app.getApplicationListener();
        core.setView(viewClass);
    }

    /** @param viewClass will find view instance with this class and set it as the current view. */
    public void setView(final Class<? extends AbstractLmlView> viewClass) {
        setView(getView(viewClass));
    }

    /** @param view will be set as the current view. Views transition consist of scene fading for about 1 second
     *            total. */
    public void setView(final AbstractLmlView view) {
        // Note that this method uses static imports of actions.
        if (currentView != null) {
            currentView.getStage()
                    .addAction(sequence(
                            // Fading out current screen:
                            alpha(0f, FADE_DURATION, Interpolation.fade),
                            // Scheduling screen change after fading:
                            run(getViewChangeRunnable(view))));
        } else {
            currentView = view;
            currentView.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.input.setInputProcessor(currentView.getStage());
            currentView.getStage()
                    .addAction(sequence(
                            // Making sure the screen is transparent:
                            alpha(0f),
                            // Fading in:
                            alpha(1f, FADE_DURATION, Interpolation.fade)));
        }
    }

    private Runnable getViewChangeRunnable(final AbstractLmlView view) {
        return new Runnable() {
            @Override
            public void run() {
                currentView = null;
                GdxUtilities.clearInputProcessor();
                setView(view);
            }
        };
    }
}
