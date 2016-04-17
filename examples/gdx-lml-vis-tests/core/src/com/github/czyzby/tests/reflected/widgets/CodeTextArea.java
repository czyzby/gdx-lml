package com.github.czyzby.tests.reflected.widgets;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.builder.TextLmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.parser.tag.LmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.VisTextAreaLmlTag;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextField;

/** Custom {@link VisTextArea} used to display LML code in main view. Supports embedding in scrollpane by calculating
 * required space needed for current text.
 *
 * @author Kotcrab */
public class CodeTextArea extends VisTextArea {
    private GlyphLayout prefSizeLayout;
    private String lastText;

    public CodeTextArea(final String text, final String styleName) {
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

    @Override
    public void setText (String str) {
        // TextArea seems to have problem when '\r\n' (Windows style) is used as line ending which
        // adds additional new line. Although example templates are using '\n' Git when cloning repository
        // may replace them with '\r\n' on Windows.
        super.setText(str.replace("\r", ""));
    }

    private void updatePrefSizeLayoutIfNeeded() {
        final String text = getText();
        // not using equals here because we only care if text has changed and strings are immutable
        if (lastText != text) {
            prefSizeLayout.setText(getStyle().font, text);
            lastText = text;
        }
    }

    /** Provides {@link CodeTextArea} tags.
     *
     * @author Kotcrab */
    public static class CodeTextAreaLmlTagProvider implements LmlTagProvider {
        @Override
        public LmlTag create(final LmlParser parser, final LmlTag parentTag, final StringBuilder rawTagData) {
            return new CodeTextAreaLmlTag(parser, parentTag, rawTagData);
        }
    }

    /** Handles {@link CodeTextArea} actor.
     *
     * @author Kotcrab */
    public static class CodeTextAreaLmlTag extends VisTextAreaLmlTag {
        public CodeTextAreaLmlTag(final LmlParser parser, final LmlTag parentTag, final StringBuilder rawTagData) {
            super(parser, parentTag, rawTagData);
        }

        @Override
        protected VisTextField getNewInstanceOfTextField(final TextLmlActorBuilder textBuilder) {
            return new CodeTextArea(textBuilder.getText(), textBuilder.getStyleName());
        }
    }
}
