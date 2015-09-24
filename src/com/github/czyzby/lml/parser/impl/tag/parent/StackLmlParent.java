package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public class StackLmlParent extends AbstractLmlParent<Stack> {
    public StackLmlParent(final LmlTagData tagData, final Stack actor, final LmlParent<?> parent,
            final LmlParser parser) {
        super(tagData, actor, parent, parser);
    }

    @Override
    public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
        actor.add(child);
    }

    @Override
    public void doOnTagClose(final LmlParser parser) {
        actor.pack();
    }

    @Override
    protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
        handleChild(getLabelFromRawDataBetweenTags(data, parser), null, parser);
    }
}