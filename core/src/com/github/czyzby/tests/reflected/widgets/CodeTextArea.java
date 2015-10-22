package com.github.czyzby.tests.reflected.widgets;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.builder.TextLmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.parser.tag.LmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.VisTextAreaLmlTag;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextField;

/**
 * Custom {@link VisTextArea} used to display LML code in main view. Supports embedding in scrollpane
 * by calculating required space needed for current text.
 * @author Kotcrab
 */
public class CodeTextArea extends VisTextArea {
    private GlyphLayout prefSizeLayout;
    private String lastText;

    public CodeTextArea(String text, String styleName) {
        super(text, styleName);
    }

    @Override
    protected void initialize() {
        super.initialize();
        prefSizeLayout = new GlyphLayout();
    }

    @Override
    public float getPrefHeight() {
        updatePrefSizeLayoutIfNeeded();
        return prefSizeLayout.height + super.getPrefHeight();
    }

    @Override
    public float getPrefWidth() {
        updatePrefSizeLayoutIfNeeded();
        return prefSizeLayout.width + super.getPrefHeight();
    }

    private void updatePrefSizeLayoutIfNeeded() {
        final String text = getText();
        if (lastText != text) {
            prefSizeLayout.setText(getStyle().font, text);
            lastText = text;
        }
    }

    public static class CodeTextAreaLmlTagProvider implements LmlTagProvider {
        @Override
        public LmlTag create(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
            return new CodeTextAreaLmlTag(parser, parentTag, rawTagData);
        }
    }

    public static class CodeTextAreaLmlTag extends VisTextAreaLmlTag {
        public CodeTextAreaLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
            super(parser, parentTag, rawTagData);
        }

        protected VisTextField getNewInstanceOfTextField(final TextLmlActorBuilder textBuilder) {
            return new CodeTextArea(textBuilder.getText(), textBuilder.getStyleName());
        }
    }
}
