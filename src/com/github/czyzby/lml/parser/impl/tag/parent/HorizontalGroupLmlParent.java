package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public class HorizontalGroupLmlParent extends AbstractLmlParent<HorizontalGroup> {
    public HorizontalGroupLmlParent(final LmlTagData tagData, final HorizontalGroup actor, final LmlParent<?> parent,
            final LmlParser parser) {
        super(tagData, actor, parent, parser);
    }

    @Override
    public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
        actor.addActor(child);
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
