package com.github.czyzby.lml.parser.impl.attribute.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlBuildingAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Chooses widget's style name (as it appears in Skin). Expects a string. By default, mapped to "style" and "class"
 * attribute names.
 *
 * @author MJ */
public class StyleLmlAttribute implements LmlBuildingAttribute<LmlActorBuilder> {
    @Override
    public Class<?>[] getHandledTypes() {
        return new Class<?>[] { Actor.class };
    }

    @Override
    public boolean process(final LmlParser parser, final LmlTag tag, final LmlActorBuilder builder,
            final String rawAttributeData) {
        builder.setStyleName(parser.parseString(rawAttributeData));
        return FULLY_PARSED;
    }
}
