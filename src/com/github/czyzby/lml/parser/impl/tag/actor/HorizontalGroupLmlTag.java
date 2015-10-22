package com.github.czyzby.lml.parser.impl.tag.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Handles {@link HorizontalGroup} actor. Appends plain text between tags as labels. Mapped to "horizontal",
 * "horizontalGroup".
 *
 * @author MJ */
public class HorizontalGroupLmlTag extends AbstractActorLmlTag {
    public HorizontalGroupLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return new HorizontalGroup();
    }

    @Override
    protected void handleValidChild(final LmlTag childTag) {
        addChild(childTag.getActor());
    }

    private void addChild(final Actor child) {
        ((HorizontalGroup) getActor()).addActor(child);
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        addChild(toLabel(plainTextLine));
    }
}
