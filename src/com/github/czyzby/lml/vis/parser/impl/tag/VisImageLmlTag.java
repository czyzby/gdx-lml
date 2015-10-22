package com.github.czyzby.lml.vis.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.actor.ImageLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.VisImage;

/**
 * Handles {@link VisImage} actor. Mapped to "image", "img", "icon".
 * @author Kotcrab
 */
public class VisImageLmlTag extends ImageLmlTag {
    public VisImageLmlTag(LmlParser parser, LmlTag parentTag, String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(LmlActorBuilder builder) {
        return new VisImage(getSkin(builder).getDrawable(builder.getStyleName()));
    }

    @Override
    protected Class<?> getActorType() {
        return VisImage.class;
    }
}
