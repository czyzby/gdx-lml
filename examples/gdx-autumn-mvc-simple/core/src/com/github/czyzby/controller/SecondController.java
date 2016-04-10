package com.github.czyzby.controller;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.sfx.MusicService;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;

/** This is the second application's view. It will be shown after all assets are loaded. */
@View(id = "second", value = "templates/second.lml", themes = "music/theme.mp3")
public class SecondController implements ActionContainer {
    /** {@link MusicService} manages sound settings. If you play sounds and music through it's methods, it will
     * automatically manage volume and on/off settings. */
    @Inject MusicService musicService;
    /** This is an asset that will be loaded by {@link AssetService}. You can annotate all common assets supported by
     * {@link AssetManager}, including the most commonly used (in 2D games) textures and texture atlases. */
    @Asset("music/sound.ogg") Sound sound;

    /** This is an action that will be available in second.lml template through "play" (annotation argument) and
     * "playSound" (actual method name) IDs. Since {@link SecondController} implements {@link ActionContainer}, all its
     * methods will be reflected and available in its LML template. */
    @LmlAction("play")
    public void playSound() {
        musicService.play(sound); // Uses defined sound settings, including volume.
    }
}
