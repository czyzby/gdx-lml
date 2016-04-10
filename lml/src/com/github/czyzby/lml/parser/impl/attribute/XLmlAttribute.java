package com.github.czyzby.lml.parser.impl.attribute;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUtilities;

/** Allows to specify initial position of the actor on the stage. Works ONLY if the actor is a root - has no parent and
 * is attached to a stage by the parser, not manually. Expends either a float (absolute position) or a float ending with
 * a '%' (for example, "0.5%"). If a percent is passed, it will set the X position as a percent of stage's initial
 * width. For the most predictable results, set both x and y attributes. By default, mapped to "x", "positionX", "posX",
 * "xPos", "xPosition".
 *
 * @author MJ
 * @see YLmlAttribute */
public class XLmlAttribute implements LmlAttribute<Actor> {
    @Override
    public Class<Actor> getHandledType() {
        return Actor.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Actor actor, final String rawAttributeData) {
        LmlUtilities.getLmlUserObject(actor).parseXPosition(parser, actor, rawAttributeData);
    }
}
