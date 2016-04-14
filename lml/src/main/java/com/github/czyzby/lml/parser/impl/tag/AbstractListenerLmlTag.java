package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.macro.util.Equation;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.scene2d.ui.reflected.ActorStorage;
import com.github.czyzby.lml.util.LmlUtilities;

/** Base for tags that attach a listener to actors. Exploits {@link ActorStorage} utility to store a list of actors
 * without putting them in a {@link com.badlogic.gdx.scenes.scene2d.Group Group}. By default, when the event occurs,
 * child actors of this tag will be added to the stage. Note that most of its attributes are ignored.
 *
 * @author MJ */
public abstract class AbstractListenerLmlTag extends AbstractActorLmlTag {
    private String condition;

    public AbstractListenerLmlTag(final LmlParser parser, final LmlTag parentTag, final StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return new ActorStorage();
    }

    /** @return stored actor, casted to {@link ActorStorage} for convenience. */
    protected ActorStorage getActorStorage() {
        return (ActorStorage) getActor();
    }

    @Override
    public boolean isAttachable() {
        return true;
    }

    @Override
    protected void doOnTagClose() {
        if (getParent() == null) {
            getParser().throwErrorIfStrict(
                    "This tag should be attached to other actors. Listener tags produce mock-up actors and cannot be root tags.");
        }
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        getActorStorage().addActor(toLabel(plainTextLine));
    }

    @Override
    protected void handleValidChild(final LmlTag childTag) {
        getActorStorage().addActor(childTag.getActor());
    }

    @Override
    public void attachTo(final LmlTag tag) {
        attachListener(tag.getActor());
    }

    /** @param condition LML equation snippet that will be processed each time the event occurs. If returns positive
     *            result, actors will be shown on the stage. Can be null (this is actually the default value) - if null,
     *            actors are displayed on each event. */
    public void setCondition(final String condition) {
        this.condition = condition;
    }

    /** @param actor should have a listener attached. The listener should call {@link #doOnEvent(Actor)} when the event
     *            occurs. */
    protected abstract void attachListener(Actor actor);

    /** @param actor has the listener attached. Its stage will be used to display stored actors. */
    protected void doOnEvent(final Actor actor) {
        if (condition != null) {
            final boolean shouldDisplay = new Equation(getParser(), getParent().getActor()).getBooleanResult(condition);
            if (!shouldDisplay) {
                return;
            }
        }
        LmlUtilities.appendActorsToStage(determineStage(actor), getActorStorage().getActors());
    }
}
