package com.github.czyzby.lml.parser.impl.attribute.group;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** See {@link VerticalGroup#wrap(boolean)}. Mapped to "wrap", "groupWrap".
 *
 * @author Metaphore */
public class VerticalGroupWrapLmlAttribute implements LmlAttribute<VerticalGroup> {
    @Override
    public Class<VerticalGroup> getHandledType() {
        return VerticalGroup.class;
    }

    @Override
    public void process(LmlParser parser, LmlTag tag, VerticalGroup actor, String rawAttributeData) {
        actor.wrap(parser.parseBoolean(rawAttributeData));
    }
}
