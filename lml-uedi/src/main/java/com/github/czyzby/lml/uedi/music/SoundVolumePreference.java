package com.github.czyzby.lml.uedi.music;

import com.github.czyzby.lml.uedi.music.impl.AbstractPercentPreference;

/** Allows to manage current volume of sounds.
 *
 * @author MJ */
public class SoundVolumePreference extends AbstractPercentPreference {
    @Override
    public String getKey() {
        return "soundVolume";
    }
}
