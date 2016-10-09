package com.github.czyzby.tests.reflected.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.impl.tag.actor.TextAreaLmlTag;
import com.github.czyzby.lml.parser.impl.tag.builder.TextLmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.parser.tag.LmlTagProvider;
import com.kotcrab.vis.ui.widget.HighlightTextArea;

/**
 * Based on {@link HighlightTextArea} from VisUI. Allows to display LML code snippets with basic syntax highlighting.
 * @author MJ
 */
public class CodeTextArea extends HighlightTextArea {
    public CodeTextArea(String text, Skin skin, String styleName) {
        super(text, skin.get(styleName, VisTextFieldStyle.class));
        setHighlighter(GdxUtilities.isRunningOnGwt() ? new MockHighlighter() : new LmlSourceHighlighter());
        setFocusBorderEnabled(false);
    }

    /**
     * Provides {@link CodeTextArea} tags.
     * @author Kotcrab
     */
    public static class CodeTextAreaLmlTagProvider implements LmlTagProvider {
        @Override
        public LmlTag create(final LmlParser parser, final LmlTag parentTag, final StringBuilder rawTagData) {
            return new CodeTextAreaLmlTag(parser, parentTag, rawTagData);
        }
    }

    /**
     * Handles {@link CodeTextArea} actor.
     * @author MJ
     */
    public static class CodeTextAreaLmlTag extends AbstractNonParentalActorLmlTag {
        public CodeTextAreaLmlTag(final LmlParser parser, final LmlTag parentTag, final StringBuilder rawTagData) {
            super(parser, parentTag, rawTagData);
        }

        @Override
        protected LmlActorBuilder getNewInstanceOfBuilder() {
            return new TextLmlActorBuilder();
        }

        @Override
        protected Actor getNewInstanceOfActor(LmlActorBuilder builder) {
            TextLmlActorBuilder textBuilder = (TextLmlActorBuilder) builder;
            return new CodeTextArea(textBuilder.getText(), getSkin(textBuilder), textBuilder.getStyleName());
        }
    }

    /**
     * Allows to set text area's prompt with "message" attribute.
     * @author MJ
     */
    public static class AreaMessageLmlAttribute implements LmlAttribute<CodeTextArea> {
        @Override
        public Class<CodeTextArea> getHandledType() {
            return CodeTextArea.class;
        }

        @Override
        public void process(LmlParser parser, LmlTag tag, CodeTextArea codeTextArea, String rawAttributeData) {
            codeTextArea.setMessageText(parser.parseString(rawAttributeData, codeTextArea));
        }
    }
}
