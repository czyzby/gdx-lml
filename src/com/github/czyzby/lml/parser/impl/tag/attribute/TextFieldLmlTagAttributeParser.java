package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum TextFieldLmlTagAttributeParser implements LmlTagAttributeParser {
	TEXT("text", "value") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			textField.setText(LmlAttributes.parseString(textField, parser, attributeValue));
		}
	},
	CURSOR_POSITION("cursorPosition", "cursor") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			textField.setCursorPosition(LmlAttributes.parseInt(textField, parser, attributeValue));
		}
	},
	PASSWORD_MODE("passwordMode", "password") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			textField.setPasswordMode(LmlAttributes.parseBoolean(textField, parser, attributeValue));
		}
	},
	PASSWORD_CHARACTER("passChar", "passwordCharacter") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			textField.setPasswordCharacter(LmlAttributes.parseString(textField, parser, attributeValue)
					.charAt(0));
		}
	},
	ONLY_FONT_CHARS("onlyFontChars", "fontOnlyChars") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			textField.setOnlyFontChars(LmlAttributes.parseBoolean(textField, parser, attributeValue));
		}
	},
	FOCUS_TRAVERSAL("focusTraversal") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			textField.setFocusTraversal(LmlAttributes.parseBoolean(textField, parser, attributeValue));
		}
	},
	TEXT_ALIGNMENT("textAlign") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			textField.setAlignment(LmlAttributes.parseAlignment(textField, parser, attributeValue));
		}
	},
	BLINK_TIME("blinkTime") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			textField.setBlinkTime(LmlAttributes.parseFloat(textField, parser, attributeValue));
		}
	},
	MESSAGE("message") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			textField.setMessageText(LmlAttributes.parseString(textField, parser, attributeValue));
		}
	},
	SELECT_ALL("selectAll") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			if (LmlAttributes.parseBoolean(textField, parser, attributeValue)) {
				textField.selectAll();
			}
		}
	},
	SELECTION_START("selectionStart") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			final int selectionStart = LmlAttributes.parseInt(textField, parser, attributeValue);
			if (lmlTagData.containsAttribute(SELECTION_END_ATTRIBUTE)) {
				textField.setSelection(
						selectionStart,
						LmlAttributes.parseInt(textField, parser,
								lmlTagData.getAttribute(SELECTION_END_ATTRIBUTE)));
			} else {
				if (textField.getText().length() != 0) {
					textField.setSelection(selectionStart, textField.getText().length() - 1);
				}
			}
		}
	},
	SELECTION_END("selectionEnd") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			final int selectionEnd = LmlAttributes.parseInt(textField, parser, attributeValue);
			if (lmlTagData.containsAttribute(SELECTION_START_ATTRIBUTE)) {
				textField.setSelection(
						LmlAttributes.parseInt(textField, parser,
								lmlTagData.getAttribute(SELECTION_START_ATTRIBUTE)), selectionEnd);
			} else {
				textField.setSelection(0, selectionEnd);
			}
		}
	},
	MAX_LENGTH("maxLength") {
		@Override
		protected void apply(final TextField textField, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			textField.setMaxLength(LmlAttributes.parseInt(textField, parser, attributeValue));
		}
	};
	protected static final String SELECTION_START_ATTRIBUTE = "SELECTIONSTART";
	protected static final String SELECTION_END_ATTRIBUTE = "SELECTIONEND";

	private final String[] aliases;

	private TextFieldLmlTagAttributeParser(final String... aliases) {
		this.aliases = aliases;
	}

	@Override
	public void apply(final Object actor, final LmlParser parser, final String attributeValue,
			final LmlTagData lmlTagData) {
		apply((TextField) actor, parser, attributeValue, lmlTagData);
	}

	protected abstract void apply(TextField textField, LmlParser parser, String attributeValue,
			LmlTagData lmlTagData);

	@Override
	public String[] getAttributeNames() {
		return aliases;
	}
}
