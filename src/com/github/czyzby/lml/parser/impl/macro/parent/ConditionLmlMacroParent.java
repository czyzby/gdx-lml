package com.github.czyzby.lml.parser.impl.macro.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;

public class ConditionLmlMacroParent extends AbstractConditionalLmlMacroParent {
	public ConditionLmlMacroParent(final LmlMacroData lmlMacroData, final LmlParent<?> parent,
			final LmlParser parser) {
		super(lmlMacroData, parent, parser);
	}

	@Override
	protected boolean checkCondition(final LmlParser parser) {
		if (arguments.size == 1) {
			return doSingleArgumentCheck(arguments.get(0), parser);
		}
		final String unparsedOperator = arguments.get(Operator.FIRST_OPERATOR_INDEX);
		final Operator operator = Operator.getOperator(unparsedOperator);
		if (operator == null) {
			if (unparsedOperator.charAt(0) == ACTION_OPERATOR) {
				return (Boolean) parser.findAction(unparsedOperator.substring(1), Array.class).consume(
						arguments);
			}
			// Assuming it was a null check and it received multiple arguments separated by whitespaces.
			// It will probably be the most commonly used feature of this macro. Ignoring.
			return true;
		}
		return operator.hasEnoughParameters(arguments) ? operator.checkCondition(arguments, parser,
				getParentActor()) : true;
	}

	@Override
	protected boolean needsArgument() {
		return true;
	}

	private static enum Operator {
		EQUALS("=", "==", "===", "eq") {
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
		MODULO("%", "mod", "md") {
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
		IS_LAST_IN_MODULO("!%", "lastInMod", "lastInModulo") {
			@Override
			public boolean checkCondition(final Array<String> conditionArguments, final LmlParser parser,
					final Actor parent) {
				final int firstArgument =
						Integer.parseInt(parseFirstArgument(conditionArguments, parser, parent));
				final int secondArgument =
						Integer.parseInt(parseSecondArgument(conditionArguments, parser, parent));
				final int modulo = firstArgument % secondArgument;
				return modulo == secondArgument - 1;
			}
		},
		MATCHES("mt", "rx", "mtch", "rgx", "regex") {
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

		public boolean hasEnoughParameters(final Array<String> arguments) {
			return arguments.size > 2;
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
