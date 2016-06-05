package com.github.czyzby.lml.parser.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlStyleSheet;
import com.github.czyzby.lml.parser.LmlSyntax;

/** Allows to process LML style sheet code (with CSS-like syntax), extracting default attribute values of selected tags.
 *
 * <p>
 * Parser instances should be generally one-time use only - since style sheets are parsed rather rarely (usually once
 * per application run), reusing LSS parsers usually does not make much sense.
 *
 * @author MJ */
public class LssParser {
    private final LmlStyleSheet styleSheet;
    private final char inheritanceMarker = '.';
    private final char blockOpening = '{';
    private final char blockClosing = '}';
    private final char separator = ':';
    private final char lineEnd = ';';

    // Control variables:
    private String lss;
    private final StringBuilder builder = new StringBuilder();
    private int index;
    private int length;
    private int line;
    private final Array<String> tags = GdxArrays.newArray();
    private final Array<String> inherits = GdxArrays.newArray();
    private String attribute;
    private final ObjectMap<String, String> attributes = GdxMaps.newObjectMap();

    /** @param parser will be used to extract style sheet and syntax data. */
    public LssParser(final LmlParser parser) {
        this(parser.getStyleSheet(), parser.getSyntax());
    }

    /** @param styleSheet will be used to set the parsed style data.
     * @param syntax provides style sheet special characters. */
    public LssParser(final LmlStyleSheet styleSheet, final LmlSyntax syntax) {
        this.styleSheet = styleSheet;
        // TODO extract values from syntax
    }

    /** @param lss LML style sheet data. Will be parsed and processed. */
    public void parse(final String lss) {
        this.lss = lss;
        length = lss.length();
        index = 0;
        line = 1;
        while (index < length) {
            parseNames();
            parseAttributes();
            processAttributes();
            tags.clear();
            inherits.clear();
            attributes.clear();
        }
    }

    /** @param string will become the exception message proceeded by the current line number. */
    protected void throwException(final String string) {
        throw new GdxRuntimeException(line + ": " + string);
    }

    /** Parses names proceeding styles block. */
    protected void parseNames() {
        burnWhitespaces();
        for (; index < length; index++) {
            final char character = get();
            if (Strings.isWhitespace(character)) {
                addName();
                continue;
            } else if (character == blockOpening) {
                addName();
                index++;
                break;
            } else {
                builder.append(character);
            }
        }
        if (GdxArrays.isEmpty(tags)) {
            throwException("No tag names chosen.");
        }
    }

    /** Appends tag or inheritance name from the current builder data. */
    protected void addName() {
        if (Strings.isNotEmpty(builder)) {
            if (Strings.startsWith(builder, inheritanceMarker)) {
                inherits.add(builder.substring(1));
            } else {
                tags.add(builder.toString());
            }
            Strings.clearBuilder(builder);
        }
    }

    /** Parses attributes block. */
    protected void parseAttributes() {
        burnWhitespaces();
        attribute = null;
        for (; index < length; index++) {
            final char character = get();
            if (character == blockClosing) {
                if (attribute != null || Strings.isNotEmpty(builder)) {
                    throwException("Unexpected tag close.");
                }
                index++;
                return;
            } else if (Strings.isNewLine(character) && (attribute != null || Strings.isNotEmpty(builder))) {
                line--;
                throwException("Expecting line end marker: '" + lineEnd + "'.");
            } else if (Strings.isWhitespace(character) && attribute == null) {
                continue;
            } else if (character == separator && attribute == null) {
                addAttributeName();
                continue;
            } else if (character == lineEnd) {
                if (attribute == null) {
                    throwException("Found unexpected line end marker: '" + lineEnd + "'.");
                }
                addAttribute();
            } else {
                builder.append(character);
            }
        }
    }

    /** Caches currently parsed attribute name. */
    protected void addAttributeName() {
        if (Strings.isNotEmpty(builder)) {
            attribute = builder.toString();
            Strings.clearBuilder(builder);
        }
    }

    /** Clears attribute cache, adds default attribute value. */
    private void addAttribute() {
        attributes.put(attribute, builder.toString().trim());
        attribute = null;
        Strings.clearBuilder(builder);
    }

    /** Adds the stored attribute values to the style sheet. Resolves inherited styles. */
    protected void processAttributes() {
        for (final String tag : tags) {
            for (final String inherit : inherits) {
                styleSheet.addStyles(tag, styleSheet.getStyles(inherit));
            }
            styleSheet.addStyles(tag, attributes);
        }
    }

    /** @return character at current index value. */
    protected char get() {
        final char character = lss.charAt(index);
        if (Strings.isNewLine(character)) {
            line++;
        }
        return character;
    }

    /** Analyzes characters, raising the index. Stops after encountering first non-whitespace character. */
    protected void burnWhitespaces() {
        for (; index < length; index++) {
            if (Strings.isNotWhitespace(get())) {
                return;
            }
        }
    }

    /** Clears control variables. */
    public void reset() {
        lss = null;
        attribute = null;
        index = 0;
        line = 1;
        tags.clear();
        inherits.clear();
        attributes.clear();
        Strings.clearBuilder(builder);
    }
}