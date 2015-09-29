package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum TooltipLmlTagAttributeParser implements LmlTagAttributeParser {
    ALWAYS("always") {
        @Override
        protected void apply(final Tooltip<?> tooltip, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            tooltip.setAlways(LmlAttributes.parseBoolean(tooltip.getActor(), parser, attributeValue));
        }
    },
    INSTANT("instant") {
        @Override
        protected void apply(final Tooltip<?> tooltip, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            tooltip.setInstant(LmlAttributes.parseBoolean(tooltip.getActor(), parser, attributeValue));
        }
    };
    private final String[] aliases;

    private TooltipLmlTagAttributeParser(final String... aliases) {
        this.aliases = aliases;
    }

    @Override
    public void apply(final Object actor, final LmlParser parser, final String attributeValue,
            final LmlTagData lmlTagData) {
        apply((Tooltip<?>) ((Table) actor).getUserObject(), parser, attributeValue, lmlTagData);
    }

    protected abstract void apply(Tooltip<?> tooltip, LmlParser parser, String attributeValue, LmlTagData lmlTagData);

    @Override
    public String[] getAttributeNames() {
        return aliases;
    }
}