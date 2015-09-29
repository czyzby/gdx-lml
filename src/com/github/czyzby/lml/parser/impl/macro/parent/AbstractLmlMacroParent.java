package com.github.czyzby.lml.parser.impl.macro.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.common.Nullables;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.ActorConsumer;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.util.LmlSyntax;

public abstract class AbstractLmlMacroParent implements LmlParent<Actor>, LmlSyntax {
    private static final int META_CHARS_AMOUNT = 4; // </@ ... >

    private final String tagName;
    private final LmlParent<?> parent;
    private final int lineNumber;
    protected final Array<String> arguments;
    /** Use with caution - contains macro ending tag. */
    protected final StringBuilder rawTextBetweenTags = new StringBuilder();

    public AbstractLmlMacroParent(final LmlMacroData lmlMacroData, final LmlParent<?> parent, final LmlParser parser) {
        this.parent = parent;
        tagName = lmlMacroData.getMacroName();
        arguments = lmlMacroData.getArguments();
        lineNumber = parser.getCurrentlyParsedLine();
    }

    @Override
    public String getTagName() {
        return tagName;
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
        return parent == null ? null
                : parent instanceof AbstractLmlMacroParent ? ((AbstractLmlMacroParent) parent).getParentActor()
                        : parent.getActor();
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public Actor getActor() {
        return null;
    }

    @Override
    public void handleChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
        // Macros generally do not have actor children, they parse text between tags "manually".
        if (parent != null) {
            parent.handleChild(child, childTagData, parser);
        }
    }

    @Override
    public void handleDataBetweenTags(final String data, final LmlParser parser) {
        // Macros parse text between tags "manually".
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

    /** Parses passed argument into an array. Honors ranges, "native" arrays (; separated) and method invocations
     * (&amp;). Looks for methods consuming Object when using reflection. Passes null to method invocations.
     *
     * @return parser array of strings. */
    public static Array<String> toArgumentArray(final String argumentValue, final LmlParser parser) {
        return toArgumentArray(argumentValue, parser, null);
    }

    /** Parses passed argument into an array. Honors ranges, "native" arrays (; separated) and method invocations
     * (&amp;). Passes parent to method invocations.
     *
     * @return parser array of strings. */
    public static Array<String> toArgumentArray(final String argumentValue, final LmlParser parser,
            final Actor parent) {
        final Array<String> arguments = GdxArrays.newArray();
        final String[] argumentElements = argumentValue.split(ARRAY_SEPARATOR_REGEX);
        for (final String element : argumentElements) {
            if (containsAll(element, RANGE_ARGUMENT_OPENING, RANGE_ARGUMENT_SEPARATOR, RANGE_ARGUMENT_CLOSING)) {
                extractRange(element, arguments);
            } else if (element.charAt(0) == ACTION_OPERATOR) {
                extractMethodInvocationResult(parser, parent, element.substring(1), arguments);
            } else if (element.charAt(0) == PREFERENCE_SIGN) {
                arguments.add(parser.getPreference(element));
            } else {
                arguments.add(element);
            }
        }
        return arguments;
    }

    private static void extractMethodInvocationResult(final LmlParser parser, final Actor parent,
            final String methodInvocation, final Array<String> arguments) {
        final ActorConsumer<Object, Object> action = parser.findAction(methodInvocation,
                parent == null ? Object.class : parent.getClass());
        final Object result = action.consume(parent);
        if (result instanceof Object[]) {
            for (final Object argument : (Object[]) result) {
                arguments.add(Nullables.toString(argument, NULL_ARGUMENT));
            }
        } else if (result instanceof Iterable<?>) {
            for (final Object argument : (Iterable<?>) result) {
                arguments.add(Nullables.toString(argument, NULL_ARGUMENT));
            }
        } else {
            arguments.add(Nullables.toString(result, NULL_ARGUMENT));
        }
    }

    /** @param lmlRangeToParse unparsed element with LML range syntax.
     * @param arguments will store extracted range elements. */
    public static void extractRange(final String lmlRangeToParse, final Array<String> arguments) {
        final int rangeOpening = lmlRangeToParse.indexOf(RANGE_ARGUMENT_OPENING);
        final String textBase = lmlRangeToParse.substring(0, rangeOpening);
        final String[] range = lmlRangeToParse.substring(rangeOpening + 1, lmlRangeToParse.length() - 1)
                .split(RANGE_ARGUMENT_SEPARATOR_REGEX);
        final int rangeStart = Integer.parseInt(range[0]);
        final int rangeEnd = Integer.parseInt(range[1]);
        if (rangeStart <= rangeEnd) {
            for (int index = rangeStart; index <= rangeEnd; index++) {
                arguments.add(textBase + index);
            }
        } else {
            for (int index = rangeStart; index >= rangeEnd; index--) {
                arguments.add(textBase + index);
            }
        }
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
            regex.append('[').append(Character.toLowerCase(character)).append(Character.toUpperCase(character))
                    .append(']');
        }
        return regex.toString();
    }

    @Override
    public Node getNode() {
        // Macros are never tree nodes.
        return null;
    }
}