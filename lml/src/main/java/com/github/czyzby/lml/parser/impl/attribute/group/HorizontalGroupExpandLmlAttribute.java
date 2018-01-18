package com.github.czyzby.lml.parser.impl.attribute.group;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** See {@link HorizontalGroup#expand(boolean)}. Mapped to "groupExpand".
 *
 * @author Metaphore */
public class HorizontalGroupExpandLmlAttribute implements LmlAttribute<HorizontalGroup> {
    @Override
    public Class<HorizontalGroup> getHandledType() {
        return HorizontalGroup.class;
    }

    @Override
    public void process(LmlParser parser, LmlTag tag, HorizontalGroup actor, String rawAttributeData) {
        actor.expand(parser.parseBoolean(rawAttributeData));
    }
}
