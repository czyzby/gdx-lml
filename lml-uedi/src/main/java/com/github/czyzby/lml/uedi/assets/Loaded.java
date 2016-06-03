package com.github.czyzby.lml.uedi.assets;

/** Allows the class with assets to listen to asset injection events. Invoked each time a field is modified.
 *
 * @author MJ */
public interface Loaded {
    /** Called each time an asset is injected into the class. Note that the method requires additional checks which
     * asset was actually loaded - make sure to write null-safe code.
     *
     * @param path the exact path of the loaded asset.
     * @param type type of the loaded asset.
     * @param asset was just loaded and injected. */
    void onLoad(String path, Class<?> type, Object asset);
}
