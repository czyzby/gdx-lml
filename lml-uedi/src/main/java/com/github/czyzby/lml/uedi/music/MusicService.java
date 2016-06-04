package com.github.czyzby.lml.uedi.music;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.czyzby.lml.parser.LmlData;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.uedi.stereotype.Singleton;

/** Allows to manage music and sounds settings. Eases playing of {@link Music} and {@link Sound} instances.
 *
 * @author MJ */
public class MusicService implements Singleton {
    private final Stage stage;
    private final MusicOnPreference musicOn;
    private final SoundOnPreference soundOn;
    private final MusicVolumePreference musicVolume;
    private final SoundVolumePreference soundVolume;

    /** @param stage will be used to add music transition actions.
     * @param musicOn decides whether music is on.
     * @param soundOn decides whether sounds are on.
     * @param musicVolume decides how loud the music is played.
     * @param soundVolume decides how loud the sounds are played. */
    public MusicService(final Stage stage, final MusicOnPreference musicOn, final SoundOnPreference soundOn,
            final MusicVolumePreference musicVolume, final SoundVolumePreference soundVolume) {
        this.stage = stage;
        this.musicOn = musicOn;
        this.soundOn = soundOn;
        this.musicVolume = musicVolume;
        this.soundVolume = soundVolume;
    }

    /** @param data will include a few useful {@link ActorConsumer} instances that allow to set up current music and
     *            sound preferences. */
    public void addDefaultActions(final LmlData data) {
        // Allows to turn the music on and off.
        data.addActorConsumer("setMusicOn", new ActorConsumer<Void, Button>() {
            @Override
            public Void consume(final Button actor) {
                musicOn.setOn(actor.isChecked());
                return null;
            }
        });
        // Allows to turn the sounds on and off.
        data.addActorConsumer("setSoundOn", new ActorConsumer<Void, Button>() {
            @Override
            public Void consume(final Button actor) {
                soundOn.setOn(actor.isChecked());
                return null;
            }
        });
        // Changes current music volume (aimed at sliders):
        data.addActorConsumer("setMusicVolume", new ActorConsumer<Void, ProgressBar>() {
            @Override
            public Void consume(final ProgressBar actor) {
                musicVolume.setPercent(actor.getValue());
                return null;
            }
        });
        // Changes current sounds volume (aimed at sliders):
        data.addActorConsumer("setSoundVolume", new ActorConsumer<Void, ProgressBar>() {
            @Override
            public Void consume(final ProgressBar actor) {
                soundVolume.setPercent(actor.getValue());
                return null;
            }
        });
    }

    /** @return true if sounds are currently on. */
    public boolean isSoundOn() {
        return soundOn.isOn();
    }

    /** @param on true to turn on, false to turn off. */
    public void setSoundOn(final boolean on) {
        soundOn.setOn(on);
    }

    /** @return current sound volume in range of [0, 1]. */
    public float getSoundVolume() {
        return soundVolume.getPercent();
    }

    /** @param volume should be in range of [0, 1]. */
    public void setSoundVolume(final float volume) {
        soundVolume.setPercent(volume);
    }

    /** @param sound will be played with the current volume setting if sounds are turned on.
     * @return the id of the sound instance if successful, -1 on failure or if sounds are turned off. */
    public long play(final Sound sound) {
        if (soundOn.isOn()) {
            return sound.play(soundVolume.getPercent());
        }
        return -1L;
    }

    /** @param sound will be played if sounds are turned on.
     * @param volume the volume in the range of [0,1]. Will multiply current sound volume setting.
     * @return the id of the sound instance if successful, -1 on failure or if sounds are turned off. */
    public long play(final Sound sound, final float volume) {
        if (soundOn.isOn()) {
            return sound.play(soundVolume.getPercent() * volume);
        }
        return -1L;
    }

    /** @param sound will be played if sounds are turned on.
     * @param volume the volume in the range of [0,1]. Will multiply current sound volume setting.
     * @param pitch the pitch multiplier. 1 stands for default, larger than 1 is faster, smaller than 1 is slower, the
     *            value should be between 0.5 and 2.0.
     * @return the id of the sound instance if successful, -1 on failure or if sounds are turned off. */
    public long play(final Sound sound, final float volume, final float pitch) {
        return play(sound, volume, pitch, 0f);
    }

    /** @param sound will be played if sounds are turned on.
     * @param volume the volume in the range of [0,1]. Will multiply current sound volume setting.
     * @param pitch the pitch multiplier. 1 stands for default, larger than 1 is faster, smaller than 1 is slower, the
     *            value should be between 0.5 and 2.0.
     * @param pan panning in the range -1 (full left) to 1 (full right). 0 is the center.
     * @return the id of the sound instance if successful, -1 on failure or if sounds are turned off. */
    public long play(final Sound sound, final float volume, final float pitch, final float pan) {
        if (soundOn.isOn()) {
            return sound.play(soundVolume.getPercent() * volume, pitch, pan);
        }
        return -1L;
    }
}
