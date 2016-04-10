package com.github.czyzby.lml.vis.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractGroupLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.layout.HorizontalFlowGroup;

/** Handles {@link HorizontalFlowGroup} actor. Mapped to "horizontalFlow", "hFlow", "horizontalFlowGroup".
 *
 * @author MJ */
public class HorizontalFlowGroupLmlTag extends AbstractGroupLmlTag {
    public HorizontalFlowGroupLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Group getNewInstanceOfGroup(final LmlActorBuilder builder) {
        return new HorizontalFlowGroup();
    }
}
