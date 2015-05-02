package com.github.czyzby.gdx.idle.view.dialog;

import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.parser.impl.annotation.ViewAction;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;

@ViewDialog(value = "templates/dialogs/settings.lml", id = "settings")
public class SettingsDialog implements ActionContainer {
	@Inject
	private MusicService musicService;

	@ViewAction("saveSettings")
	private void saveSettings() {
		musicService.savePreferences(true);
	}

	@ViewAction("restoreSettings")
	private void restoreSettings() {
		musicService.restoreSettingsFromPreferences();
	}
}
