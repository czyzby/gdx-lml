package com.konfigurats.lml.parser.impl.macro.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.utils.Array;
import com.konfigurats.lml.error.LmlParsingException;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.ActorConsumer;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.util.LmlSyntax;
import com.konfigurats.lml.util.common.Nullables;
import com.konfigurats.lml.util.gdx.collection.GdxArrays;

public abstract class AbstractLmlMacroParent implements LmlParent<Actor>, LmlSyntax {
	private static final int META_CHARS_AMOUNT = 4; // </@ ... >

	private final String tagName;
	private final LmlParent<?> parent;
	protected final Array<String> arguments;
	/** Use with caution - contains macro ending tag. */
	protected final StringBuilder rawTextBetweenTags = new StringBuilder();

	public AbstractLmlMacroParent(final String tagName, final LmlParent<?> parent,
			final Array<String> arguments) {
		this.tagName = tagName;
		this.parent = parent;
		this.arguments = arguments;
	}

	@Override
	public String getTagName() {
		return tagName;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public boolean acceptCharacter(final char character) {
		appendCharacter(character);
		// Not accepting to parse the character, macro does it's internal parsing.
		return false;
	}

	protected void appendCharacter(final char character) {
		rawTextBetweenTags.append(character);
	}

	@Override
	public LmlParent<?> getParent() {
		return parent;
	}

	/** @return return parent's actor. Can be null. Utility method - parent can be null and this method does the
	 *         necessary null check. */
	public Actor getParentActor() {
		return parent == null ? null : parent.getActor();
	}

	@Override
	public Actor getActor() {
		return null;
	}

	@Override
	public void handleChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
	}

	@Override
	public void handleDataBetweenTags(final String data, final LmlParser parser) {
	}

	protected String getAppenedTextWithStrippedEndTag() {
		final String appendedText = rawTextBetweenTags.toString();
		if (appendedText.length() == 0) {
			return appendedText;
		}
		return appendedText.substring(0, appendedText.length() - META_CHARS_AMOUNT - getTagName().length());
	}

	public String toArgument(final String argumentKey) {
		return ARGUMENT_SIGN + "" + ARGUMENT_OPENING + argumentKey + ARGUMENT_CLOSING;
	}

	public String toArgument(final int argumentKey) {
		return toArgument(String.valueOf(argumentKey));
	}

	/** Parses passed argument into an array. Honors ranges, "native" arrays (; separated) and method
	 * invocations (&). Looks for methods consuming Object when using reflection. Passes null to method
	 * invocations.
	 *
	 * @return parser array of strings. */
	public static Array<String> toArgumentArray(final String argumentValue, final LmlParser parser) {
		return toArgumentArray(argumentValue, parser, null);
	}

	/** Parses passed argument into an array. Honors ranges, "native" arrays (; separated) and method
	 * invocations (&). Passes parent to method invocations.
	 *
	 * @return parser array of strings. */
	public static Array<String> toArgumentArray(final String argumentValue, final LmlParser parser,
			final Actor parent) {
		final Array<String> arguments = GdxArrays.newArray();
		final String[] argumentElements = argumentValue.split(ARRAY_SEPARATOR_REGEX);
		for (final String element : argumentElements) {
			if (containsAll(element, RANGE_ARGUMENT_OPENING, RANGE_ARGUMENT_SEPARATOR, RANGE_ARGUMENT_CLOSING)) {
				arguments.addAll(extractRange(element));
			} else if (element.charAt(0) == ACTION_OPERATOR) {
				arguments.addAll(extractMethodInvocation(parser, parent, element.substring(1)));
			} else if (element.charAt(0) == PREFERENCE_SIGN) {
				arguments.add(parser.getPreference(element.substring(1)));
			} else {
				arguments.add(element);
			}
		}
		return arguments;
	}

	private static Array<? extends String> extractMethodInvocation(final LmlParser parser,
			final Actor parent, final String methodInvocation) {
		final ActorConsumer<Object, Object> action =
				parser.findAction(methodInvocation, parent == null ? Object.class : parent.getClass());
		final Object result = action.consume(parent);
		if (result instanceof Iterable<?>) {
			final Array<String> arguments = GdxArrays.newArray();
			for (final Object argument : (Iterable<?>) result) {
				arguments.add(Nullables.toString(argument, NULL_ARGUMENT));
			}
			return arguments;
		} else {
			return GdxArrays.newArray(Nullables.toString(result, NULL_ARGUMENT));
		}
	}

	public static Array<? extends String> extractRange(final String element) {
		final int rangeOpening = element.indexOf(RANGE_ARGUMENT_OPENING);
		final String textBase = element.substring(0, rangeOpening);
		final String[] range =
				element.substring(rangeOpening + 1, element.length() - 1).split(
						RANGE_ARGUMENT_SEPARATOR_REGEX);
		final int rangeStart = Integer.parseInt(range[0]);
		final int rangeEnd = Integer.parseInt(range[1]);
		final Array<String> rangeArguments = GdxArrays.newArray();
		if (rangeStart <= rangeEnd) {
			for (int index = rangeStart; index <= rangeEnd; index++) {
				rangeArguments.add(textBase + index);
			}
		} else {
			for (int index = rangeStart; index >= rangeEnd; index--) {
				rangeArguments.add(textBase + index);
			}
		}
		return rangeArguments;
	}

	public static boolean containsAll(final String string, final Object... sequences) {
		for (final Object sequence : sequences) {
			if (!string.contains(sequence.toString())) {
				return false;
			}
		}
		return true;
	}

	protected void throwErrorIfStrict(final LmlParser parser, final String message) {
		if (parser.isStrict()) {
			throw new LmlParsingException(message, parser);
		}
	}

	/** @return regex that evaluates equalIgnoreKeys on tag name. */
	protected String getTagNameIgnoringCaseRegex() {
		final StringBuilder regex = new StringBuilder();
		for (final char character : getTagName().toCharArray()) { // Uppercase.
			regex.append("[" + Character.toLowerCase(character) + Character.toUpperCase(character) + "]");
		}
		return regex.toString();
	}

	@Override
	public Node getNode() {
		return null;
	}
}
