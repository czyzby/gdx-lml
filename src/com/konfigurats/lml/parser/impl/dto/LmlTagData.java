package com.konfigurats.lml.parser.impl.dto;

import com.badlogic.gdx.utils.ObjectMap;
import com.konfigurats.lml.error.LmlParsingException;

public class LmlTagData extends AbstractLmlDto {
	private final String tagName;
	private final String id;
	private final ObjectMap<String, String> attributes;
	private final boolean closed;

	public LmlTagData(final String tagName, final ObjectMap<String, String> attributes, final boolean closed) {
		this.tagName = tagName;
		this.attributes = attributes;
		this.closed = closed;
		id = attributes.get(ID_ATTRIBUTE);
	}

	/** @param rawData data in standard LML format - for example, "tagName id=ID attribute=value /".
	 * @return parsed LmlTagData containing all relevant informations. */
	public static LmlTagData parse(final CharSequence rawData) {
		boolean isClosed;
		String rawTagData;
		if (rawData.charAt(rawData.length() - 1) == CLOSED_TAG_SIGN) {
			rawTagData = rawData.subSequence(0, rawData.length() - 1).toString();
			isClosed = true;
		} else {
			rawTagData = rawData.toString();
			isClosed = false;
		}
		rawTagData = escapeSpaces(rawTagData);
		final String[] tagArguments = extractTagArguments(rawTagData);
		final String tagName = parseAttributeName(tagArguments);
		final ObjectMap<String, String> tagAttributes = getTagAttributes(tagArguments);

		return new LmlTagData(tagName, tagAttributes, isClosed);
	}

	private static ObjectMap<String, String> getTagAttributes(final String[] tagArguments) {
		final ObjectMap<String, String> tagAttributes = new ObjectMap<String, String>(tagArguments.length);
		if (tagArguments.length > 1) {
			for (int index = 1; index < tagArguments.length; index++) {
				final String[] tagAttribute = tagArguments[index].split(ATTRIBUTE_SEPARATOR);
				if (tagAttribute.length != 2) {
					throw new LmlParsingException("Invalid tag attribute: " + tagArguments[index] + ".");
				}
				tagAttributes.put(parseAttributeName(tagAttribute), parseAttributeValue(tagAttribute));
			}
		}
		return tagAttributes;
	}

	private static String parseAttributeName(final String[] tagAttribute) {
		return tagAttribute[0].toUpperCase();
	}

	private static String parseAttributeValue(final String[] tagAttribute) {
		return escapeQuotation(unescapeSpaces(tagAttribute[1]));
	}

	private static String[] extractTagArguments(final String rawTagData) {
		final String[] tagArguments = rawTagData.split(WHITESPACE_REGEX);
		validateArguments(tagArguments);
		return tagArguments;
	}

	private static void validateArguments(final String[] tagArguments) {
		if (tagArguments.length == 0) {
			throw new LmlParsingException("Tag cannot be empty. Use <blank/> for a mock-up actor.");
		}
	}

	/** @return name used to open the tag. */
	public String getTagName() {
		return tagName;
	}

	/** @return widget's unique ID appended by ID attribute. Might be null. */
	public String getId() {
		return id;
	}

	/** @return additional attributes appended to the tag. */
	public ObjectMap<String, String> getAttributes() {
		return attributes;
	}

	public boolean containsAttribute(final String attribute) {
		return attributes.containsKey(attribute);
	}

	public boolean containsAnyAttribute(final String... attributes) {
		for (final String attribute : attributes) {
			if (containsAttribute(attribute)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsAllAttributes(final String... attributes) {
		for (final String attribute : attributes) {
			if (!containsAttribute(attribute)) {
				return false;
			}
		}
		return true;
	}

	public String getAttribute(final String attribute) {
		return attributes.get(attribute);
	}

	/** @return true if tag ends with "/" and is considered closed. */
	public boolean isClosed() {
		return closed;
	}
}
