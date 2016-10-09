package com.github.czyzby.lml.uedi.ui;

import java.lang.reflect.Member;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;
import com.github.czyzby.kiwi.util.gdx.viewport.LetterboxingViewport;
import com.github.czyzby.uedi.stereotype.Provider;

/** Manages and provides a single {@link Stage} instance (dedicated to GUI).
 *
 * @author MJ */
public class StageProvider implements Provider<Stage> {
    private final Stage stage;

    /** @param batch will be used to draw the GUI. */
    public StageProvider(final Batch batch) {
        stage = new Stage(new LetterboxingViewport(GdxUtilities.isMobile() ? 160f : 96f,
                Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight()), batch);
    }

    @Override
    public Class<? extends Stage> getType() {
        return Stage.class;
    }

    @Override
    public Stage provide(final Object target, final Member member) {
        return stage;
    }

    /** @return the only {@link Stage} instance dedicated to GUI. */
    public Stage getStage() {
        return stage;
    }
}
