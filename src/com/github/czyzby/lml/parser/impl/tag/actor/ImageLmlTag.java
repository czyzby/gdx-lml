package com.github.czyzby.lml.parser.impl.tag.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Handles {@link Image} actor. Mapped to "image", "img", "icon".
 *
 * @author MJ */
public class ImageLmlTag extends AbstractNonParentalActorLmlTag {
    public ImageLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return new Image(getSkin(builder).getDrawable(builder.getStyleName()));
    }

    @Override
    protected Class<?> getActorType() {
        return Image.class;
    }
}
