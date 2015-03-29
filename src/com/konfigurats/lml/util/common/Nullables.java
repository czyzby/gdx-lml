package com.konfigurats.lml.util.common;

/** Provides static utilities for nullable objects to avoid NullPointerExceptions. Java 6 compatible.
 *
 * @author MJ */
public class Nullables {
	/** "null", matches StringBuilder behavior. */
	public static final String DEFAULT_NULL_STRING = "null";

	private Nullables() {
	}

	/** A simple null-check. */
	public static boolean isNull(final Object nullable) {
		return nullable == null;
	}

	/** A simple not-null-check. */
	public static boolean isNotNull(final Object nullable) {
		return nullable != null;
	}

	/** @param nullable probable null.
	 * @param alternative will be return if nullable is null. */
	public static <Type> Type getOrElse(final Type nullable, final Type alternative) {
		return nullable == null ? alternative : nullable;
	}

	/** @return true if objects are equal (using equals method) or if both are null. */
	public static boolean areEqual(final Object first, final Object second) {
		return first == second || first != null && first.equals(second);
	}

	/** @param nullables nullable objects.
	 * @return true if any of the objects is null. */
	public static boolean isAnyNull(final Object... nullables) {
		for (final Object object : nullables) {
			if (object == null) {
				return true;
			}
		}
		return false;
	}

	/** @param nullables nullable objects.
	 * @return true if all passed objects are null. */
	public static boolean areAllNull(final Object... nullables) {
		for (final Object object : nullables) {
			if (object != null) {
				return false;
			}
		}
		return true;
	}

	/** @param nullables nullable objects.
	 * @return true if any of the objects is not null. */
	public static boolean isAnyNotNull(final Object... nullables) {
		for (final Object object : nullables) {
			if (object != null) {
				return true;
			}
		}
		return false;
	}

	/** @param nullables nullable objects.
	 * @return true if all passed objects are not null. */
	public static boolean areAllNotNull(final Object... nullables) {
		for (final Object object : nullables) {
			if (object == null) {
				return false;
			}
		}
		return true;
	}

	/** @return nullable object converted to string. If parameter is null, "null" is returned. As long as
	 *         toString is properly implemented in the object, this method never returns null. */
	public static String toString(final Object nullable) {
		return nullable == null ? DEFAULT_NULL_STRING : nullable.toString();
	}

	/** @return nullable object converted to string. If first parameter is null, onNull parameter is returned. */
	public static String toString(final Object nullable, final String onNull) {
		return nullable == null ? onNull : nullable.toString();
	}
}
