package com.github.czyzby.lml.parser.impl.attribute.group;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** See {@link VerticalGroup#expand(boolean)}. Mapped to "groupExpand".
 *
 * @author Metaphore */
public class VerticalGroupExpandLmlAttribute implements LmlAttribute<VerticalGroup> {
    @Override
    public Class<VerticalGroup> getHandledType() {
        return VerticalGroup.class;
    }

    @Override
    public void process(LmlParser parser, LmlTag tag, VerticalGroup actor, String rawAttributeData) {
        actor.expand(parser.parseBoolean(rawAttributeData));
    }
}
