package com.github.czyzby.lml.parser.impl.attribute.container;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUtilities;

/** See {@link Container#padTop(Value)} . See
 * {@link LmlUtilities#parseHorizontalValue(LmlParser, LmlTag, com.badlogic.gdx.scenes.scene2d.Actor, String)} and
 * {@link LmlUtilities#parseVerticalValue(LmlParser, LmlTag, com.badlogic.gdx.scenes.scene2d.Actor, String)} for more
 * info on value parsing. To avoid collision with cell attributes, this attribute is mapped to "containerPadTop".
 *
 * @author Metaphore */
public class ContainerPadTopLmlAttribute implements LmlAttribute<Container<?>> {
    @Override
    public Class<Container<?>> getHandledType() {
        // Double cast as there were a problem with generics - SomeClass.class cannot be returned as
        // <Class<SomeClass<?>>, even though casting never throws ClassCastException in the end.
        return (Class<Container<?>>) (Object) Container.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Container<?> actor, final String rawAttributeData) {
        final Value verticalPad = LmlUtilities.parseVerticalValue(parser, tag, actor, rawAttributeData);
        actor.padTop(verticalPad);
    }
}
