package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum ImageLmlTagAttributeParser implements LmlTagAttributeParser {
    ALIGN("imageAlign", "imgAlign") {
        @Override
        protected void apply(final Image image, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            image.setAlign(LmlAttributes.parseAlignment(image, parser, attributeValue));
        }
    },
    SCALING("scaling") {
        @Override
        protected void apply(final Image image, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            image.setScaling(Scaling.valueOf(LmlAttributes.parseString(image, parser, attributeValue)));
        }
    };

    private final String[] aliases;

    private ImageLmlTagAttributeParser(final String... aliases) {
        this.aliases = aliases;
    }

    @Override
    public void apply(final Object actor, final LmlParser parser, final String attributeValue,
            final LmlTagData lmlTagData) {
        apply((Image) actor, parser, attributeValue, lmlTagData);
    }

    protected abstract void apply(Image image, LmlParser parser, String attributeValue, LmlTagData lmlTagData);

    @Override
    public String[] getAttributeNames() {
        return aliases;
    }
}
