package com.github.czyzby.gdx.idle.view.dialog;

import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;

@ViewDialog(value = "templates/dialogs/settings.lml", id = "settings")
public class SettingsDialog implements ActionContainer {
    @Inject private MusicService musicService;

    @LmlAction("saveSettings")
    private void saveSettings() {
        musicService.savePreferences(true);
    }

    @LmlAction("restoreSettings")
    private void restoreSettings() {
        musicService.restoreSettingsFromPreferences();
    }
}
