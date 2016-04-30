package com.github.czyzby.lml.vis.parser.impl.attribute.building;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlBuildingAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.vis.parser.impl.tag.builder.NumberSelectorLmlActorBuilder;
import com.kotcrab.vis.ui.widget.NumberSelector;

/** See {@link NumberSelector#NumberSelector(String, float, float, float)}}. Mapped to "name".
 *
 * @author Kotcrab */
@SuppressWarnings("deprecation")
public class NumberSelectorNameLmlAttribute implements LmlBuildingAttribute<NumberSelectorLmlActorBuilder> {
    @Override
    public Class<NumberSelectorLmlActorBuilder> getBuilderType() {
        return NumberSelectorLmlActorBuilder.class;
    }

    @Override
    public boolean process(final LmlParser parser, final LmlTag tag, final NumberSelectorLmlActorBuilder builder,
            final String rawAttributeData) {
        builder.setName(parser.parseString(rawAttributeData));
        return FULLY_PARSED;
    }
}
