package com.github.czyzby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.actions.GlobalActions;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.util.LmlApplicationListener;
import com.github.czyzby.lml.vis.util.VisLml;
import com.github.czyzby.views.Menu;
import com.kotcrab.vis.ui.VisUI;

/** Core of the application. Maintains a map of all application's views.
 *
 * @author MJ */
public class Core extends LmlApplicationListener {
    /** Default window size. Size of the virtual stage viewport. */
    public static final int WIDTH = 640, HEIGHT = 480;
    // Side note: the application does not look very pretty when resized, but - in this case - blame LibGDX scaling, not
    // the humble creator of this example.

    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        // Loading VisUI skin:
        VisUI.load();

        super.create(); // Initiates parser, adds default actions.

        System.out.println("Generating...");
        saveDtdSchema(Gdx.files.absolute("/home/mj/lml_vis.dtd"));
        System.out.println("Generating...");

        // Processing global LML macros, available in all views:
        getParser().parseTemplate(Gdx.files.internal("views/macros/Global.lml"));

        // Setting first view:
        setView(Menu.class);
    }

    @Override
    protected LmlParser createParser() {
        return VisLml.parser()
                // Registering global actions, available in all views:
                .actions("global", new GlobalActions())
                // Adding i18n bundle - all LML strings proceeded with @ will be taken from this bundle:
                .i18nBundle(I18NBundle.createBundle(Gdx.files.internal("i18n/nls")))
                // Creating a new parser instance:
                .build();
    }

    @Override
    public void dispose() {
        super.dispose(); // Disposes of views.
        Disposables.disposeOf(batch);
        VisUI.dispose();
    }

    // Note that this class contains some static utility methods, which could be considered bad practice (equivalent of
    // global variables... in a way). This is just an example of a simple application: to avoid overuse of static
    // methods, while maintaining simplicity and ease of coding, you could use a dependency injection library like
    // Dagger or gdx-autumn, both of which allow to create (cleaner-ish) scoped singletons.

    // Heavy objects static utilities:

    /** @return the only instance of {@link SpriteBatch} in the application. */
    public static Batch getBatch() {
        return ((Core) Gdx.app.getApplicationListener()).batch;
    }

    /** @return a new instance of {@link Stage} using {@link ExtendViewport} and application's only {@link Batch}
     *         instance. */
    public static Stage newStage() {
        return new Stage(new FitViewport(WIDTH, HEIGHT), getBatch());
    }
}
