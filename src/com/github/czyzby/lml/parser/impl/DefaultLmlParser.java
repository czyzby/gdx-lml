package com.github.czyzby.lml.parser.impl;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.common.Nullables;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.parser.LmlData;
import com.github.czyzby.lml.parser.LmlSyntax;
import com.github.czyzby.lml.parser.LmlTemplateReader;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.parser.tag.LmlTagProvider;
import com.github.czyzby.lml.util.LmlParsingException;
import com.github.czyzby.lml.util.LmlUtilities;

/** Default implementation of LML parser.
 *
 * @author MJ */
public class DefaultLmlParser extends AbstractLmlParser {
    /** Helper variable. Keeps reference to currently nearest nested parent. Never a macro tag. */
    private LmlTag currentParentTag;
    /** Parsing result, cleared after each parsing. Kept as field to support {@link #addActor(Actor)}. */
    private Array<Actor> actors;

    /** Creates a new strict parser with default syntax and reader.
     *
     * @param data contains skin, actions, i18n bundles and other data needed to parse LML templates. */
    public DefaultLmlParser(final LmlData data) {
        super(data, DefaultLmlSyntax.INSTANCE, new DefaultLmlTemplateReader(), true);
    }

    /** Creates a new strict parser with custom syntax and default reader.
     *
     * @param data contains skin, actions, i18n bundles and other data needed to parse LML templates.
     * @param syntax determines syntax of LML templates. */
    public DefaultLmlParser(final LmlData data, final LmlSyntax syntax) {
        super(data, syntax, new DefaultLmlTemplateReader(), true);
    }

    /** Creates a new strict parser with custom syntax and reader.
     *
     * @param data contains skin, actions, i18n bundles and other data needed to parse LML templates.
     * @param syntax determines syntax of LML templates.
     * @param templateReader reads and buffers templates and their files. */
    public DefaultLmlParser(final LmlData data, final LmlSyntax syntax, final LmlTemplateReader templateReader) {
        super(data, syntax, templateReader, true);
    }

    /** Creates a new parser with custom syntax, reader and strict setting.
     *
     * @param data contains skin, actions, i18n bundles and other data needed to parse LML templates.
     * @param syntax determines syntax of LML templates.
     * @param templateReader reads and buffers templates and their files.
     * @param strict if false, will ignore some unexpected errors, like unknown attributes, invalid referenced method
     *            names etc. Set to true for more HTML-like feel or quick prototyping. */
    public DefaultLmlParser(final LmlData data, final LmlSyntax syntax, final LmlTemplateReader templateReader,
            final boolean strict) {
        super(data, syntax, templateReader, strict);
    }

    @Override
    public void addActor(final Actor actor) {
        if (actors == null) {
            throw new IllegalStateException("Actors can be added to result collection only during parsing.");
        }
        actors.add(actor);
        mapActorById(actor);
    }

    @Override
    protected Array<Actor> parseTemplate() {
        try {
            return parse();
        } catch (final LmlParsingException exception) {
            // Expected exception.
            throw exception;
        } catch (final Exception exception) {
            // Unexpected exception. Rethrowing as our own to point the error line.
            throwError("Unable to parse passed template due to an unexpected exception.", exception);
            return null;
        } finally {
            currentParentTag = null; // Making sure there are no tags from invalid templates.
            templateReader.clear(); // If an exception was thrown, we no longer want to parse the template content;
                                    // if no error occurred, template reader has no data and this is a safe no-op.
            actors = null; // Nulling out helper actors array reference. This is the parsing result we want to return.
        }
    }

    /** Does the actual parsing
     *
     * @return actor parsed from LML template currently stored in the template reader. */
    protected Array<Actor> parse() {
        actors = GdxArrays.newArray(Actor.class);
        final StringBuilder builder = new StringBuilder();
        while (templateReader.hasNextCharacter()) {
            final char character = templateReader.nextCharacter();
            if (character == syntax.getArgumentOpening()) {
                // Found an argument opening. This needs to be replaced.
                processArgument();
            } else if (character == syntax.getTagOpening()) {
                // Tag was just opened. This might be a comment, though.
                if (isNextCharacterCommentOpening()) {
                    processComment();
                    continue;
                }
                // This is not a comment, since we're there. Parsing a new tag.
                if (currentParentTag != null) {
                    currentParentTag.handleDataBetweenTags(builder.toString());
                }
                Strings.clearBuilder(builder);
                processTag(builder);
            } else { // Just your regular letter outside of a tag:
                builder.append(character);
            }
        }
        if (currentParentTag != null) {
            throwError('"' + currentParentTag.getTagName() + "\" tag was never closed.");
        }
        return actors;
    }

    /** Found an argument opening sign. Have to find argument's name and replace it in the template. */
    private void processArgument() {
        final StringBuilder argumentNameBuilder = new StringBuilder();
        while (templateReader.hasNextCharacter()) {
            final char argumentCharacter = templateReader.nextCharacter();
            if (argumentCharacter == syntax.getArgumentClosing()) {
                final String argumentName = argumentNameBuilder.toString().trim(); // Getting actual argument name.
                templateReader.append(Nullables.toString(data.getArgument(argumentName)), argumentName + " argument");
                return;
            }
            argumentNameBuilder.append(argumentCharacter);
        }
    }

    /** Found an open tag starting with comment sign. Burning through characters up to the comment's end. */
    private void processComment() {
        templateReader.nextCharacter(); // Burning comment opening char.
        if (nestedComments) {
            processNestedComment();
            return;
        }
        while (templateReader.hasNextCharacter()) {
            final char commentCharacter = templateReader.nextCharacter();
            if (isCommentClosingMarker(commentCharacter) && templateReader.hasNextCharacter()
                    && templateReader.peekCharacter() == syntax.getTagClosing()) {
                // Character was a comment closing sign and the next tag closed the comment.
                templateReader.nextCharacter(); // Polling tag closing.
                break;
            }
        }
    }

    /** Found an open tag starting with comment sign and nested comments are on. Burning through characters up to the
     * comment's end, honoring nested, commented-out comments. */
    private void processNestedComment() {
        int nestedCommentsAmount = 1; // Starting with just the initial comment.
        while (templateReader.hasNextCharacter()) {
            final char commentCharacter = templateReader.nextCharacter();
            if (isCommentClosingMarker(commentCharacter) && templateReader.hasNextCharacter()
                    && templateReader.peekCharacter() == syntax.getTagClosing()) {
                // Character was a comment closing sign and the next tag closed the comment.
                templateReader.nextCharacter(); // Polling tag closing.
                if (--nestedCommentsAmount == 0) {
                    // All comments ended. Returning.
                    break;
                }
            }
            if (commentCharacter == syntax.getTagOpening() && templateReader.hasNextCharacter()
                    && isCommentOpeningMarker(templateReader.peekCharacter())) {
                // Another comment was opened inside the currently parsed one.
                templateReader.nextCharacter(); // Polling comment opening marker.
                nestedCommentsAmount++;
            }
        }
    }

    /** Found an open tag that is not a comment. Collecting whole tag data.
     *
     * @param builder will be used to collect data. */
    private void processTag(final StringBuilder builder) {
        while (templateReader.hasNextCharacter()) {
            final char tagCharacter = templateReader.nextCharacter();
            if (tagCharacter == syntax.getArgumentOpening()) {
                // LML parser argument inside a tag. We want to convert these.
                processArgument();
            } else if (tagCharacter == syntax.getTagOpening() && isNextCharacterCommentOpening()) {
                // Comment inside a tag. Removing its content.
                processComment();
            } else if (tagCharacter == syntax.getTagClosing()) {
                // We're being closed.
                final String rawTagData = builder.toString();
                Strings.clearBuilder(builder);
                processTagEntity(rawTagData, builder);
                return;
            } else {
                builder.append(tagCharacter);
            }
        }
        throwError("Unclosed tag: " + builder.toString());
    }

    /** @param rawTagData collected, unparsed LML tag data. Might be a macro or a widget.
     * @param builder might be needed to process additional data. */
    private void processTagEntity(String rawTagData, final StringBuilder builder) {
        rawTagData = rawTagData.trim();
        final int tagNameEndIndex = getTagNameEndIndex(rawTagData);
        if (Strings.startsWith(rawTagData, syntax.getClosedTagMarker())) {
            // This is a closing tag (</tag>). Trying to close current parent. Starting with 1 char to strip marker.
            final String closedTagName = rawTagData.substring(1, tagNameEndIndex).trim();
            processClosedTag(closedTagName);
        } else if (Strings.startsWith(rawTagData, syntax.getMacroMarker())) {
            // Uh-oh, this is a macro. Macros handle their content themselves, so we need to process them differently.
            final String macroName = LmlUtilities // Stripping last character if the tag is immediately closed: <@tag/>.
                    .stripEnding(rawTagData.substring(1, tagNameEndIndex), syntax.getClosedTagMarker()).trim();
            processMacro(macroName, rawTagData, builder);
        } else {
            // Regular tag.
            final String tagName = LmlUtilities // Stripping last character if the tag is immediately closed: <tag/>.
                    .stripEnding(rawTagData.substring(0, tagNameEndIndex), syntax.getClosedTagMarker()).trim();
            processRegularTag(tagName, rawTagData);
        }
    }

    /** @param rawTagData unparsed LML tag data.
     * @return index that marks the end of tag's name. */
    private static int getTagNameEndIndex(final String rawTagData) {
        final int spaceIndex = rawTagData.indexOf(' ');
        if (Strings.isCharacterPresent(spaceIndex)) {
            // At least one space is present in the string, so we assume that the data before first space is tag's name:
            return spaceIndex;
        }
        // There are no spaces, so we assume that the whole tag data is its name:
        return rawTagData.length();
    }

    /** @param closedTagName is the currently closed tag. */
    private void processClosedTag(final String closedTagName) {
        if (currentParentTag == null) {
            throwErrorIfStrict("There were no open tags, and yet: \"" + closedTagName + "\" is a closed parental tag.");
            return;
        } else if (!currentParentTag.getTagName().equals(closedTagName)) {
            if (strict || !strict && !currentParentTag.getTagName().equalsIgnoreCase(closedTagName)) {
                throwError("Tag: \"" + closedTagName + "\" was closed, but: \"" + currentParentTag.getTagName()
                        + "\" was expected.");
            }
        }
        currentParentTag.closeTag();
        final LmlTag grandParent = currentParentTag.getParent();
        if (grandParent == null) { // Tag was a root.
            if (currentParentTag.getActor() != null) {
                actors.add(currentParentTag.getActor());
            }
        } else { // Tag had a parent.
            grandParent.handleChild(currentParentTag);
        }
        mapActorById(currentParentTag.getActor());
        currentParentTag = grandParent;
    }

    /** @param macroName name of the macro tag to be parsed.
     * @param rawTagData raw data of the macro tag.
     * @param builder used to append data between macro tags. Cleared afterwards. */
    private void processMacro(final String macroName, final String rawTagData, final StringBuilder builder) {
        final LmlTagProvider tagProvider = syntax.getMacroTagProvider(macroName);
        if (tagProvider == null) {
            throwError("No macro tag provider found for name: " + macroName);
        }
        final LmlTag macroTag = tagProvider.create(this, currentParentTag, rawTagData);
        if (macroTag.isChild()) { // Immediately closing the tag, since it's a child.
            macroTag.closeTag();
            return;
        }
        int sameNameNestedMacrosAmount = 1; // We start with just our own macro.
        final StringBuilder helperBuilder = new StringBuilder();
        while (templateReader.hasNextCharacter()) {
            final char macroCharacter = templateReader.nextCharacter();
            if (macroCharacter == syntax.getTagOpening() && templateReader.hasNextCharacter()) {
                // Another tag was opened. This might be macro's end or a nested macro - we need to handle both.
                final char nextMacroCharacter = templateReader.peekCharacter();
                if (nextMacroCharacter == syntax.getClosedTagMarker()) {
                    // A tag is being closed. We need to investigate if this is our tag.
                    final int sameMacroTag = isSameMacroTagName(macroTag, helperBuilder);
                    if (sameMacroTag > -1 && --sameNameNestedMacrosAmount == 0) {
                        // We're done. All nested macros with the same name are parsed.
                        burnCharacters(sameMacroTag);
                        break;
                    }
                } else if (nextMacroCharacter == syntax.getMacroMarker()) {
                    // A macro is being open. We need to investigate if it has the same tag name.
                    if (isSameMacroTagName(macroTag, helperBuilder) > -1) {
                        sameNameNestedMacrosAmount++;
                    }
                }
            }
            // No macro tag is currently being open or the opening is irrelevant. Appending character.
            builder.append(macroCharacter);
        }
        if (sameNameNestedMacrosAmount > 0) {
            throwError("Macro tag not closed: " + macroTag.getTagName());
        }
        final String content = builder.toString();
        if (Strings.isNotEmpty(content)) {
            macroTag.handleDataBetweenTags(content);
        }
        Strings.clearBuilder(builder);
        macroTag.closeTag();
    }

    /** @param charactersAmount amount of characters to be removed from the reader. */
    private void burnCharacters(final int charactersAmount) {
        for (int index = 0; index < charactersAmount; index++) {
            templateReader.nextCharacter();
        }
    }

    /** @param macroTag is currently parsed and a same name tag is currently being closed or open right now.
     * @param tagNameBuilder helper builder to construct macro tag name.
     * @return true (>-1) if a macro with the same name is currently being closed or opened. The returned value is also
     *         the amount of characters that should be burned if the macro closing tag actually belongs to the currently
     *         parsed macro and not one of its nested children. */
    private int isSameMacroTagName(final LmlTag macroTag, final StringBuilder tagNameBuilder) {
        int additionalIndexesToPeek = 0;
        if (templateReader.hasNextCharacter() && templateReader.peekCharacter() == syntax.getClosedTagMarker()) {
            // The tag is currently closed. Looking for macro sign on the next index.
            additionalIndexesToPeek++;
        }
        if (!templateReader.hasNextCharacter(additionalIndexesToPeek)
                || templateReader.peekCharacter(additionalIndexesToPeek++) != syntax.getMacroMarker()) {
            // This is a regular tag, so names cannot match.
            return -1;
        }
        Strings.clearBuilder(tagNameBuilder);
        while (templateReader.hasNextCharacter(additionalIndexesToPeek)) {
            final char character = templateReader.peekCharacter(additionalIndexesToPeek++);
            if (character == syntax.getTagClosing() || character == syntax.getClosedTagMarker()
                    || Strings.isWhitespace(character)) {
                // Whitespaces separate tag name from attributes and cannot be escaped in tag names. If the tag is
                // currently being closed or its a whitespace, we've got the whole tag name. But this can happen:
                // <@macro><@macro attribute /></@macro>
                // If we return now, we tell the parser that a nested macro is inside this one, even though the macro
                // itself was closed as soon as it was opened. We need to check if macro is not a child.
                if ((Strings.isWhitespace(character) || character == syntax.getClosedTagMarker())
                        && isCurrentMacroTagChild(additionalIndexesToPeek - 1)) {
                    return -1;
                }
                final String closedTagName = tagNameBuilder.toString().trim();
                if (macroTag.getTagName().equals(closedTagName)
                        || !strict && macroTag.getTagName().equalsIgnoreCase(closedTagName)) {
                    return additionalIndexesToPeek;
                }
                // Names don't match.
                return -1;
            }
            // Tag is not closed yet. Appending character.
            tagNameBuilder.append(character);
        }
        // Tag never closed. Exception will probably be thrown in the future, but we don't care for now.
        return -1;
    }

    /** @param additionalIndexesToPeek current character pointer to check.
     * @return true if currently parsed macro tag is a child. */
    private boolean isCurrentMacroTagChild(int additionalIndexesToPeek) {
        while (templateReader.hasNextCharacter(additionalIndexesToPeek)) {
            final char character = templateReader.peekCharacter(additionalIndexesToPeek++);
            if (character == syntax.getClosedTagMarker() && isLastCharacterInMacroTag(additionalIndexesToPeek)) {
                // '/' found before tag was closed. This is might be a child.
                return true;
            } else if (character == syntax.getTagClosing()) {
                // Tag was closed without '/' marker. This is a parent.
                return false;
            }
        }
        return false;
    }

    /** @param additionalIndexesToPeek current character pointer to check.
     * @return if the additionalIndexesToPeek-1 char is the last one in the tag - it contains only whitespace chars and
     *         the tag closing marker right after it. */
    private boolean isLastCharacterInMacroTag(int additionalIndexesToPeek) {
        while (templateReader.hasNextCharacter(additionalIndexesToPeek)) {
            final char character = templateReader.peekCharacter(additionalIndexesToPeek++);
            if (character == syntax.getTagClosing()) {
                // Found tag closing after the character.
                return true;
            } else if (!Strings.isWhitespace(character)) {
                // Found different char before the tag was closed.
                return false;
            }
        }
        return false;
    }

    /** @param tagName name of the tag to be parsed.
     * @param rawTagData raw data of a regular widget tag. */
    private void processRegularTag(final String tagName, final String rawTagData) {
        final LmlTagProvider tagProvider = syntax.getTagProvider(tagName);
        if (tagProvider == null) {
            throwError("No tag parser found for name: " + tagName);
        }
        final LmlTag tag = tagProvider.create(this, currentParentTag, rawTagData);
        if (tag.isParent()) {
            currentParentTag = tag;
        } else {
            // The tag is a child, so we're closing it immediately.
            tag.closeTag();
            if (currentParentTag != null) {
                // Tag is child - adding to current parent:
                currentParentTag.handleChild(tag);
            } else {
                // Tag is a root - adding to the result:
                if (tag.getActor() != null) {
                    actors.add(tag.getActor());
                }
            }
            mapActorById(tag.getActor());
        }
    }
}
