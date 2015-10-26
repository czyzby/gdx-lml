package com.github.czyzby.lml.vis.parser.impl.attribute.numberselector;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.NumberSelector;

/**
 * See {@link NumberSelector#setPrecision(int)}. By default mapped to "precision".
 * @author Kotcrab
 */
public class PrecisionLmlAttribute implements LmlAttribute<NumberSelector> {
    @Override
    public Class<NumberSelector> getHandledType() {
        return NumberSelector.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final NumberSelector actor,
            final String rawAttributeData) {
        actor.setPrecision(parser.parseInt(rawAttributeData, actor));
    }
}
