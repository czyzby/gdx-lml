package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum LabelLmlTagAttributeParser implements LmlTagAttributeParser {
    TEXT("text", "value") {
        @Override
        protected void apply(final Label label, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            label.setText(LmlAttributes.parseString(label, parser, attributeValue));
        }
    },
    ALIGN("textAlign") {
        @Override
        protected void apply(final Label label, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            label.setAlignment(LmlAttributes.parseAlignment(label, parser, attributeValue));
        }
    },
    WRAP("wrap") {
        @Override
        protected void apply(final Label label, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            label.setWrap(LmlAttributes.parseBoolean(label, parser, attributeValue));
        }
    },
    ELLIPSIS("ellipsis") {
        @Override
        protected void apply(final Label label, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            label.setEllipsis(LmlAttributes.parseBoolean(label, parser, attributeValue));
        }
    },
    MULTILINE("multiline") {
        @Override
        protected void apply(final Label label, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            label.setUserObject(LabelLmlTagAttributeParser.MULTILINE_ATTRIBUTE);
        }
    };
    public static final String MULTILINE_ATTRIBUTE = "MULTILINE";

    private final String[] aliases;

    private LabelLmlTagAttributeParser(final String... aliases) {
        this.aliases = aliases;
    }

    @Override
    public void apply(final Object actor, final LmlParser parser, final String attributeValue,
            final LmlTagData lmlTagData) {
        apply((Label) actor, parser, attributeValue, lmlTagData);
    }

    protected abstract void apply(Label label, LmlParser parser, String attributeValue, LmlTagData lmlTagData);

    @Override
    public String[] getAttributeNames() {
        return aliases;
    }
}
