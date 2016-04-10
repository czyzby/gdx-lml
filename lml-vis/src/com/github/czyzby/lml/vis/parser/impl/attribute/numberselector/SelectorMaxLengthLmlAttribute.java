package com.github.czyzby.lml.vis.parser.impl.attribute.numberselector;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.NumberSelector;

/** See {@link NumberSelector#setMaxLength(int)}. Mapped to "maxLength".
 *
 * @author MJ */
public class SelectorMaxLengthLmlAttribute implements LmlAttribute<NumberSelector> {
    @Override
    public Class<NumberSelector> getHandledType() {
        return NumberSelector.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final NumberSelector actor,
            final String rawAttributeData) {
        actor.setMaxLength(parser.parseInt(rawAttributeData, actor));
    }
}
