package com.github.czyzby.lml.util;

/** An ugly utility that contains all LML-relevant characters and strings. Has no methods. Implement for easy
 * access to variable (and possibly chaotic code).
 *
 * @author MJ */
public interface LmlSyntax {
	char TAG_OPENING = '<';
	char TAG_CLOSING = '>';
	char CLOSED_TAG_SIGN = '/';

	char COMMENT_START_OPERATOR = '!';
	char COMMENT_END_OPERATOR = '-';
	char SCHEMA_COMMENT_OPERATOR = '?';

	char ARGUMENT_SIGN = '$';
	char ARGUMENT_OPENING = '{';
	char ARGUMENT_CLOSING = '}';
	char ARGUMENT_SEPARATOR = ';';

	char MACRO_SIGN = '@';
	char PREFERENCE_SIGN = '#';
	String PREFERENCE_SEPARATOR = "#";

	char BUNDLE_LINE_SIGN = '@';
	String BUNDLE_LINE_SEPARATOR = "@";
	String BUNDLE_LINE_ARGUMENT_SEPARATOR = "[|]";

	String ARRAY_SEPARATOR_REGEX = ";";
	char RANGE_ARGUMENT_OPENING = '[';
	char RANGE_ARGUMENT_SEPARATOR = '-';
	String RANGE_ARGUMENT_SEPARATOR_REGEX = "-";
	char RANGE_ARGUMENT_CLOSING = ']';
	char PERCENT_OPERATOR = '%';

	char ACTION_OPERATOR = '&';
	char ACTION_SEPARATOR = '.';
	String ACTION_SEPARATOR_REGEX = "[.]";

	String NULL_ARGUMENT = "NULL";
	String BOOLEAN_TRUE_ATTRIBUTE_VALUE = "TRUE";
	String BOOLEAN_FALSE_ATTRIBUTE_VALUE = "FALSE";
	String DEFAULT_VALUE_NAME = "default";

	char TAB = '\t';
	char SPACE = ' ';
	char NEW_LINE = '\n';
	char CARRIAGE_RETURN = '\r';

	String SPACE_SEPARATOR = " ";
	String WHITESPACE_REGEX = "\\s+";

	char QUOTATION_CHAR = '\'';
	char DOUBLE_QUOTATION_CHAR = '"';

	String ATTRIBUTE_SEPARATOR = "=";
}
