package com.github.czyzby.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.czyzby.Core;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;

/** Displays LibGDX logos. A proof of concept: using "external" assets with a LML-built stage. The actual asset handling
 * and loading should probably be implemented in a different way (like using {@link AssetManager} and {@link Sprite}s).
 *
 * @author MJ */
public class Images extends AbstractLmlView {
    private final Texture logo;

    public Images() {
        super(Core.newStage());
        logo = new Texture(Gdx.files.internal("logo.png"));
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/Images.lml");
    }

    @Override
    public void render(final float delta) {
        super.render(delta);
        final Batch batch = getStage().getBatch();
        final Viewport viewport = getStage().getViewport();

        // Drawing logo image 3 times in different parts of the screen:
        batch.begin();
        batch.draw(logo, 0, 0);
        batch.draw(logo, (int) (viewport.getWorldWidth() / 2f - logo.getWidth() / 2f),
                (int) (viewport.getWorldHeight() / 2f - logo.getHeight() / 2f));
        batch.draw(logo, (int) (viewport.getWorldWidth() - logo.getWidth()),
                (int) (viewport.getWorldHeight() - logo.getHeight()));
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        Disposables.disposeOf(logo);
    }

    @Override
    public String getViewId() {
        return "img";
    }
}
