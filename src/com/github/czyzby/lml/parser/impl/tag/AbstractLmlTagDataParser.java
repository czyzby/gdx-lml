package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.LmlTagDataParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.attribute.CommonLmlTagAttributeParser;
import com.github.czyzby.lml.util.LmlSyntax;

/** Provides tag validation and common tags handling.
 *
 * @author MJ */
public abstract class AbstractLmlTagDataParser<Widget extends Actor> implements LmlTagDataParser<Widget>, LmlSyntax {
    public static final String DEFAULT_STYLE_NAME = "default";

    public static final String CLASS_ATTRIBUTE = "CLASS";
    public static final String STYLE_ATTRIBUTE = "STYLE";

    private static final ObjectMap<String, LmlTagAttributeParser> ATTRIBUTE_PARSERS;

    static {
        ATTRIBUTE_PARSERS = GdxMaps.newObjectMap();
        for (final LmlTagAttributeParser parser : CommonLmlTagAttributeParser.values()) {
            registerParser(parser);
        }
    }

    public static void registerParser(final LmlTagAttributeParser parser) {
        for (final String alias : parser.getAttributeNames()) {
            ATTRIBUTE_PARSERS.put(alias.toUpperCase(), parser);
        }
    }

    public static void unregisterParser(final String withAlias) {
        ATTRIBUTE_PARSERS.remove(withAlias);
    }

    @Override
    public Widget parseChild(final LmlTagData lmlTagData, final LmlParser parser) {
        final Widget child = parseChildWithValidTag(lmlTagData, parser);
        parseAttributes(lmlTagData, parser, child);
        return child;
    }

    protected void parseAttributes(final LmlTagData lmlTagData, final LmlParser parser, final Actor actor) {
        for (final Entry<String, String> attribute : lmlTagData.getAttributes()) {
            if (ATTRIBUTE_PARSERS.containsKey(attribute.key)) {
                ATTRIBUTE_PARSERS.get(attribute.key).apply(actor, parser, attribute.value, lmlTagData);
            }
        }
    }

    /** Actual implementation of child parsing.
     *
     * @param lmlTagData is validated.
     * @return actor parsed from tag data. */
    protected abstract Widget parseChildWithValidTag(LmlTagData lmlTagData, LmlParser parser);

    @Override
    public LmlParent<Widget> parseParent(final LmlTagData lmlTagData, final LmlParser parser,
            final LmlParent<?> parent) {
        if (lmlTagData.isClosed()) {
            throw new LmlParsingException("Invalid parser implementation. Tag is not a parent.", parser);
        }
        return parseParentWithValidTag(lmlTagData, parser, parent);
    }

    /** Actual implementation of parent parsing.
     *
     * @param lmlTagData is validated.
     * @return actor parsed from tag data with additional parental data. */
    protected abstract LmlParent<Widget> parseParentWithValidTag(LmlTagData lmlTagData, LmlParser parser,
            final LmlParent<?> parent);

    /** @return true if widget has an attribute that defines his style from skin. */
    protected boolean containsStyleAttribute(final LmlTagData lmlTagData) {
        return lmlTagData.containsAnyAttribute(STYLE_ATTRIBUTE, CLASS_ATTRIBUTE);
    }

    /** @return style name extracted from tag data. Check CLASS and STYLE attributes. Default if not set. */
    protected String getStyleName(final LmlTagData lmlTagData, final LmlParser parser) {
        String styleName = lmlTagData.getAttribute(STYLE_ATTRIBUTE);
        if (styleName == null) {
            styleName = lmlTagData.getAttribute(CLASS_ATTRIBUTE);
        }
        return styleName == null ? DEFAULT_STYLE_NAME : parser.parseStringData(styleName, null);
    }

    protected void throwErrorIfStrict(final LmlParser parser, final String message) {
        if (parser.isStrict()) {
            throw new LmlParsingException(message, parser);
        }
    }
}
