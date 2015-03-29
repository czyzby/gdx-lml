package com.konfigurats.lml.parser.impl.dto;

import com.badlogic.gdx.utils.Array;
import com.konfigurats.lml.error.LmlParsingException;

public class LmlMacroData extends AbstractLmlDto {
	public static final char LML_MACRO_OPENING = '@';

	private final String macroName;
	private final Array<String> arguments;
	private final boolean closed;

	public LmlMacroData(final String macroName, final Array<String> arguments, final boolean closed) {
		this.macroName = macroName;
		this.arguments = arguments;
		this.closed = closed;
	}

	public static LmlMacroData parse(final CharSequence rawData) {
		validateMacroRawData(rawData);
		final boolean isClosed = isClosed(rawData);
		final String macroData = extractMacroData(rawData);
		final String[] macroAttributes = extractMacroAttributes(rawData, macroData);
		final String macroName = getMacroName(macroAttributes);
		final Array<String> arguments = getMacroArguments(macroAttributes);
		return new LmlMacroData(macroName, arguments, isClosed);
	}

	private static boolean isClosed(final CharSequence rawData) {
		return rawData.charAt(rawData.length() - 1) == CLOSED_TAG_SIGN;
	}

	private static String extractMacroData(final CharSequence rawData) {
		if (isClosed(rawData)) {
			return escapeSpaces(rawData.subSequence(1, rawData.length() - 1).toString());
		}
		return escapeSpaces(rawData.subSequence(1, rawData.length()).toString());
	}

	private static void validateMacroRawData(final CharSequence rawData) {
		if (rawData.charAt(0) != LML_MACRO_OPENING) {
			throw new LmlParsingException("Invalid parser implementation. Raw data is not a macro: "
					+ rawData + ".");
		}
	}

	private static String[] extractMacroAttributes(final CharSequence rawData, final String macroData) {
		final String[] macroAttributes = macroData.split(WHITESPACE_REGEX);
		validateAttributes(rawData, macroAttributes);
		return macroAttributes;
	}

	private static void validateAttributes(final CharSequence rawData, final String[] macroAttributes) {
		if (macroAttributes.length == 0) {
			throw new LmlParsingException("Invalid macro tag: " + rawData + ".");
		}
	}

	private static String getMacroName(final String[] macroAttributes) {
		return macroAttributes[0].toUpperCase();
	}

	private static Array<String> getMacroArguments(final String[] macroAttributes) {
		final Array<String> arguments = new Array<String>();
		if (macroAttributes.length > 1) {
			for (int argumentIndex = 1; argumentIndex < macroAttributes.length; argumentIndex++) {
				arguments.add(getMacroArgument(macroAttributes[argumentIndex]));
			}
		}
		return arguments;
	}

	private static String getMacroArgument(final String string) {
		return escapeQuotation(unescapeSpaces(string));
	}

	/** @return name of the macro used to open the tag. */
	public String getMacroName() {
		return macroName;
	}

	/** @return optional macro arguments. Can be empty, cannot be null. */
	public Array<String> getArguments() {
		return arguments;
	}

	/** @return if true, tag ended with / and has no text in between. */
	public boolean isClosed() {
		return closed;
	}
}
