package com.konfigurats.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.tag.parent.TextFieldLmlParent;

public class TextFieldLmlTagDataParser extends AbstractLmlTagDataParser<TextField> {
	public static final String TEXT_ATTRIBUTE = "TEXT";
	public static final String VALUE_ATTRIBUTE = "VALUE";
	public static final String CURSOR_POSITION_ATTRIBUTE = "CURSORPOSITION";
	public static final String PASSWORD_MODE_ATTRIBUTE = "PASSWORD";
	public static final String PASSWORD_CHARACTER_ATTRIBUTE = "PASSWORDCHAR";
	public static final String ONLY_FONT_CHARS_ATTRIBUTE = "ONLYFONTCHARS";
	public static final String FILL_PARENT_ATTRIBUTE = "FILLPARENT";
	public static final String FOCUS_TRAVERSAL_ATTRIBUTE = "FOCUSTRAVERSAL";
	public static final String SELECT_ALL_ATTRIBUTE = "SELECTALL";
	public static final String SELECTION_START_ATTRIBUTE = "SELECTIONSTART";
	public static final String SELECTION_END_ATTRIBUTE = "SELECTIONEND";
	public static final String TEXT_ALIGNMENT_ATTRIBUTE = "TEXTALIGN";
	public static final String BLINK_TIME_ATTRIBUTE = "BLINKTIME";
	public static final String MESSAGE_ATTRIBUTE = "MESSAGE";

	@Override
	protected TextField parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final TextField textField = getNewInstance(lmlTagData, parser);
		textField.setMessageText(lmlTagData.getAttribute(MESSAGE_ATTRIBUTE));
		setFieldData(lmlTagData, parser, textField);
		setSelection(lmlTagData, parser, textField);
		setPasswordMode(lmlTagData, parser, textField);
		return textField;
	}

	protected TextField getNewInstance(final LmlTagData lmlTagData, final LmlParser parser) {
		return new TextField(getInitialText(lmlTagData, parser), parser.getSkin(), getStyleName(lmlTagData,
				parser));
	}

	protected String getInitialText(final LmlTagData lmlTagData, final LmlParser parser) {
		if (lmlTagData.containsAttribute(TEXT_ATTRIBUTE)) {
			return parseString(lmlTagData, TEXT_ATTRIBUTE, parser, null);
		}
		return lmlTagData.containsAttribute(VALUE_ATTRIBUTE) ? parseString(lmlTagData, VALUE_ATTRIBUTE,
				parser, null) : EMPTY_STRING;
	}

	private void setFieldData(final LmlTagData lmlTagData, final LmlParser parser, final TextField textField) {
		if (lmlTagData.containsAttribute(FILL_PARENT_ATTRIBUTE)) {
			textField.setFillParent(parseBoolean(lmlTagData, FILL_PARENT_ATTRIBUTE, parser, textField));
		}
		if (lmlTagData.containsAttribute(BLINK_TIME_ATTRIBUTE)) {
			textField.setBlinkTime(parseFloat(lmlTagData, BLINK_TIME_ATTRIBUTE, parser, textField));
		}
		if (lmlTagData.containsAttribute(TEXT_ALIGNMENT_ATTRIBUTE)) {
			textField.setAlignment(parseAlignment(lmlTagData, TEXT_ALIGNMENT_ATTRIBUTE, parser, textField));
		}
	}

	private void setSelection(final LmlTagData lmlTagData, final LmlParser parser, final TextField textField) {
		if (lmlTagData.containsAttribute(CURSOR_POSITION_ATTRIBUTE)) {
			textField.setCursorPosition(parseInt(lmlTagData, CURSOR_POSITION_ATTRIBUTE, parser, textField));
		}
		if (lmlTagData.containsAttribute(FOCUS_TRAVERSAL_ATTRIBUTE)) {
			textField
					.setFocusTraversal(parseBoolean(lmlTagData, FOCUS_TRAVERSAL_ATTRIBUTE, parser, textField));
		}
		if (parseBoolean(lmlTagData, SELECT_ALL_ATTRIBUTE, parser, textField)) {
			textField.selectAll();
		}
		if (lmlTagData.containsAttribute(SELECTION_START_ATTRIBUTE)) {
			if (lmlTagData.containsAttribute(SELECTION_END_ATTRIBUTE)) {
				textField.setSelection(parseInt(lmlTagData, SELECTION_START_ATTRIBUTE, parser, textField),
						parseInt(lmlTagData, SELECTION_END_ATTRIBUTE, parser, textField));
			}
			textField.setSelection(parseInt(lmlTagData, SELECTION_START_ATTRIBUTE, parser, textField),
					textField.getText().length());
		} else if (lmlTagData.containsAttribute(SELECTION_END_ATTRIBUTE)) {
			textField.setSelection(0, parseInt(lmlTagData, SELECTION_END_ATTRIBUTE, parser, textField));
		}
	}

	private void setPasswordMode(final LmlTagData lmlTagData, final LmlParser parser,
			final TextField textField) {
		textField.setPasswordMode(parseBoolean(lmlTagData, PASSWORD_MODE_ATTRIBUTE, parser, textField));
		if (lmlTagData.containsAttribute(PASSWORD_CHARACTER_ATTRIBUTE)) {
			textField.setPasswordCharacter(lmlTagData.getAttribute(PASSWORD_CHARACTER_ATTRIBUTE).charAt(0));
		}
		if (lmlTagData.containsAttribute(ONLY_FONT_CHARS_ATTRIBUTE)) {
			textField
					.setOnlyFontChars(parseBoolean(lmlTagData, ONLY_FONT_CHARS_ATTRIBUTE, parser, textField));
		}
	}

	@Override
	protected LmlParent<TextField> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return new TextFieldLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent, parser);
	}

}
