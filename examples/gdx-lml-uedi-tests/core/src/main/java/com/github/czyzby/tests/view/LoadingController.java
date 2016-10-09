package com.github.czyzby.tests.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.uedi.views.View;
import com.github.czyzby.uedi.stereotype.Singleton;

/** Updates {@link AssetManager} until all assets are loaded.
 *
 * @author MJ */
public class LoadingController extends View implements Singleton {
    @LmlActor("menu") ProgressBar progressBar;
    private AssetManager assetManager;

    @Override
    public String getViewId() {
        return "loading";
    }

    @Override
    public boolean isFirst() {
        return true;
    }

    @Override
    public void render(final float delta) {
        assetManager.update();
        progressBar.setValue(assetManager.getProgress());
        super.render(delta);
    }
}
