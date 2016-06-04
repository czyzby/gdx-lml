package com.github.czyzby.lml.uedi.music;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.github.czyzby.kiwi.util.common.Exceptions;

/** Allows to stop the chosen {@link Music} instance in {@link Action}.
 *
 * @author MJ */
public class MusicStopAction extends Action {
    private Music music;

    @Override
    public boolean act(final float delta) {
        if (music != null) {
            try {
                music.stop();
            } catch (final Exception exception) {
                Exceptions.ignore(exception);
            }
            music = null;
        }
        return true;
    }

    @Override
    public void reset() {
        super.reset();
        music = null;
    }

    /** @return will be stopped. Null if already stopped. */
    public Music getMusic() {
        return music;
    }

    /** @param music will be stopped. */
    public void setMusic(final Music music) {
        this.music = music;
    }

    /** @param music will be stopped.
     * @return a {@link MusicStopAction} instance, which will stop the theme once executed. */
    public static MusicStopAction stop(final Music music) {
        final MusicStopAction action = Actions.action(MusicStopAction.class);
        action.music = music;
        return action;
    }
}
