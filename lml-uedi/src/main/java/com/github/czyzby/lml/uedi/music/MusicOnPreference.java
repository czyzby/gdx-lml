package com.github.czyzby.lml.uedi.music;

import com.github.czyzby.lml.uedi.music.impl.AbstractTogglePreference;

/** Allows to turn the music on and off.
 *
 * @author MJ */
public class MusicOnPreference extends AbstractTogglePreference {
    @Override
    public String getKey() {
        return "musicOn";
    }
}
