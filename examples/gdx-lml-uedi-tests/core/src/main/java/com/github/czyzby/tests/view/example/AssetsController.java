package com.github.czyzby.tests.view.example;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.czyzby.lml.uedi.assets.Loaded;
import com.github.czyzby.lml.uedi.views.View;
import com.github.czyzby.tests.Root;
import com.github.czyzby.uedi.stereotype.Singleton;

/** Shows asset usage.
 *
 * @author MJ */
public class AssetsController extends View implements Loaded, Singleton {
    // Will be injected:
    private Batch batch;
    // Will be automatically loaded:
    private Texture badlogic; // image/badlogic.png
    private TextureAtlas logo; // atlas/logo.atlas
    // "transient" variables are not injected:
    transient Sprite logoSprite;
    // Non-null variables are not injected:
    private final Viewport gameViewport = new FitViewport(Root.WIDTH, Root.HEIGHT);

    @Override
    public String getViewId() {
        return "assets";
    }

    @Override
    public void onLoad(final String path, final Class<?> type, final Object asset) {
        // This class implements "Loaded" interface with "onLoad" method: this method is called every time a requested
        // assets is loaded and injected into the class. In the example below, a Sprite instance is created with a
        // texture atlas region as soon as the atlas is loaded.
        if (asset == logo) {
            logoSprite = new Sprite(logo.findRegion("logo"));
            logoSprite.setX((int) (-logoSprite.getWidth() / 2));
            logoSprite.setY((int) -logoSprite.getHeight() * 2);
        }
    }

    @Override
    public void resize(final int width, final int height, final boolean centerCamera) {
        gameViewport.update(width, height);
        super.resize(width, height, centerCamera);
    }

    @Override
    public void render(final float delta) {
        // A prove of concept: regular assets can be used along with a Scene2D Stage.
        final float alpha = getStage().getRoot().getColor().a;
        batch.begin();
        batch.setColor(1f, 1f, 1f, alpha);
        batch.setProjectionMatrix(gameViewport.getCamera().combined);
        logoSprite.draw(batch, alpha);
        batch.draw(badlogic, -badlogic.getWidth() / 2, 0);
        batch.end();
        super.render(delta);
    }
}
