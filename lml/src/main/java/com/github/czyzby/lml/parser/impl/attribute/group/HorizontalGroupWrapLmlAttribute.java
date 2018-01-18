package com.github.czyzby.lml.parser.impl.attribute.group;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** See {@link HorizontalGroup#wrap(boolean)}. Mapped to "groupWrap".
 *
 * @author Metaphore */
public class HorizontalGroupWrapLmlAttribute implements LmlAttribute<HorizontalGroup> {
    @Override
    public Class<HorizontalGroup> getHandledType() {
        return HorizontalGroup.class;
    }

    @Override
    public void process(LmlParser parser, LmlTag tag, HorizontalGroup actor, String rawAttributeData) {
        actor.wrap(parser.parseBoolean(rawAttributeData));
    }
}
