package com.github.czyzby.lml.uedi.i18n.impl;

import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.reflect.Field;
import com.github.czyzby.lml.uedi.assets.impl.AssetInjection;

/** Contains necessary data to inject a {@link I18NBundle} into a field.
 *
 * @author MJ */
public class BundleInjection extends AssetInjection {
    /** @param path path to the bundle.
     * @param field reference to the field that needs to be filled.
     * @param owner instance of the class containing the field. */
    public BundleInjection(final String path, final Field field, final Object owner) {
        super(path, I18NBundle.class, field, owner);
    }
}
