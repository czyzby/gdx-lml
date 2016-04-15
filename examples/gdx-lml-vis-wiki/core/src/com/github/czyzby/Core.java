package com.github.czyzby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.util.LmlApplicationListener;
import com.github.czyzby.lml.vis.util.VisLml;
import com.github.czyzby.views.FirstView;
import com.github.czyzby.views.YetAnotherView;
import com.github.czyzby.views.actions.Global;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;

public class Core extends LmlApplicationListener {
    public static final int WIDTH = 480, HEIGHT = 340;
    private Batch batch;

    @Override
    public void create() {
        VisUI.load(SkinScale.X2);
        batch = new SpriteBatch();
        super.create();
        addClassAlias(YetAnotherView.ID, YetAnotherView.class);
        setView(FirstView.class);

        // Generating DTD:
        // saveDtdSchema(Gdx.files.local("lml.dtd"));
    }

    /** @return application's only {@link Batch}. */
    public Batch getBatch() {
        return batch;
    }

    /** @return a new customized {@link Stage} instance. */
    public static Stage newStage() {
        // Getting our Core instance:
        final Core core = (Core) Gdx.app.getApplicationListener();
        return new Stage(new FitViewport(WIDTH, HEIGHT), core.getBatch());
    }

    @Override
    public void dispose() {
        super.dispose();
        Disposables.disposeOf(batch);
        VisUI.dispose(); // Disposing of default VisUI skin.
        setCurrentView(null);
    }

    @Override
    protected LmlParser createParser() {
        return VisLml.parser()
                // Registering global action container:
                .actions("global", Global.class)
                // Adding localization support:
                .i18nBundle(I18NBundle.createBundle(Gdx.files.internal("i18n/bundle"))).build();
    }
}