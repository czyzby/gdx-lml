package com.github.czyzby.lml.parser.impl.attribute;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** See {@link Layout#setFillParent(boolean)}. By default, mapped to "fillParent" attribute name.
 *
 * @author MJ */
public class FillParentLmlAttribute implements LmlAttribute<Actor> {
    @Override
    public Class<Actor> getHandledType() {
        return Actor.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Actor actor, final String rawAttributeData) {
        if (actor instanceof Layout) {
            ((Layout) actor).setFillParent(parser.parseBoolean(rawAttributeData, actor));
        } else {
            parser.throwErrorIfStrict(
                    "Fill parent attribute can be added only to actors that implement Layout interface. Tag: "
                            + tag.getTagName() + " with actor: " + actor + " cannot have this attribute.");
        }
    }
}
