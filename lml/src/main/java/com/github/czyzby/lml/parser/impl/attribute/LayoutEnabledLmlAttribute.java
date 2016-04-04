package com.github.czyzby.lml.parser.impl.attribute;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** See {@link Layout#setLayoutEnabled(boolean)}. By default, mapped to "layout" and "layoutEnabled" attribute names.
 *
 * @author MJ */
public class LayoutEnabledLmlAttribute implements LmlAttribute<Actor> {
    @Override
    public Class<Actor> getHandledType() {
        return Actor.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Actor actor, final String rawAttributeData) {
        if (actor instanceof Layout) {
            ((Layout) actor).setLayoutEnabled(parser.parseBoolean(rawAttributeData, actor));
        } else {
            parser.throwErrorIfStrict(
                    "Layout enabled attribute can be added only to actors that implement Layout interface. Tag: "
                            + tag.getTagName() + " with actor: " + actor + " cannot have this attribute.");
        }
    }
}
