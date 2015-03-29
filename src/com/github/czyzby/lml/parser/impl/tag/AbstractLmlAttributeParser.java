package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.util.LmlSyntax;
import com.github.czyzby.lml.util.gdx.Alignment;

public abstract class AbstractLmlAttributeParser implements LmlSyntax {
	/** @return boolean parsed from the attribute. False for null. */
	protected boolean parseBoolean(final LmlTagData lmlTagData, final String attributeName,
			final LmlParser parser, final Actor forActor) {
		final String attribute = parseString(lmlTagData, attributeName, parser, forActor);
		if (attribute == null || attribute.length() == 0
				|| BOOLEAN_FALSE_ATTRIBUTE_VALUE.equalsIgnoreCase(attribute)) {
			return false;
		} else if (BOOLEAN_TRUE_ATTRIBUTE_VALUE.equalsIgnoreCase(attribute)
				|| attributeName.equalsIgnoreCase(attribute)) {
			return true;
		}
		throw new LmlParsingException(
				"Boolean attribute value expected (true, false or repeated attribute name). Received: "
						+ attribute + ".", parser);
	}

	/** @param attributeName its value cannot be null. Has to represent a value from Alignment utility enum.
	 * @return attribute converted into LibGDX alignment. */
	protected int parseAlignment(final LmlTagData lmlTagData, final String attributeName,
			final LmlParser parser, final Actor forActor) {
		final String attribute = parseString(lmlTagData, attributeName, parser, forActor);
		return Alignment.valueOf(attribute.toUpperCase()).getAlignment();
	}

	/** @return attribute parsed as a number. */
	protected float parseFloat(final LmlTagData lmlTagData, final String attributeName,
			final LmlParser parser, final Actor forActor) {
		final String attribute = parseString(lmlTagData, attributeName, parser, forActor);
		return Float.parseFloat(attribute);
	}

	/** @return attribute parsed as a number. */
	protected int parseInt(final LmlTagData lmlTagData, final String attributeName, final LmlParser parser,
			final Actor forActor) {
		final String attribute = parseString(lmlTagData, attributeName, parser, forActor);
		return Integer.parseInt(attribute);
	}

	protected String parseString(final LmlTagData lmlTagData, final String attributeName,
			final LmlParser parser, final Actor forActor) {
		final String rawAttribute = lmlTagData.getAttribute(attributeName);
		return parser.parseStringData(rawAttribute, forActor);
	}

	protected void throwErrorIfStrict(final LmlParser parser, final String message) {
		if (parser.isStrict()) {
			throw new LmlParsingException(message, parser);
		}
	}
}
