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

	char BUNDLE_LINE_OPENING = '@';
	String BUNDLE_LINE_ARGUMENT_SEPARATOR = "[|]";

	String ARRAY_SEPARATOR_REGEX = ";";
	char RANGE_ARGUMENT_OPENING = '[';
	char RANGE_ARGUMENT_SEPARATOR = '-';
	String RANGE_ARGUMENT_SEPARATOR_REGEX = "-";
	char RANGE_ARGUMENT_CLOSING = ']';

	char ACTION_OPERATOR = '&';
	char ACTION_SEPARATOR = '.';
	String ACTION_SEPARATOR_REGEX = "[.]";

	String EMPTY_STRING = "";
	String NULL_ARGUMENT = "NULL";
	String BOOLEAN_TRUE_ATTRIBUTE_VALUE = "TRUE";
	String BOOLEAN_FALSE_ATTRIBUTE_VALUE = "FALSE";

	char TAB = '\t';
	char SPACE = ' ';
	char NEW_LINE = '\n';
	char CARRIAGE_RETURN = '\r';

	String WHITESPACE_REGEX = "\\s+";

	char QUOTATION_CHAR = '\'';
	char DOUBLE_QUOTATION_CHAR = '"';

	String ATTRIBUTE_SEPARATOR = "=";

	String ID_ATTRIBUTE = "ID";
	String ON_CLICK_ATTRIBUTE = "ONCLICK";
	String ON_CHANGE_ATTRIBUTE = "ONCHANGE";
	String ON_CREATE_ATTRIBUTE = "ONCREATE";
	String STYLE_ATTRIBUTE = "STYLE";
	String CLASS_ATTRIBUTE = "CLASS";
	String VISIBLE_ATTRIBUTE = "VISIBLE";
	String TOUCHABLE_ATTRIBUTE = "TOUCHABLE";
	String FILL_PARENT_ATTRIBUTE = "FILLPARENT";
	String TOOLTIP_ATTRIBUTE = "TOOLTIP";
	String TOOLTIP_ID = "TOOLTIPID";
	String TOOLTIP_STYLE_ATTRIBUTE = "TOOLTIPSTYLE";
	String DEBUG_ATTRIBUTE = "DEBUG";
	String TREE_NODE_ATTRIBUTE = "TREENODE";
}
