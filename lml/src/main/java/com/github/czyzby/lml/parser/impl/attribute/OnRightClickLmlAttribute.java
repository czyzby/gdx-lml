package com.github.czyzby.lml.parser.impl.attribute;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Attaches a ClickListener to the the actor, invoking a chosen action upon right button clicking on actor. Expects an action ID.
 * Assigned action method might have {@link Params} as parameter to retrieve some event related data.
 * By default, mapped to "onRightClick" and "rightClick" attribute names.
 *
 * @author Metaphore */
public class OnRightClickLmlAttribute implements LmlAttribute<Actor> {

    @Override
    public Class<Actor> getHandledType() {
        return Actor.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Actor actor, final String rawAttributeData) {
        final ActorConsumer<?, Params> action = parser.parseAction(rawAttributeData, tmpParams);
        if (action == null) {
            parser.throwError("Could not find action for: " + rawAttributeData + " with actor: " + actor);
        }
        actor.addListener(new ClickListener(1) {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                tmpParams.actor = actor;
                tmpParams.x = x;
                tmpParams.y = y;
                tmpParams.stageX = event.getStageX();
                tmpParams.stageY = event.getStageY();
                action.consume(tmpParams);
                tmpParams.reset();
            }
        });
    }

    private static Params tmpParams = new Params();
    public static class Params {
        /** An actor that is being clicked. */
        public Actor actor;
        /** Click position in actor's local coordinates. */
        public float x, y;
        /** Click position in stage's coordinates. */
        public float stageX, stageY;

        public void reset() {
            actor = null;
        }
    }
}
