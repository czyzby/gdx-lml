package com.github.czyzby.lml.parser.impl.attribute.image;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Allows to set tiled drawable obtained from the default skin to the {@link Image}.
 * Mapped to "tiledDrawable", "tiled".
 *
 * @see Skin#getTiledDrawable(String)
 * @author Metaphore */
public class ImageTiledDrawableLmlAttribute implements LmlAttribute<Image> {
    @Override
    public Class<Image> getHandledType() {
        return Image.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Image actor, final String rawAttributeData) {
        Skin skin = parser.getData().getDefaultSkin();
        TiledDrawable drawable = skin.getTiledDrawable(parser.parseString(rawAttributeData, actor));
        actor.setDrawable(drawable);
    }
}
