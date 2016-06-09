package com.github.czyzby.lml.uedi.music;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.preference.ApplicationPreferences;
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
    // Music utilities:
    private Music currentTheme;
    private final float duration = 0.5f;
    private final Array<Music> themes = GdxArrays.newArray();
    private final OnCompletionListener listener = new OnCompletionListener() {
        @Override
        public void onCompletion(final Music music) {
            music.setOnCompletionListener(null);
            if (music == currentTheme) {
                changeTheme();
            }
        }
    };

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
        // Allows to query current music settings in LML templates.
        data.addActorConsumer("isMusicOn", new ActorConsumer<Boolean, Object>() {
            @Override
            public Boolean consume(final Object actor) {
                return isMusicOn();
            }
        });
        data.addActorConsumer("isSoundOn", new ActorConsumer<Boolean, Object>() {
            @Override
            public Boolean consume(final Object actor) {
                return isSoundOn();
            }
        });
        data.addActorConsumer("getMusicVolume", new ActorConsumer<Float, Object>() {
            @Override
            public Float consume(final Object actor) {
                return getMusicVolume();
            }
        });
        data.addActorConsumer("getSoundVolume", new ActorConsumer<Float, Object>() {
            @Override
            public Float consume(final Object actor) {
                return getSoundVolume();
            }
        });
        // Allows to turn the music on and off.
        data.addActorConsumer("setMusicOn", new ActorConsumer<Void, Button>() {
            @Override
            public Void consume(final Button actor) {
                setMusicOn(actor.isChecked());
                return null;
            }
        });
        // Allows to turn the sounds on and off.
        data.addActorConsumer("setSoundOn", new ActorConsumer<Void, Button>() {
            @Override
            public Void consume(final Button actor) {
                setSoundOn(actor.isChecked());
                return null;
            }
        });
        // Changes current music volume (aimed at sliders):
        data.addActorConsumer("setMusicVolume", new ActorConsumer<Void, ProgressBar>() {
            @Override
            public Void consume(final ProgressBar actor) {
                setMusicVolume(actor.getValue());
                return null;
            }
        });
        // Changes current sounds volume (aimed at sliders):
        data.addActorConsumer("setSoundVolume", new ActorConsumer<Void, ProgressBar>() {
            @Override
            public Void consume(final ProgressBar actor) {
                setSoundVolume(actor.getValue());
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

    /** @return true if music are currently on. */
    public boolean isMusicOn() {
        return musicOn.isOn();
    }

    /** @param on true to turn on, false to turn off. */
    public void setMusicOn(final boolean on) {
        musicOn.setOn(on);
        if (on) {
            currentTheme = getNextTheme(null);
            fadeInCurrentTheme();
        } else {
            fadeOutCurrentTheme();
        }
    }

    /** @return current music volume in range of [0, 1]. */
    public float getMusicVolume() {
        return musicVolume.getPercent();
    }

    /** @param volume should be in range of [0, 1]. */
    public void setMusicVolume(final float volume) {
        musicVolume.setPercent(volume);
        if (currentTheme != null) {
            currentTheme.setVolume(volume);
        }
    }

    /** @param music is music is turned on, will be smoothly started and played. Replaces current music theme, if any.
     *            Will be played once with the current volume setting. */
    public void play(final Music music) {
        if (musicOn.isOn()) {
            fadeOutCurrentTheme();
            currentTheme = music;
            fadeInCurrentTheme();
        }
    }

    /** Smoothly fades out current theme. */
    protected void fadeOutCurrentTheme() {
        if (currentTheme != null && currentTheme.isPlaying()) {
            currentTheme.setOnCompletionListener(null);
            stage.addAction(Actions.sequence(
                    VolumeAction.setVolume(currentTheme, currentTheme.getVolume(), 0f, duration, Interpolation.fade),
                    MusicStopAction.stop(currentTheme)));
        }
        currentTheme = null;
    }

    /** Smoothly fades in current theme. */
    protected void fadeInCurrentTheme() {
        if (currentTheme != null) {
            currentTheme.setLooping(false);
            currentTheme.setVolume(0f);
            currentTheme.setOnCompletionListener(listener);
            stage.addAction(
                    VolumeAction.setVolume(currentTheme, 0f, musicVolume.getPercent(), duration, Interpolation.fade));
            currentTheme.play();
        }
    }

    /** Forces a change of current theme. Assumes a theme was already played - there are no smooth fade ins of any
     * kind. */
    protected void changeTheme() {
        currentTheme = getNextTheme(currentTheme);
        if (currentTheme != null) {
            currentTheme.setLooping(false);
            currentTheme.setOnCompletionListener(listener);
            currentTheme.setVolume(musicVolume.getPercent());
            currentTheme.play();
        }
    }

    /** @param previous was played previously.
     * @return the new music theme. */
    protected Music getNextTheme(final Music previous) {
        return themes.size == 1 ? themes.first() : themes.random();
    }

    /** @param theme will be added to the current themes collection, which are constantly played on a loop. By default,
     *            themes order is random. If no theme is currently played, this music instance will start playing
     *            smoothly. */
    public void addTheme(final Music theme) {
        if (!themes.contains(theme, true)) {
            themes.add(theme);
        }
        if (currentTheme == null && musicOn.isOn()) {
            currentTheme = theme;
            fadeInCurrentTheme();
        }
    }

    /** @param themes will be added to the current themes collection, which are constantly played on a loop. By default,
     *            themes order is random. If no theme is currently played, one of the passed music instances will start
     *            playing smoothly. */
    public void addThemes(final Music... themes) {
        for (final Music theme : themes) {
            if (!this.themes.contains(theme, true)) {
                this.themes.add(theme);
            }
        }
        if (currentTheme == null && musicOn.isOn()) {
            currentTheme = getNextTheme(null);
            fadeInCurrentTheme();
        }
    }

    /** @param themes will be added to the current themes collection, which are constantly played on a loop. By default,
     *            themes order is random. If no theme is currently played, one of the passed music instances will start
     *            playing smoothly. */
    public void addThemes(final Iterable<Music> themes) {
        for (final Music theme : themes) {
            if (!this.themes.contains(theme, true)) {
                this.themes.add(theme);
            }
        }
        if (currentTheme == null && musicOn.isOn()) {
            currentTheme = getNextTheme(null);
            fadeInCurrentTheme();
        }
    }

    /** @param theme will be removed from the current themes collection. If is currently played, will be smoothly
     *            stopped and replaced with another theme (if any are left). */
    public void removeTheme(final Music theme) {
        themes.removeValue(theme, true);
        if (currentTheme == theme) {
            fadeOutCurrentTheme();
            currentTheme = getNextTheme(theme);
            fadeInCurrentTheme();
        }
    }

    /** @param themes will be removed from the current themes collection. If one of them is currently played, it will be
     *            smoothly stopped and replaced with another theme (if any are left). */
    public void removeThemes(final Music... themes) {
        boolean removeCurrent = false;
        for (final Music theme : themes) {
            this.themes.removeValue(theme, true);
            removeCurrent |= currentTheme == theme;
        }
        if (removeCurrent) {
            final Music nextTheme = getNextTheme(currentTheme);
            fadeOutCurrentTheme();
            currentTheme = nextTheme;
            fadeInCurrentTheme();
        }
    }

    /** @param themes will be removed from the current themes collection. If one of them is currently played, it will be
     *            smoothly stopped and replaced with another theme (if any are left). */
    public void removeThemes(final Iterable<Music> themes) {
        boolean removeCurrent = false;
        for (final Music theme : themes) {
            this.themes.removeValue(theme, true);
            removeCurrent |= currentTheme == theme;
        }
        if (removeCurrent) {
            final Music nextTheme = getNextTheme(currentTheme);
            fadeOutCurrentTheme();
            currentTheme = nextTheme;
            fadeInCurrentTheme();
        }
    }

    /** Removes all themes. Stops currently played music instance. */
    public void clearThemes() {
        themes.clear();
        fadeOutCurrentTheme();
    }

    /** Saves music preferences. Will flush the global preferences: any other settings will be permanently saved. */
    public void savePreferences() {
        ApplicationPreferences.getPreferences().flush();
    }
}
