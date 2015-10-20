package com.github.czyzby.gdx.idle.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlActor;

@View(value = "templates/loading.lml", id = "loading", first = true)
public class LoadingView implements ViewRenderer {
    @Inject private AssetService assetService;

    @LmlActor("loadingBar") private ProgressBar loadingBar;
    @LmlActor("progressLabel") private Label progressLabel;
    private int progress;

    @Override
    public void render(final Stage stage, final float delta) {
        assetService.update();
        updateProgress();
        stage.act(delta);
        stage.draw();
    }

    private void updateProgress() {
        final float currentProgress = assetService.getLoadingProgress();
        loadingBar.setValue(currentProgress);
        final int progressPercent = (int) (currentProgress * 100f);
        if (progressPercent != progress) {
            progress = progressPercent;
            progressLabel.setText(String.valueOf(progress));
        }
    }
}