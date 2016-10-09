package com.github.czyzby.lml.uedi.music;

import com.github.czyzby.lml.uedi.music.impl.AbstractTogglePreference;

/** Allows to turn the sounds on and off.
 *
 * @author MJ */
public class SoundOnPreference extends AbstractTogglePreference {
    @Override
    public String getKey() {
        return "soundOn";
    }
}
