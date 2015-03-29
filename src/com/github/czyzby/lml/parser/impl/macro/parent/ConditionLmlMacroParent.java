package com.github.czyzby.lml.parser.impl.macro.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.util.gdx.collection.GdxMaps;

public class ConditionLmlMacroParent extends AbstractLmlMacroParent {
	private static final String ELSE_OPENING = "<@";
	private static final String ELSE_CLOSING = ":[eE][lL][sS][eE]/>";

	public ConditionLmlMacroParent(final String tagName, final LmlParent<?> parent,
			final Array<String> arguments) {
		super(tagName, parent, arguments);
	}

	@Override
	public void closeTag(final LmlParser parser) {
		final String textInTag = getAppenedTextWithStrippedEndTag();
		if (textInTag.length() == 0) {
			throwErrorIfStrict(parser, "Conditional macro has to have a body.");
			return;
		}
		if (arguments.size == 0) {
			throwErrorIfStrict(parser, "Conditional macro has to have an argument to check.");
			return;
		}
		final String[] textToAppend =
				textInTag.split(ELSE_OPENING + getTagNameIgnoringCaseRegex() + ELSE_CLOSING);

		if (checkCondition(parser)) {
			parser.appendToBuffer(textToAppend[0]);
		} else if (textToAppend.length > 1) {
			parser.appendToBuffer(textToAppend[1]);
		}
	}

	private boolean checkCondition(final LmlParser parser) {
		if (arguments.size == 1) {
			return doSingleArgumentCheck(arguments.get(0), parser);
		} else if (arguments.size == 2) {
			throwErrorIfStrict(parser, "Invalid arguments amount in condition.");
		} else {
			final String unparsedOperator = arguments.get(Operator.FIRST_OPERATOR_INDEX);
			final Operator operator = Operator.getOperator(unparsedOperator);
			if (operator == null) {
				if (unparsedOperator.charAt(0) == ACTION_OPERATOR) {
					return (Boolean) parser.findAction(unparsedOperator.substring(1), Array.class).consume(
							arguments);
				} else {
					return (Boolean) parser.findAction(unparsedOperator, Array.class).consume(arguments);
				}
			} else {
				return operator.checkCondition(arguments, parser, getParentActor());
			}
		}
		return false;
	}

	private boolean doSingleArgumentCheck(final String conditionArgument, final LmlParser parser) {
		if (conditionArgument != null && conditionArgument.length() > 0) {
			if (conditionArgument.charAt(0) == ACTION_OPERATOR) {
				// Method invocation, consuming parent:
				return (Boolean) parser.findAction(conditionArgument.substring(1), Array.class).consume(
						getParentActor());
			}
			// Null/false check:
			return !conditionArgument.equalsIgnoreCase(NULL_ARGUMENT)
					&& !conditionArgument.equalsIgnoreCase(BOOLEAN_FALSE_ATTRIBUTE_VALUE);
		}
		return false;
	}

	private static enum Operator {
		EQUALS("=", "==", "===", "eq", "equals", "equal") {
			@Override
			public boolean checkCondition(final Array<String> conditionArguments, final LmlParser parser,
					final Actor parent) throws LmlParsingException {
				final String firstArgument = parseFirstArgument(conditionArguments, parser, parent);
				final String secondArgument = parseSecondArgument(conditionArguments, parser, parent);
				if (isInt(firstArgument) && isInt(secondArgument)) {
					return Integer.parseInt(firstArgument) == Integer.parseInt(secondArgument);
				}
				if (isFloat(firstArgument) && isFloat(secondArgument)) {
					return Float.compare(Float.parseFloat(firstArgument), Float.parseFloat(secondArgument)) == 0;
				}
				return firstArgument.equals(secondArgument);
			}
		},
		NOT_EQUALS("!=", "!==", "ne", "notEquals", "notEqual") {
			@Override
			public boolean checkCondition(final Array<String> conditionArguments, final LmlParser parser,
					final Actor parent) throws LmlParsingException {
				return !Operator.EQUALS.checkCondition(conditionArguments, parser, parent);
			}
		},
		LOWER_THAN("<", "lt", "lowerThan", "lesserThan") {
			@Override
			public boolean checkCondition(final Array<String> conditionArguments, final LmlParser parser,
					final Actor parent) throws LmlParsingException {
				final float firstArgument =
						Float.parseFloat(parseFirstArgument(conditionArguments, parser, parent));
				final float secondArgument =
						Float.parseFloat(parseSecondArgument(conditionArguments, parser, parent));
				return firstArgument < secondArgument;
			}
		},
		LOWER_OR_EQUALS("<=", "le", "lowerOrEquals", "lesserOrEquals") {
			@Override
			public boolean checkCondition(final Array<String> conditionArguments, final LmlParser parser,
					final Actor parent) throws LmlParsingException {
				final float firstArgument =
						Float.parseFloat(parseFirstArgument(conditionArguments, parser, parent));
				final float secondArgument =
						Float.parseFloat(parseSecondArgument(conditionArguments, parser, parent));
				return firstArgument <= secondArgument;
			}
		},
		GREATER_THAN("gt", "greaterThan") {
			@Override
			public boolean checkCondition(final Array<String> conditionArguments, final LmlParser parser,
					final Actor parent) throws LmlParsingException {
				final float firstArgument =
						Float.parseFloat(parseFirstArgument(conditionArguments, parser, parent));
				final float secondArgument =
						Float.parseFloat(parseSecondArgument(conditionArguments, parser, parent));
				return firstArgument > secondArgument;
			}
		},
		GREATER_OR_EQUALS("ge", "greaterOrEquals") {
			@Override
			public boolean checkCondition(final Array<String> conditionArguments, final LmlParser parser,
					final Actor parent) throws LmlParsingException {
				final float firstArgument =
						Float.parseFloat(parseFirstArgument(conditionArguments, parser, parent));
				final float secondArgument =
						Float.parseFloat(parseSecondArgument(conditionArguments, parser, parent));
				return firstArgument >= secondArgument;
			}
		},
		IN(":", "in") {
			@Override
			public boolean checkCondition(final Array<String> conditionArguments, final LmlParser parser,
					final Actor parent) {
				final String firstArgument = parseFirstArgument(conditionArguments, parser, parent);
				final Array<String> secondArgument =
						AbstractLmlMacroParent.toArgumentArray(
								parseSecondArgument(conditionArguments, parser, parent), parser, parent);
				return secondArgument.contains(firstArgument, false);
			}
		},
		MODULO("%", "mod", "md", "modulo") {
			@Override
			public boolean checkCondition(final Array<String> conditionArguments, final LmlParser parser,
					final Actor parent) {
				final int firstArgument =
						Integer.parseInt(parseFirstArgument(conditionArguments, parser, parent));
				final int secondArgument =
						Integer.parseInt(parseSecondArgument(conditionArguments, parser, parent));
				final int modulo = firstArgument % secondArgument;
				if (conditionArguments.size > THIRD_ARGUMENT_INDEX) {
					conditionArguments.set(FIRST_ARGUMENT_INDEX, String.valueOf(modulo));
					conditionArguments.set(FIRST_OPERATOR_INDEX,
							conditionArguments.get(SECOND_OPERATOR_INDEX));
					conditionArguments.set(SECOND_ARGUMENT_INDEX,
							conditionArguments.get(THIRD_ARGUMENT_INDEX));
					return getOperator(conditionArguments.get(FIRST_OPERATOR_INDEX)).checkCondition(
							conditionArguments, parser, parent);
				}
				return modulo == 0;
			}
		},
		MATCHES("mt", "rx", "mtch", "rgx", "matches", "regex") {
			@Override
			public boolean checkCondition(final Array<String> conditionArguments, final LmlParser parser,
					final Actor parent) throws LmlParsingException {
				final String firstArgument = parseFirstArgument(conditionArguments, parser, parent);
				final String secondArgument = parseSecondArgument(conditionArguments, parser, parent);
				return firstArgument.matches(secondArgument);
			}
		},
		EQUALS_IGNORE_CASE("ic", "eic", "ignoreCase", "equalsIgnoreCase") {
			@Override
			public boolean checkCondition(final Array<String> conditionArguments, final LmlParser parser,
					final Actor parent) throws LmlParsingException {
				final String firstArgument = parseFirstArgument(conditionArguments, parser, parent);
				final String secondArgument = parseSecondArgument(conditionArguments, parser, parent);
				return firstArgument.equalsIgnoreCase(secondArgument);
			}
		};

		private static final int FIRST_ARGUMENT_INDEX = 0;
		private static final int FIRST_OPERATOR_INDEX = 1;
		private static final int SECOND_ARGUMENT_INDEX = 2;
		private static final int SECOND_OPERATOR_INDEX = 3;
		private static final int THIRD_ARGUMENT_INDEX = 4;

		private Operator(final String... aliases) {
			for (final String alias : aliases) {
				Constants.OPERATORS.put(alias, this);
			}
		}

		public abstract boolean checkCondition(final Array<String> conditionArguments,
				final LmlParser parser, final Actor parent);

		protected boolean isInt(final String argument) {
			for (final char character : argument.toCharArray()) {
				if (character < '0' || character > '9') {
					return false;
				}
			}
			return true;
		}

		protected boolean isFloat(final String argument) {
			boolean dotAppeared = false;
			for (final char character : argument.toCharArray()) {
				if (character < '0' || character > '9') {
					if (!dotAppeared && character == '.') {
						dotAppeared = true;
						continue;
					}
					return false;
				}
			}
			return true;
		}

		public static Operator getOperator(final String alias) {
			return Constants.OPERATORS.get(alias);
		}

		private static String parseFirstArgument(final Array<String> conditionArguments,
				final LmlParser parser, final Actor parent) {
			return parser.parseStringData(conditionArguments.get(FIRST_ARGUMENT_INDEX), parent);
		}

		private static String parseSecondArgument(final Array<String> conditionArguments,
				final LmlParser parser, final Actor parent) {
			return parser.parseStringData(conditionArguments.get(SECOND_ARGUMENT_INDEX), parent);
		}

		private static class Constants {
			private final static ObjectMap<String, Operator> OPERATORS = GdxMaps.newObjectMap();
		}
	}
}
