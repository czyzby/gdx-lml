package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum LayoutLmlTagAttributeParser implements LmlTagAttributeParser {
    FILL_PARENT("fillParent") {
        @Override
        public void apply(final Layout actor, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            actor.setFillParent(LmlAttributes.parseBoolean((Actor) actor, parser, attributeValue));
        }
    };
    private final String[] aliases;

    private LayoutLmlTagAttributeParser(final String... aliases) {
        this.aliases = aliases;
    }

    @Override
    public void apply(final Object actor, final LmlParser parser, final String attributeValue,
            final LmlTagData lmlTagData) {
        apply((Layout) actor, parser, attributeValue, lmlTagData);
    }

    protected abstract void apply(Layout actor, LmlParser parser, String attributeValue, LmlTagData lmlTagData);

    @Override
    public String[] getAttributeNames() {
        return aliases;
    }
}
