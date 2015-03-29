package com.github.czyzby.lml.parser.impl.dto;

import com.github.czyzby.lml.util.LmlSyntax;

public class AbstractLmlDto implements LmlSyntax {
	public static String escapeSpaces(final String value) {
		return value.replace("\\ ", "\\");
	}

	public static String unescapeSpaces(final String value) {
		return value.replace("\\", " ");
	}

	public static String escapeQuotation(final String attribute) {
		if (isStartingAndEndingWith(attribute, QUOTATION_CHAR)
				|| isStartingAndEndingWith(attribute, DOUBLE_QUOTATION_CHAR)) {
			return attribute.substring(1, attribute.length() - 1);
		}
		return attribute;
	}

	public static boolean isStartingAndEndingWith(final String attribute, final char character) {
		return attribute.charAt(0) == character && attribute.charAt(attribute.length() - 1) == character;
	}

}
