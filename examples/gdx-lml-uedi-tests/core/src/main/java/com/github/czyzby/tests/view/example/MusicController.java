package com.github.czyzby.tests.view.example;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.uedi.music.MusicService;
import com.github.czyzby.lml.uedi.views.View;
import com.github.czyzby.uedi.stereotype.Singleton;

/** Plays music theme in the background. Allows to play sounds.
 *
 * @author MJ */
public class MusicController extends View implements Singleton {
    // Will be injected:
    private MusicService musicService;
    // Will be automatically loaded:
    private Music theme; // music/theme.ogg
    private Sound sound; // sound/sound.ogg

    @Override
    public String getViewId() {
        return "music";
    }

    @Override
    public void show() {
        // "theme" will be constantly played as long as the user is on this view:
        musicService.addTheme(theme);
        super.show();
    }

    @Override
    public void hide() {
        musicService.removeTheme(theme);
        musicService.savePreferences();
        super.hide();
    }

    /** Is sounds are currently on, will play the loaded sound with current volume setting. */
    @LmlAction("play")
    public void playSound() {
        musicService.play(sound);
    }
}
