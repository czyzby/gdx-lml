package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public class TextAreaLmlParent extends TextFieldLmlParent {
    public TextAreaLmlParent(final LmlTagData tagData, final TextField actor, final LmlParent<?> parent,
            final LmlParser parser) {
        super(tagData, actor, parent, parser);
    }

    @Override
    protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
        if (isDataNotEmpty(data) && actor.getText().length() > 0) {
            actor.appendText(String.valueOf(NEW_LINE));
        }
        super.handleValidDataBetweenTags(data, parser);
    }
}
