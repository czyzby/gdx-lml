package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public class SplitPaneLmlParent extends AbstractLmlParent<SplitPane> {
    private boolean wasFirstChildAppended, wasSecondChildAppended;

    public SplitPaneLmlParent(final LmlTagData tagData, final SplitPane actor, final LmlParent<?> parent,
            final LmlParser parser) {
        super(tagData, actor, parent, parser);
    }

    @Override
    public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
        if (child == null) {
            return;
        }
        validateWidgetStatus(parser);
        if (wasFirstChildAppended) {
            wasSecondChildAppended = true;
            actor.setSecondWidget(child);
        } else {
            wasFirstChildAppended = true;
            actor.setFirstWidget(child);
        }
    }

    private void validateWidgetStatus(final LmlParser parser) {
        if (wasSecondChildAppended) {
            throwErrorIfStrict(parser, "SplitPane can have only two children.");
            // Not strict - resetting.
            wasFirstChildAppended = wasSecondChildAppended = false;
        }
    }

    @Override
    public void doOnTagClose(final LmlParser parser) {
    }

    @Override
    protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
        handleChild(getLabelFromRawDataBetweenTags(data, parser), null, parser);
    }
}
