package com.github.czyzby.gdx.idle.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.autumn.mvc.stereotype.ViewActor;

@View(value = "templates/loading.lml", id = "loading", first = true)
public class LoadingView implements ViewRenderer {
	@Inject
	private AssetService assetService;
	@Inject
	private InterfaceService interfaceService;

	@ViewActor("loadingBar")
	private ProgressBar loadingBar;
	@ViewActor("progressLabel")
	private Label progressLabel;
	private int progress;

	@Override
	public void render(final Stage stage, final float delta) {
		if (assetService.update()) {
			interfaceService.show(MenuView.class);
		}
		updateProgress();
		stage.act(delta);
		stage.draw();
	}

	private void updateProgress() {
		final float currentProgress = assetService.getLoadingProgress();
		loadingBar.setValue(progress);
		final int progressPercent = (int) (currentProgress * 100f);
		if (progressPercent != progress) {
			progress = progressPercent;
			progressLabel.setText(String.valueOf(progress));
		}
	}
}