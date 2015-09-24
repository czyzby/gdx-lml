package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public class TextButtonLmlParent extends TableLmlParent {
    private final StringBuilder text;

    public TextButtonLmlParent(final LmlTagData tagData, final TextButton actor, final LmlParent<?> parent,
            final LmlParser parser) {
        super(tagData, actor, parent, parser);
        text = new StringBuilder(actor.getText());
    }

    protected TextButton getActorAsTextButton() {
        return (TextButton) actor;
    }

    @Override
    protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
        text.append(parser.parseStringData(data, actor));
    }

    @Override
    public void doOnTagClose(final LmlParser parser) {
        super.doOnTagClose(parser);
        getActorAsTextButton().setText(text.toString());
    }
}
