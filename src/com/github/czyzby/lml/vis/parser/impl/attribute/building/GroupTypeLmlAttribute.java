package com.github.czyzby.lml.vis.parser.impl.attribute.building;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlBuildingAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.vis.parser.impl.tag.builder.DragPaneLmlActorBuilder;
import com.github.czyzby.lml.vis.parser.impl.tag.builder.DragPaneLmlActorBuilder.GroupType;

/** Allows to choose which group type will be used by the drag pane widget. Expects a string matching (case ignored) a
 * constant from {@link GroupType} enum. Mapped to "type".
 *
 * @author MJ */
public class GroupTypeLmlAttribute implements LmlBuildingAttribute<DragPaneLmlActorBuilder> {
    @Override
    public Class<DragPaneLmlActorBuilder> getBuilderType() {
        return DragPaneLmlActorBuilder.class;
    }

    @Override
    public boolean process(final LmlParser parser, final LmlTag tag, final DragPaneLmlActorBuilder builder,
            final String rawAttributeData) {
        try {
            final GroupType type = GroupType.valueOf(parser.parseString(rawAttributeData).toUpperCase());
            if (type != null) {
                builder.setGroupType(type);
            } else {
                parser.throwErrorIfStrict(
                        "Invalid group type. Expected name of GroupType enum constant, got: " + rawAttributeData);
            }
        } catch (final Exception exception) {
            parser.throwErrorIfStrict(
                    "Invalid group type. Expected name of GroupType enum constant, got: " + rawAttributeData,
                    exception);
        }
        return FULLY_PARSED;
    }
}
