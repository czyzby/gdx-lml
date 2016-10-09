package com.github.czyzby.lml.uedi.music;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

/** Allows to easily handle {@link Music} volume transitions.
 *
 * @author MJ */
public class VolumeAction extends TemporalAction {
    private float start, end;
    private Music music;

    @Override
    protected void begin() {
        music.setVolume(start);
    }

    @Override
    protected void update(final float percent) {
        music.setVolume(start + (end - start) * percent);
    }

    @Override
    protected void end() {
        music.setVolume(end);
        music = null;
    }

    @Override
    public void reset() {
        super.reset();
        music = null;
    }

    /** @return managed {@link Music} instance. */
    public Music getMusic() {
        return music;
    }

    /** @param music managed {@link Music} instance. */
    public void setMusic(final Music music) {
        this.music = music;
    }

    /** @return initial volume. */
    public float getStart() {
        return start;
    }

    /** @param start initial volume. */
    public void setStart(final float start) {
        this.start = start;
    }

    /** @return target volume. */
    public float getEnd() {
        return end;
    }

    /** @param end target volume. */
    public void setEnd(final float end) {
        this.end = end;
    }

    /** @param music its volume will go from 0 to 1.
     * @param duration length of the transition.
     * @return {@link VolumeAction} which should be added to a stage. */
    public static VolumeAction fadeIn(final Music music, final float duration) {
        return fadeIn(music, duration, Interpolation.fade);
    }

    /** @param music its volume will go from 0 to 1.
     * @param duration length of the transition.
     * @param interpolation handles how the volume is changed in time.
     * @return {@link VolumeAction} which should be added to a stage. */
    public static VolumeAction fadeIn(final Music music, final float duration, final Interpolation interpolation) {
        return setVolume(music, 0f, 1f, duration, interpolation);
    }

    /** @param music its volume will go from current value to 0.
     * @param duration length of the transition.
     * @return {@link VolumeAction} which should be added to a stage. */
    public static VolumeAction fadeOut(final Music music, final float duration) {
        return fadeOut(music, duration, Interpolation.fade);
    }

    /** @param music its volume will go from current value to 0.
     * @param duration length of the transition.
     * @param interpolation handles how the volume is changed in time.
     * @return {@link VolumeAction} which should be added to a stage. */
    public static VolumeAction fadeOut(final Music music, final float duration, final Interpolation interpolation) {
        return setVolume(music, music.getVolume(), 0f, duration, interpolation);
    }

    /** @param music its volume will be modified.
     * @param from starting volume value.
     * @param to target volume value.
     * @param duration length of the transition.
     * @param interpolation decides how the volume is changed in time.
     * @return {@link VolumeAction} instance which should be added to a stage. */
    public static VolumeAction setVolume(final Music music, final float from, final float to, final float duration,
            final Interpolation interpolation) {
        final VolumeAction action = Actions.action(VolumeAction.class);
        action.start = from;
        action.end = to;
        action.music = music;
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }
}
