package com.github.czyzby.lml.parser.impl.tag.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Handles {@link Stack} actor. Appends plain text between tags as labels. Mapped to "stack".
 *
 * @author MJ */
public class StackLmlTag extends AbstractActorLmlTag {
    public StackLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return new Stack();
    }

    @Override
    protected Class<?> getActorType() {
        return Stack.class;
    }

    @Override
    protected void handleValidChild(final LmlTag childTag) {
        addChild(childTag.getActor());
    }

    /** @param actor will be added to the stack. */
    protected void addChild(final Actor actor) {
        ((Stack) getActor()).add(actor);
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        addChild(toLabel(plainTextLine));
    }
}
