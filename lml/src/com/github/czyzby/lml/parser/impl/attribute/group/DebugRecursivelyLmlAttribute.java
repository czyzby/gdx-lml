package com.github.czyzby.lml.parser.impl.attribute.group;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** See {@link Group#setDebug(boolean, boolean)}. Second method argument is always true, so this method sets debug for
 * all children; for non-recursive debug, use default debug attribute. Mapped to "debugRecursively", "recursiveDebug",
 * "debugChildren".
 *
 * @author MJ */
public class DebugRecursivelyLmlAttribute implements LmlAttribute<Group> {
    @Override
    public Class<Group> getHandledType() {
        return Group.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Group actor, final String rawAttributeData) {
        actor.setDebug(parser.parseBoolean(rawAttributeData, actor), true);
    }
}
