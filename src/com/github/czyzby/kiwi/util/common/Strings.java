package com.github.czyzby.kiwi.util.common;

/** Utility class for strings and CharSequences (sometimes expected or returned by Scene2D API).
 *
 * @author MJ */
public class Strings {
	/** A string with length of 0, not null. */
	public static final String EMPTY_STRING = "";
	/** Common regex. */
	public static final String WHITESPACE_REGEX = "\\s*";

	private Strings() {
	}

	/** @return true if passed char sequence is null or has no characters. */
	public static boolean isEmpty(final CharSequence charSequence) {
		return charSequence == null || charSequence.length() == 0;
	}

	/** @return true if passed char sequence is not null and has at least one character. */
	public static boolean isNotEmpty(final CharSequence charSequence) {
		return charSequence != null && charSequence.length() > 0;
	}

	/** @return true if the passed sequence is null or contains only whitespace characters. */
	public static boolean isWhitespace(final CharSequence charSequence) {
		if (isEmpty(charSequence)) {
			return true;
		}
		for (int index = 0; index < charSequence.length(); index++) {
			final char character = charSequence.charAt(index);
			if (isNotWhitespace(character)) {
				return false;
			}
		}
		return true;
	}

	/** @return true if the passed sequence is not null and contains at least one non-whitespace character. */
	public static boolean isNotWhitespace(final CharSequence charSequence) {
		if (isEmpty(charSequence)) {
			return false;
		}
		for (int index = 0; index < charSequence.length(); index++) {
			final char character = charSequence.charAt(index);
			if (isNotWhitespace(character)) {
				return true;
			}
		}
		return false;
	}

	/** GWT utility. Character.isWhitespace not supported.
	 *
	 * @return true if character is a whitespace. */
	public static boolean isWhitespace(final char character) {
		return character == ' ' || character == '\t' || character == '\n' || character == '\r'
				|| character == '\f';
	}

	/** GWT utility. Character.isWhitespace not supported.
	 *
	 * @return true if character is not a whitespace. */
	public static boolean isNotWhitespace(final char character) {
		return character != ' ' && character != '\t' && character != '\n' && character != '\r'
				&& character != '\f';
	}

	/** @return true if passed character is a new line. */
	public static boolean isNewLine(final char character) {
		return character == '\n' || character == '\r';
	}

	/** @return true if passed character is not a new line. */
	public static boolean isNotNewLine(final char character) {
		return character != '\n' && character != '\r';
	}

	/** @return true if the passed sequence starts with the given character. */
	public static boolean startsWith(final CharSequence charSequence, final char character) {
		return charSequence.length() > 0 && charSequence.charAt(0) == character;
	}

	/** @return true if the passed sequence starts with the given characters. */
	public static boolean startsWith(final CharSequence charSequence, final char character0,
			final char character1) {
		return charSequence.length() > 1 && charSequence.charAt(0) == character0
				&& charSequence.charAt(1) == character1;
	}

	/** @return true if the passed sequence starts with the given characters. */
	public static boolean startsWith(final CharSequence charSequence, final char character0,
			final char character1, final char character2) {
		return charSequence.length() > 2 && charSequence.charAt(0) == character0
				&& charSequence.charAt(1) == character1 && charSequence.charAt(2) == character2;
	}

	/** @return true if the passed sequence starts with the given characters. */
	public static boolean startsWith(final CharSequence charSequence, final char character0,
			final char character1, final char character2, final char character3) {
		return charSequence.length() > 3 && charSequence.charAt(0) == character0
				&& charSequence.charAt(1) == character1 && charSequence.charAt(2) == character2
				&& charSequence.charAt(3) == character3;
	}

	/** @return true if the passed sequence starts with the given characters. */
	public static boolean startsWith(final CharSequence charSequence, final char... characters) {
		if (charSequence.length() >= characters.length) {
			for (int index = 0; index < characters.length; index++) {
				if (charSequence.charAt(index) != characters[index]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/** @return true if the passed sequence ends with the given character. */
	public static boolean endsWith(final CharSequence charSequence, final char character) {
		return charSequence.length() > 0 && charSequence.charAt(charSequence.length() - 1) == character;
	}

	/** @return true if the passed sequence ends with the given characters. */
	public static boolean endsWith(final CharSequence charSequence, final char character0,
			final char character1) {
		return charSequence.length() > 1 && charSequence.charAt(charSequence.length() - 1) == character1
				&& charSequence.charAt(charSequence.length() - 2) == character0;
	}

	/** @return true if the passed sequence ends with the given characters. */
	public static boolean endsWith(final CharSequence charSequence, final char character0,
			final char character1, final char character2) {
		return charSequence.length() > 2 && charSequence.charAt(charSequence.length() - 1) == character2
				&& charSequence.charAt(charSequence.length() - 2) == character1
				&& charSequence.charAt(charSequence.length() - 3) == character0;
	}

	/** @return true if the passed sequence ends with the given characters. */
	public static boolean endsWith(final CharSequence charSequence, final char character0,
			final char character1, final char character2, final char character3) {
		return charSequence.length() > 3 && charSequence.charAt(charSequence.length() - 1) == character3
				&& charSequence.charAt(charSequence.length() - 2) == character2
				&& charSequence.charAt(charSequence.length() - 3) == character1
				&& charSequence.charAt(charSequence.length() - 4) == character0;
	}

	/** @return true if the passed sequence ends with the given characters. */
	public static boolean endsWith(final CharSequence charSequence, final char... characters) {
		if (charSequence.length() >= characters.length) {
			for (int index = 0, modifier = charSequence.length() - characters.length; index < characters.length; index++) {
				if (charSequence.charAt(index + modifier) != characters[index]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/** @return true if the given sequence contains selected character. */
	public static boolean contains(final CharSequence charSequence, final char character) {
		for (int index = 0; index < charSequence.length(); index++) {
			if (charSequence.charAt(index) == character) {
				return true;
			}
		}
		return false;
	}

	/** @param stringBuilder will have its length set as 0. */
	public static void clearBuilder(final com.badlogic.gdx.utils.StringBuilder stringBuilder) {
		stringBuilder.setLength(0);
	}

	/** @param stringBuilder will have its length set as 0. */
	public static void clearBuilder(final StringBuilder stringBuilder) {
		stringBuilder.setLength(0);
	}

	/** @param separator will be used to separate joined strings from each other. Passing null or empty string
	 *            will result in merging strings without any separation.
	 * @param objectsToJoin will be converted to strings and joined using the selected separator. Nulls are
	 *            converted into "null" strings.
	 * @return passed objects as strings joined into one object. Note that this string will never be null - if
	 *         objectsToJoin are empty, an empty string is returned. */
	public static String join(final CharSequence separator, final Object... objectsToJoin) {
		if (objectsToJoin == null || objectsToJoin.length == 0) {
			return EMPTY_STRING;
		}
		if (objectsToJoin.length == 1) {
			// Avoiding unnecessary operations.
			return objectsToJoin[0] == null ? Nullables.DEFAULT_NULL_STRING : objectsToJoin[0].toString();
		}

		final StringBuilder stringBuilder = new StringBuilder();
		if (isEmpty(separator)) {
			// No separator - merging strings.
			for (final Object element : objectsToJoin) {
				stringBuilder.append(element);
			}
			return stringBuilder.toString();
		}
		// A separator is selected - joining strings with selected separator.
		int index = 0;
		stringBuilder.append(objectsToJoin[index++]);
		for (; index < objectsToJoin.length; index++) {
			stringBuilder.append(separator);
			stringBuilder.append(objectsToJoin[index]);
		}
		return stringBuilder.toString();
	}

	/** @param objectsToMerge will be converted and merged into one string, without using any separator. Note
	 *            that nulls will be added as "null" strings.
	 * @return merged objects as strings. Is never null - for empty objectsToMerge, returns empty string.
	 *         Equivalent to using join method with null or empty separator. */
	public static String merge(final Object... objectsToMerge) {
		return join(null, objectsToMerge);
	}

	/** @param separator will be used to separate joined strings from each other. Passing null or empty string
	 *            will result in merging strings without any separation.
	 * @param objectsToJoin will be converted to strings and joined using the selected separator. Nulls are
	 *            completely ignored - they are not added to the string and do not invoke adding separators.
	 * @return passed objects joined as strings into one object. Note that this string will never be null - if
	 *         objectsToJoin are empty, an empty string is returned. */
	public static String joinIgnoringNulls(final CharSequence separator, final Object... objectsToJoin) {
		if (objectsToJoin == null || objectsToJoin.length == 0) {
			return EMPTY_STRING;
		}
		if (objectsToJoin.length == 1) {
			// Avoiding unnecessary operations.
			return objectsToJoin[0] == null ? EMPTY_STRING : objectsToJoin[0].toString();
		}

		final StringBuilder stringBuilder = new StringBuilder();
		if (isEmpty(separator)) {
			// No separator - merging strings.
			for (final Object element : objectsToJoin) {
				if (element != null) {
					stringBuilder.append(element);
				}
			}
			return stringBuilder.toString();
		}
		// A separator is selected - joining strings with selected separator.
		int index = 0;
		for (; index < objectsToJoin.length; index++) {
			if (objectsToJoin[index] != null) {
				stringBuilder.append(objectsToJoin[index]);
				break;
			}
		}
		for (; index < objectsToJoin.length; index++) {
			if (objectsToJoin[index] == null) {
				continue;
			}
			stringBuilder.append(separator);
			stringBuilder.append(objectsToJoin[index]);
		}
		return stringBuilder.toString();
	}

	/** @param objectsToMerge will be converted and merged into one string, without using any separator. Note
	 *            that nulls will not be added at all.
	 * @return merged objects as strings. Is never null - for empty objectsToMerge, returns empty string.
	 *         Equivalent to using joinIgnoringNulls method with null or empty separator. */
	public static String mergeIgnoringNulls(final Object... objectsToMerge) {
		return joinIgnoringNulls(null, objectsToMerge);
	}

	/** @return nullable object converted to string. If parameter is null, empty string is returned. As long as
	 *         toString is properly implemented in the object, this method never returns null. */
	public static String toString(final Object nullable) {
		return nullable == null ? EMPTY_STRING : nullable.toString();
	}

	/** @return nullable object converted to string. If first parameter is null, onNull parameter is returned. */
	public static String toString(final Object nullable, final String onNull) {
		return nullable == null ? onNull : nullable.toString();
	}

	/** @return true if the passed charSequence contains legal characters for an int. Note that the value of int
	 *         is not validated and can be too big or small. */
	public static boolean isInt(final CharSequence charSequence) {
		if (isEmpty(charSequence)) {
			return false;
		}
		int index = 0;
		if (charSequence.charAt(0) == '-') {
			if (charSequence.length() > 1) {
				index++;
			} else {
				return false;
			}
		}
		for (; index < charSequence.length(); index++) {
			final char character = charSequence.charAt(index);
			if (character < '0' || character > '9') {
				return false;
			}
		}
		return true;
	}

	/** @return true if the passed charSequence contains legal characters for a float. Note that the value of
	 *         float is not validated and can be too big or small. */
	public static boolean isFloat(final CharSequence charSequence) {
		if (Strings.isEmpty(charSequence)) {
			return false;
		}
		boolean receivedDot = false;
		for (int index = 0; index < charSequence.length(); index++) {
			final char character = charSequence.charAt(index);
			if (character < '0' || character > '9') {
				if (charSequence.length() > 1) {
					if (index == 0 && character == '-') {
						continue;
					} else if (!receivedDot && character == '.') {
						receivedDot = true;
						continue;
					} else if (character == 'f' && index + 1 == charSequence.length()) {
						return true;
					}
				}
				return false;
			}
		}
		return true;
	}

	/** @return a new array with the passed values. Can be empty, but is never null. */
	public static String[] newArray(final String... values) {
		if (values == null) {
			return new String[0];
		}
		return values;
	}
}
