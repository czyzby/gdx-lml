package com.github.czyzby.lml.parser.impl;

import java.util.Queue;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.impl.dto.AbstractLmlDto;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlTagParsers;
import com.github.czyzby.lml.util.LmlSyntax;

// Here be dragons, kind of.
public class Scene2DLmlParser extends AbstractLmlParser {
	private int currentlyParsedLine;

	public Scene2DLmlParser(final Skin skin) {
		super(skin);
	}

	public Scene2DLmlParser(final Skin skin, final I18NBundle i18nBundle) {
		super(skin, i18nBundle);
	}

	public Scene2DLmlParser(final Skin skin, final I18NBundle i18nBundle, final Preferences preferences) {
		super(skin, i18nBundle, preferences);
	}

	{
		LmlTagParsers.registerDefaultMacroSyntax(this);
		LmlTagParsers.registerDefaultTagSyntax(this);
	}

	@Override
	public int getCurrentlyParsedLine() {
		return currentlyParsedLine;
	}

	@Override
	public Array<Actor> parse(final String lmlDocument) throws LmlParsingException {
		return new ParserAlgorithm(this).parse(lmlDocument);
	}

	private static class ParserAlgorithm implements LmlSyntax {
		private final Scene2DLmlParser parser;
		private final Array<Actor> parsedActors = new Array<Actor>();
		private final StringBuilder currentRawTagData = new StringBuilder();
		private final StringBuilder dataBetweenTags = new StringBuilder();
		private boolean parsingWidget;
		private boolean startOfLine = true;
		private int ignoredBufferChanges;
		private Queue<Character> originalFileBuffer;
		private Queue<Character> lastBuffer;
		private int matchingIgnoredTagsAmount;

		public ParserAlgorithm(final Scene2DLmlParser parser) {
			this.parser = parser;
		}

		public Array<Actor> parse(final String lmlDocument) {
			parser.currentlyParsedLine = 1;
			parser.buffer.clear();
			parser.appendToBuffer(lmlDocument);
			originalFileBuffer = parser.buffer.getFirst();

			// No Pattern-Matcher in GWT. Meh.
			while (!parser.buffer.isEmpty()) {
				// Current buffer might be empty. Removing until we find a non-empty one.
				removeEmptyBuffers();
				if (parser.buffer.isEmpty()) {
					// No buffers left. We're done.
					break;
				}

				final Queue<Character> currentBuffer = parser.buffer.getFirst();
				final char character = currentBuffer.poll();
				// Keeping track of current buffer:
				updateCurrentBufferInfo(currentBuffer, character);
				if (isParentRejectingCharacter(character)) {
					// Macro can reject character parsing and handle raw data on their own. Regular tags don't
					// usually do that sort of stuff.
					handleCharacterRejection(character);
					continue;
				}
				if (parser.isWhitespace(character) && startOfLine) {
					// Whitespace at the start of a line. Ignoring.
					continue;
				} else {
					startOfLine = false;
				}
				if (isCharacterStartOfAnArgument(currentBuffer, character)) {
					// Possible ${argument}. Need to investigate and maybe replace this guy.
					handleArgumentParsing(currentBuffer);
					continue;
				}
				if (parser.isNewLine(character)) {
					// New line character. If a tag was opened, it's probably broken.
					handleNewLine();
					continue;
				}
				if (parsingWidget) {
					// A tag is currently open. Appending characters to tag raw data.
					handleWidgetTagParsing(character);
				} else {
					if (character == TAG_OPENING) {
						// A new tag is opened.
						if (!checkIfComment(currentBuffer)) {
							// A new tag opened. Starting to parse.
							parsingWidget = true;
							handleDataBetweenTags(dataBetweenTags);
						} else {
							// Just parsed a comment. Let's move on.
							continue;
						}
					} else {
						// Appending raw data to text between tags. Some widgets create labels with that.
						dataBetweenTags.append(character);
					}
				}
			}
			validateHierarchy();
			return parsedActors;
		}

		private void validateHierarchy() {
			if (!parser.widgetsHierarchy.isEmpty()) {
				throw new LmlParsingException("Unclosed parent tag: "
						+ parser.widgetsHierarchy.getFirst().getTagName() + ".", parser);
			}
		}

		private void removeEmptyBuffers() {
			while (!parser.buffer.isEmpty() && parser.buffer.getFirst().isEmpty()) {
				parser.buffer.removeFirst();
			}
		}

		private void updateCurrentBufferInfo(final Queue<Character> currentBuffer, final char character) {
			if (lastBuffer != currentBuffer) {
				lastBuffer = currentBuffer;
				if (ignoredBufferChanges == 0) {
					startOfLine = true;
				} else {
					ignoredBufferChanges--;
				}
			}
			if (currentBuffer == originalFileBuffer && parser.isNewLine(character)) {
				// Line changed in the original file.
				parser.currentlyParsedLine++;
			}
		}

		private boolean isParentRejectingCharacter(final char character) {
			return !parser.widgetsHierarchy.isEmpty()
					&& !parser.widgetsHierarchy.getFirst().acceptCharacter(character);
		}

		private void handleCharacterRejection(final char character) {
			// Currently parsed tag rejected the character, but we still have to know where the tag ends.
			if (parsingWidget) {
				if (character == TAG_CLOSING) {
					parsingWidget = false;
					handleIgnoredClosingTag(parsedActors, currentRawTagData);
				} else {
					currentRawTagData.append(character);
				}
			} else if (character == TAG_OPENING) {
				parsingWidget = true;
			}
		}

		private void handleIgnoredClosingTag(final Array<Actor> parsedActors, final StringBuilder rawTagData) {
			if (!parser.isDataEmpty(rawTagData)) {
				if (isClosingTag(rawTagData)) {
					// Even though the tag told us to ignore parsed data, we just noticed that it ends.
					handleIgnoredTagClosing(parsedActors, extractTagNameFromClosedTag(rawTagData));
				} else if (isTagNameMatchingCurrentParent(rawTagData)) {
					// The same macro/tag was opened with the same keyword. Ignoring next tag closing.
					matchingIgnoredTagsAmount++;
				}
			}
			parser.clearData(rawTagData);
		}

		private boolean isTagNameMatchingCurrentParent(final StringBuilder rawTagData) {
			return extractTagNameFromRawData(rawTagData).equalsIgnoreCase(
					parser.widgetsHierarchy.getFirst().getTagName());
		}

		private String extractTagNameFromRawData(final StringBuilder rawTagData) {
			final String tagName = rawTagData.toString().split(AbstractLmlDto.WHITESPACE_REGEX)[0];
			return tagName.charAt(0) == MACRO_SIGN ? tagName.substring(1) : tagName;
		}

		private void handleIgnoredTagClosing(final Array<Actor> parsedActors, final String tag) {
			if (isTagMatchingCurrentParentTag(tag)) {
				if (matchingIgnoredTagsAmount > 0) {
					// Nested tag with the same name was closed. It's not our time, not yet.
					matchingIgnoredTagsAmount--;
					return;
				}
				// Probably a macro. Even though it told us to ignore characters, it seems to be ending, so we
				// kill it. Just like that.
				final LmlParent<?> parent = parser.widgetsHierarchy.removeFirst();
				parent.closeTag(parser);
				if (parent.getActor() != null) {
					if (parser.widgetsHierarchy.isEmpty()) {
						parsedActors.add(parent.getActor());
					}
					attachActorId(parent.getId(), parent.getActor());
				}
			}
		}

		private boolean isTagMatchingCurrentParentTag(final String tag) {
			return tag.equalsIgnoreCase(parser.widgetsHierarchy.getFirst().getTagName())
					|| tag.charAt(0) == MACRO_SIGN
					&& tag.substring(1).equalsIgnoreCase(parser.widgetsHierarchy.getFirst().getTagName());
		}

		private boolean isCharacterStartOfAnArgument(final Queue<Character> currentBuffer,
				final char character) {
			// ${...
			return character == ARGUMENT_SIGN && currentBuffer.peek() != null
					&& currentBuffer.peek().charValue() == ARGUMENT_OPENING;
		}

		private void handleArgumentParsing(final Queue<Character> currentBuffer) {
			// ${Argument} is starting - deleting the argument invocation and appending a new buffer with
			// argument's value (will be parsed like any other LML).
			final StringBuilder keyBuilder = new StringBuilder();
			currentBuffer.poll();
			for (char argumentKeyChar = currentBuffer.poll(); argumentKeyChar != ARGUMENT_CLOSING; argumentKeyChar =
					currentBuffer.poll()) {
				keyBuilder.append(argumentKeyChar);
			}
			final String key = keyBuilder.toString();
			parser.appendToBuffer(parser.getArgument(key));
			// Buffer will change twice - once to parse the argument, once to get back to the usual stuff.
			ignoredBufferChanges += 2;
		}

		private void handleNewLine() {
			if (parsingWidget) {
				throw new LmlParsingException("Unexpected line end while parsing tag: " + currentRawTagData
						+ ".", parser);
			}
			startOfLine = true;
			// Every line of texts is passed to widgets separately to allow multiline labels, text areas etc.
			handleDataBetweenTags(dataBetweenTags);
		}

		private void handleWidgetTagParsing(final char character) {
			if (character == TAG_CLOSING) {
				// Tag is being closed. Parsing tag raw data.
				parsingWidget = false;
				handleTag(parsedActors, currentRawTagData);
			} else {
				currentRawTagData.append(character);
			}
		}

		private boolean checkIfComment(final Queue<Character> currentBuffer) {
			// A tag was just opened. Checking if the next character is an comment operator: <! <?
			if (currentBuffer.peek() != null
					&& (currentBuffer.peek().charValue() == COMMENT_START_OPERATOR || currentBuffer.peek()
							.charValue() == SCHEMA_COMMENT_OPERATOR)) {
				// Deleting comment from the buffer.
				boolean wasCommentEndOperatorAppended = false;
				final boolean honorNewLines = currentBuffer == originalFileBuffer;
				while (currentBuffer.peek() != null) {
					final char character = currentBuffer.poll();
					if (honorNewLines && parser.isNewLine(character)) {
						parser.currentlyParsedLine++;
					}
					if (!wasCommentEndOperatorAppended) {
						wasCommentEndOperatorAppended = isCommentEndOperator(character);
					} else if (character == TAG_CLOSING) {
						break;
					} else {
						wasCommentEndOperatorAppended = isCommentEndOperator(character);
					}
				}
				return true;
			}
			return false;
		}

		private boolean isCommentEndOperator(final char character) {
			return character == COMMENT_END_OPERATOR || character == SCHEMA_COMMENT_OPERATOR;
		}

		private void handleTag(final Array<Actor> parsedActors, final StringBuilder rawTagData) {
			if (parser.isMacro(rawTagData)) {
				// Tag with @ appended. Parsing marco.
				handleMacro(rawTagData);
			} else if (!parser.isDataEmpty(rawTagData)) {
				// Tag was not empty.
				if (isClosingTag(rawTagData)) {
					// Tag was not parental (it ended with /). Parsing tag at once.
					handleTagClosing(parsedActors, extractTagNameFromClosedTag(rawTagData));
				} else {
					// Tag is parental. Creating tag parent.
					handleTagOpening(parsedActors, LmlTagData.parse(rawTagData));
				}
			}
			parser.clearData(rawTagData);
		}

		private String extractTagNameFromClosedTag(final StringBuilder rawTagData) {
			// Omits / at the start of a closed tag.
			return rawTagData.subSequence(1, rawTagData.length()).toString();
		}

		private void handleMacro(final StringBuilder rawTagData) {
			final LmlMacroData macroData = LmlMacroData.parse(rawTagData);
			if (!parser.macroParsers.containsKey(macroData.getMacroName())) {
				parser.throwErrorIfStrict("Unknown macro: " + macroData.getMacroName() + ".");
				return;
			}
			if (macroData.isClosed()) {
				// Macro ends at once (ends with /). Invoking macro.
				parser.macroParsers.get(macroData.getMacroName()).parseMacro(parser, macroData);
			} else {
				// Macro is parental adding a new macro parent.
				final LmlParent<?> macroParent =
						parser.macroParsers.get(macroData.getMacroName()).parseMacroParent(parser, macroData,
								parser.getCurrentParent());
				parser.widgetsHierarchy.addFirst(macroParent);
			}
		}

		/** @return true if tag starts with /. */
		private boolean isClosingTag(final StringBuilder wigdetData) {
			return wigdetData.charAt(0) == CLOSED_TAG_SIGN;
		}

		private void attachActorId(final String id, final Actor actor) {
			if (id != null) {
				// Actor contains an ID attribute. Mapping it to the ID, so it can be retrieved later.
				actor.setName(id);
				parser.actorsByIds.put(id, actor);
			}
		}

		private void handleTagOpening(final Array<Actor> parsedActors, final LmlTagData tagData) {
			if (!parser.tagParsers.containsKey(tagData.getTagName())) {
				parser.throwErrorIfStrict("Unknown tag: " + tagData.getTagName() + ".");
				return;
			}
			if (tagData.isClosed()) {
				// Tag ends with /. Parsing an actor at once.
				handleChildTagOpeningAndClosing(parsedActors, tagData);
			} else {
				// Parental tag opened. Updating parent info.
				handleParentTagOpening(tagData);
			}
		}

		private void handleChildTagOpeningAndClosing(final Array<Actor> parsedActors, final LmlTagData tagData) {
			final Actor actor = parser.tagParsers.get(tagData.getTagName()).parseChild(tagData, parser);
			if (parser.widgetsHierarchy.isEmpty()) {
				// Tag was a LML root, having no parent. Adding an actor to the parsed array.
				parsedActors.add(actor);
			} else {
				// Parents like to know about their children.
				parser.widgetsHierarchy.getFirst().handleChild(actor, tagData, parser);
			}
			attachActorId(tagData.getId(), actor);
		}

		private void handleParentTagOpening(final LmlTagData tagData) {
			final LmlParent<?> oldParent = parser.getCurrentParent();
			final LmlParent<?> currentParent =
					parser.tagParsers.get(tagData.getTagName()).parseParent(tagData, parser, oldParent);
			if (oldParent != null) {
				// The old parent still needs to know about its child, even if it's a parent itself.
				oldParent.handleChild(currentParent.getActor(), tagData, parser);
			}
			parser.widgetsHierarchy.addFirst(currentParent);
		}

		private void handleTagClosing(final Array<Actor> parsedActors, final String tag) {
			if (parser.widgetsHierarchy.isEmpty()) {
				parser.throwErrorIfStrict("Closed unopened tag.");
				return;
			}
			if (!tag.equalsIgnoreCase(parser.widgetsHierarchy.getFirst().getTagName())) {
				throw new LmlParsingException("Closed unexpected tag: " + tag + ", expected: "
						+ parser.widgetsHierarchy.getFirst().getTagName() + ".", parser);
			}
			final LmlParent<?> parent = parser.widgetsHierarchy.removeFirst();
			parent.closeTag(parser);
			if (parent.getActor() != null) {
				// Some parents are not actors - for example, macros can be parental, but create no actors by
				// themselves.
				if (parser.widgetsHierarchy.isEmpty()) {
					parsedActors.add(parent.getActor());
				}
				attachActorId(parent.getId(), parent.getActor());
			}
		}

		private void handleDataBetweenTags(final StringBuilder dataBetweenTags) {
			// Some non-tag text can be written between tags. It's OK, some parents can read these stuff.
			if (!parser.isDataEmpty(dataBetweenTags) && !parser.widgetsHierarchy.isEmpty()) {
				parser.widgetsHierarchy.getFirst().handleDataBetweenTags(dataBetweenTags.toString(), parser);
			}
			parser.clearData(dataBetweenTags);
		}
	}
}
