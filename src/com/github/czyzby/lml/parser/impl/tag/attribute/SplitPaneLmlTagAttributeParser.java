package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum SplitPaneLmlTagAttributeParser implements LmlTagAttributeParser {
    FILL_PARENT("fillParent") {
        @Override
        public void apply(final SplitPane splitPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            splitPane.setFillParent(LmlAttributes.parseBoolean(splitPane, parser, attributeValue));
        }
    },
    SPLIT_AMOUNT("splitAmount", "split") {
        @Override
        protected void apply(final SplitPane splitPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            splitPane.setSplitAmount(LmlAttributes.parseFloat(splitPane, parser, attributeValue));
        }
    },
    MIN_SPLIT_AMOUNT("minSplitAmount", "minSplit") {
        @Override
        protected void apply(final SplitPane splitPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            splitPane.setMinSplitAmount(LmlAttributes.parseFloat(splitPane, parser, attributeValue));
        }
    },
    MAX_SPLIT_AMOUNT("maxSplitAmount", "maxSplit") {
        @Override
        protected void apply(final SplitPane splitPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            splitPane.setMaxSplitAmount(LmlAttributes.parseFloat(splitPane, parser, attributeValue));
        }
    };
    private final String[] aliases;

    private SplitPaneLmlTagAttributeParser(final String... aliases) {
        this.aliases = aliases;
    }

    @Override
    public void apply(final Object actor, final LmlParser parser, final String attributeValue,
            final LmlTagData lmlTagData) {
        apply((SplitPane) actor, parser, attributeValue, lmlTagData);
    }

    protected abstract void apply(SplitPane splitPane, LmlParser parser, String attributeValue, LmlTagData lmlTagData);

    @Override
    public String[] getAttributeNames() {
        return aliases;
    }
}
