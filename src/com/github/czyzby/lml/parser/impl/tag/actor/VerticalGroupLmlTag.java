package com.github.czyzby.lml.parser.impl.tag.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Handles {@link VerticalGroup} actor. Appends plain text between tags as labels. Mapped to "vertical",
 * "verticalGroup".
 *
 * @author MJ */
public class VerticalGroupLmlTag extends AbstractActorLmlTag {
    public VerticalGroupLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return new VerticalGroup();
    }

    @Override
    protected void handleValidChild(final LmlTag childTag) {
        addChild(childTag.getActor());
    }

    private void addChild(final Actor child) {
        ((VerticalGroup) getActor()).addActor(child);
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        addChild(toLabel(plainTextLine));
    }
}
