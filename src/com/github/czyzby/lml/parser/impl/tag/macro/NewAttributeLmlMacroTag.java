package com.github.czyzby.lml.parser.impl.tag.macro;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.impl.tag.AbstractMacroLmlTag;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** You can register attributes using LML templates. To be completely honest, there is hardly any advantage over
 * implementing custom {@link LmlAttribute} and adding it to the LML syntax object, except doing it in LML does require
 * less code and set-up. Macro requires exactly two attributes: an array of attribute aliases and a method that consumes
 * {@link AttributeParsingData}. AttributeParsingData basically contains the everything that
 * {@link LmlAttribute#process(LmlParser, LmlTag, Object, String)} method gets as its arguments. Let's say we want to
 * define an argument that sets upper-cased text on a label:
 *
 * <blockquote>
 *
 * <pre>
 * public void setUpperCaseText(AttributeParsingData data) {
 *     if (data.getActor() instanceof Label) {
 *         Label label = (Label) data.getActor();
 *         String text = data.getParser().parseString(data.getRawAttributeData(), label).toUpperCase();
 *         label.setText(text);
 *     }
 * }
 * </pre>
 *
 * </blockquote> Now you can register it in the templates an enjoy the new attribute: <blockquote>
 *
 * <pre>
 * &lt;newAttribute upper;upperCase setUpperCaseText&gt;
 * &lt;label upper="value" /&gt;
 * &lt;label upperCase=@bundleLine&gt;
 * </pre>
 *
 * </blockquote>The first label will have "VALUE" as its text; the second will find "bundleLine" in the default i18n
 * bundle, convert it to upper case and set the result as its text.
 *
 * <p>
 * For the curious: this is a class with the same functionality (except now it actually applies only to labels, so we do
 * not have to worry about classes, check type or do any casting). You would have to manually pass an instance of this
 * class to LmlSyntax object to register it, though, so that's an extra Java code line.<blockquote>
 *
 * <pre>
 * public class UpperCaseLmlAttribute implements LmlAttribute&lt;Label&gt; {
 *     &#64;Override
 *     public Class&lt;Label&gt; getHandledType() {
 *         return Label.class;
 *     }
 *
 *     &#64;Override
 *     public void process(LmlParser parser, LmlTag tag, Label actor, String rawAttributeData) {
 *         actor.setText(parser.parseString(rawAttributeData, actor).toUpperCase());
 *     }
 * }
 * </pre>
 *
 * </blockquote>
 *
 * @author MJ */
public class NewAttributeLmlMacroTag extends AbstractMacroLmlTag {
    public NewAttributeLmlMacroTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    public void handleDataBetweenTags(final String rawData) {
    }

    @Override
    public void closeTag() {
        final Array<String> attributes = getAttributes();
        if (GdxArrays.sizeOf(attributes) < 2) {
            getParser().throwError(
                    "Unable to create a new atrribute with less than two macro attributes: attribute aliases array and setter method consuming AttributeParsingData.");
        }
        // Possible tag names:
        final String[] tagNames = getParser().parseArray(attributes.first(), getActor());
        // Processes attribute parsing:
        final ActorConsumer<?, AttributeParsingData> parser = getParser().parseAction(attributes.get(1),
                new AttributeParsingData());
        if (parser == null) {
            getParser().throwError(
                    "Unable to add new attribute. Action consuming AttributeParsingData not found for name: "
                            + attributes.get(1));
        }
        getParser().getSyntax().addAttributeProcessor(new CustomLmlAttribute(parser), tagNames);
    }

    /** Allows to register new attributes from within LML templates using new attribute macro.
     *
     * @author MJ */
    public static class CustomLmlAttribute implements LmlAttribute<Actor> {
        private final ActorConsumer<?, AttributeParsingData> attributeParser;

        /** @param attributeParser wraps around a method that does the actual attribute parsing. */
        public CustomLmlAttribute(final ActorConsumer<?, AttributeParsingData> attributeParser) {
            this.attributeParser = attributeParser;
        }

        @Override
        public Class<Actor> getHandledType() {
            return Actor.class;
        }

        @Override
        public void process(final LmlParser parser, final LmlTag tag, final Actor actor,
                final String rawAttributeData) {
            attributeParser.consume(new AttributeParsingData(parser, tag, actor, rawAttributeData));
        }

    }

    /** Contains attribute parsing data normally passed as arguments to
     * {@link LmlAttribute#process(LmlParser, LmlTag, Object, String)} method.
     *
     * @author MJ */
    public static class AttributeParsingData {
        private final LmlParser parser;
        private final LmlTag tag;
        private final Actor actor;
        private final String rawAttributeData;

        /** Utility private constructor. */
        private AttributeParsingData() {
            this(null, null, null, null);
        }

        /** @param parser handles LML template parsing.
         * @param tag contains raw tag data. Allows to access actor's parent.
         * @param actor handled actor instance.
         * @param rawAttributeData unparsed LML attribute data that should be handled by an attribute processor. Common
         *            data types (string, int, float, boolean, action) are already handled by LML parser implementation,
         *            so make sure to invoke its methods. */
        public AttributeParsingData(final LmlParser parser, final LmlTag tag, final Actor actor,
                final String rawAttributeData) {
            this.parser = parser;
            this.tag = tag;
            this.actor = actor;
            this.rawAttributeData = rawAttributeData;
        }

        /** @return handles LML template parsing. */
        public LmlParser getParser() {
            return parser;
        }

        /** @return contains raw tag data. Allows to access actor's parent. */
        public LmlTag getTag() {
            return tag;
        }

        /** @return handled actor instance. Should have the attribute property set. */
        public Actor getActor() {
            return actor;
        }

        /** @return unparsed LML attribute data that should be handled by an attribute processor. Common data types
         *         (string, int, float, boolean, action) are already handled by LML parser implementation, so make sure
         *         to invoke its methods. */
        public String getRawAttributeData() {
            return rawAttributeData;
        }
    }
}
