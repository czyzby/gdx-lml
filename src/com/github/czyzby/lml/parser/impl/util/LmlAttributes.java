package com.github.czyzby.lml.parser.impl.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.common.Nullables;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.ActorConsumer;
import com.github.czyzby.lml.util.LmlSyntax;

/** LML tag attributes parsing utilities.
 *
 * @author MJ */
public class LmlAttributes implements LmlSyntax {
	private static final ObjectMap<String, Value> STATIC_TABLE_VALUES = GdxMaps.newObjectMap("minHeight",
			Value.minHeight, "prefHeight", Value.prefHeight, "maxHeight", Value.maxHeight, "minWidth",
			Value.minWidth, "prefWidth", Value.prefWidth, "maxWidth", Value.maxWidth);

	public LmlAttributes() {
	}

	/** Registers the given value under passed name. These values should be generic and work only on context
	 * actors. Where a Value is expected (for example - in tables' cell sizes), you can pass chosen valueName
	 * as the attribute and the corresponding Value implementation will be taken from a map. */
	public static void registerTableValue(final String valueName, final Value value) {
		STATIC_TABLE_VALUES.put(valueName, value);
	}

	/** @return boolean parsed from the attribute. False for null. */
	public static boolean parseBoolean(final Actor actor, final LmlParser parser, final String attributeValue) {
		final String attribute = parseString(actor, parser, attributeValue);
		if (Strings.isEmpty(attribute) || BOOLEAN_FALSE_ATTRIBUTE_VALUE.equalsIgnoreCase(attribute)) {
			return false;
		} else if (BOOLEAN_TRUE_ATTRIBUTE_VALUE.equalsIgnoreCase(attribute)) {
			return true;
		}
		throw new LmlParsingException(
				"Boolean attribute value expected (true or false, case ignored). Received: " + attribute
						+ ".", parser);
	}

	/** @param attributeName its value cannot be null. Has to represent a value from Alignment utility enum.
	 * @return attribute converted into LibGDX alignment. */
	public static int parseAlignment(final Actor actor, final LmlParser parser, final String attributeValue) {
		return Alignment.valueOf(parseString(actor, parser, attributeValue).toUpperCase()).getAlignment();
	}

	/** @return attribute parsed as a number. */
	public static float parseFloat(final Actor actor, final LmlParser parser, final String attributeValue) {
		return Float.parseFloat(parseString(actor, parser, attributeValue));
	}

	/** @return attribute parsed as a number. */
	public static int parseInt(final Actor actor, final LmlParser parser, final String attributeValue) {
		return Integer.parseInt(parseString(actor, parser, attributeValue));
	}

	/** @return parsed string value. Honors bundles, preferences and actions. */
	public static String parseString(final Actor actor, final LmlParser parser, final String attributeValue) {
		return parser.parseStringData(attributeValue, actor);
	}

	/** @param attributeValue expected action name. May (but does not have to) start with &, which will be
	 *            ignored when extracting action.
	 * @return action connected with the action key mapped by the parser. */
	public static ActorConsumer<Object, Object> parseAction(final Actor actor, final LmlParser parser,
			final String attributeValue) {
		return attributeValue.charAt(0) == ACTION_OPERATOR ? parser.findAction(attributeValue.substring(1),
				actor) : parser.findAction(attributeValue, actor);
	}

	/** @param attributeValue action, number, %number, number% or registered value name.
	 * @return if action: will invoke action - if the result is a Value instance, it will be returned. If
	 *         number: a Fixed value will be returned. If %number: a value that returns a percent of actor
	 *         height will be returned. If number%: a value that returns a percent of parent's height will be
	 *         returned. If value name: Value will be extracted from the static values map and returned. */
	public static Value parseVerticalValue(final Actor actor, final Table parent, final LmlParser parser,
			final String attributeValue) {
		final String attributeToParse;
		if (attributeValue.charAt(0) == ACTION_OPERATOR) {
			final Object actionResult = parser.findAction(attributeValue.substring(1), actor).consume(actor);
			if (actionResult instanceof Value) {
				return (Value) actionResult;
			}
			attributeToParse = Nullables.toString(actionResult, NULL_ARGUMENT);
		} else {
			attributeToParse = parseString(actor, parser, attributeValue);
		}
		return determineVerticalValue(parent, attributeToParse, parser);
	}

	/** @param attributeValue action, number, %number, number% or registered value name.
	 * @return if action: will invoke action - if the result is a Value instance, it will be returned. If
	 *         number: a Fixed value will be returned. If %number: a value that returns a percent of actor
	 *         width will be returned. If number%: a value that returns a percent of parent's width will be
	 *         returned. If value name: Value will be extracted from the static values map and returned. */
	private static Value determineVerticalValue(final Table parent, final String attributeToParse,
			final LmlParser parser) {
		if (Strings.isFloat(attributeToParse)) {
			return new Value.Fixed(Float.parseFloat(attributeToParse));
		} else if (attributeToParse.charAt(attributeToParse.length() - 1) == PERCENT_OPERATOR) {
			return Value.percentHeight(
					Float.parseFloat(attributeToParse.substring(0, attributeToParse.length() - 1)), parent);
		} else if (attributeToParse.charAt(0) == PERCENT_OPERATOR) {
			return Value.percentHeight(Float.parseFloat(attributeToParse.substring(1)));
		}
		if (!STATIC_TABLE_VALUES.containsKey(attributeToParse)) {
			throw new LmlParsingException(
					"Invalid argument for table Value: "
							+ attributeToParse
							+ ". Table Value object expected. No Value registered with that name. "
							+ "For fixed value, pass number. For percent of actor's height, pass %number. For percent of parent's height, pass number%."
							+ "If this name is connected with an action, it should start with &.");
		}
		return STATIC_TABLE_VALUES.get(attributeToParse);
	}

	public static Value parseHorizontalValue(final Actor actor, final Table parent, final LmlParser parser,
			final String attributeValue) {
		final String attributeToParse;
		if (attributeValue.charAt(0) == ACTION_OPERATOR) {
			final Object actionResult = parser.findAction(attributeValue.substring(1), actor).consume(actor);
			if (actionResult instanceof Value) {
				return (Value) actionResult;
			}
			attributeToParse = Nullables.toString(actionResult, NULL_ARGUMENT);
		} else {
			attributeToParse = parseString(actor, parser, attributeValue);
		}
		return determineHorizontalValue(parent, attributeToParse, parser);
	}

	private static Value determineHorizontalValue(final Table parent, final String attributeToParse,
			final LmlParser parser) {
		if (Strings.isFloat(attributeToParse)) {
			return new Value.Fixed(Float.parseFloat(attributeToParse));
		} else if (attributeToParse.charAt(attributeToParse.length() - 1) == PERCENT_OPERATOR) {
			return Value.percentWidth(
					Float.parseFloat(attributeToParse.substring(0, attributeToParse.length() - 1)), parent);
		} else if (attributeToParse.charAt(0) == PERCENT_OPERATOR) {
			return Value.percentWidth(Float.parseFloat(attributeToParse.substring(1)));
		}
		if (!STATIC_TABLE_VALUES.containsKey(attributeToParse)) {
			throw new LmlParsingException(
					"Invalid argument for table Value: "
							+ attributeToParse
							+ ". Table Value object expected. No Value registered with that name. "
							+ "For fixed value, pass number. For percent of actor's width, pass %number. For percent of parent's width, pass number%."
							+ "If this name is connected with an action, it should start with &.");
		}
		return STATIC_TABLE_VALUES.get(attributeToParse);
	}

	public static void throwErrorIfStrict(final LmlParser parser, final String message) {
		if (parser.isStrict()) {
			throw new LmlParsingException(message, parser);
		}
	}
}
