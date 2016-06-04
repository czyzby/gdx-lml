package com.github.czyzby.lml.uedi.music;

import com.github.czyzby.lml.uedi.music.impl.AbstractPercentPreference;

/** Allows to manage current volume of music.
 *
 * @author MJ */
public class MusicVolumePreference extends AbstractPercentPreference {
    @Override
    public String getKey() {
        return "musicVolume";
    }
}
