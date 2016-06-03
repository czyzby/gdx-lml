package com.github.czyzby.lml.uedi.assets;

import com.badlogic.gdx.graphics.g3d.Model;
import com.github.czyzby.lml.uedi.assets.impl.AbstractAssetProvider;
import com.github.czyzby.lml.uedi.assets.impl.InjectingAssetManager;

/** Provides {@link Model} instances.
 *
 * @author MJ */
public class ModelProvider extends AbstractAssetProvider<Model> {
    public static final String[] EXTENSIONS = new String[] { "g3db", "g3dj", "obj" };

    /** @param assetManager will be used to load the assets. */
    public ModelProvider(final InjectingAssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public Class<? extends Model> getType() {
        return Model.class;
    }

    @Override
    protected String getFolder() {
        return "model";
    }

    @Override
    protected String[] getExtensions() {
        return EXTENSIONS;
    }
}
