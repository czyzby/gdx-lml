package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum ButtonLmlTagAttributeParser implements LmlTagAttributeParser {
	IS_CHECKED("isChecked", "checked") {
		@Override
		protected void apply(final Button button, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			button.setChecked(LmlAttributes.parseBoolean(button, parser, attributeValue));
		}
	},
	IS_DISABLED("isDisabled", "disabled", "disable") {
		@Override
		protected void apply(final Button button, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			button.setDisabled(LmlAttributes.parseBoolean(button, parser, attributeValue));
		}
	};
	private final String[] aliases;

	private ButtonLmlTagAttributeParser(final String... aliases) {
		this.aliases = aliases;
	}

	@Override
	public void apply(final Object actor, final LmlParser parser, final String attributeValue,
			final LmlTagData lmlTagData) {
		apply((Button) actor, parser, attributeValue, lmlTagData);
	}

	protected abstract void apply(Button button, LmlParser parser, String attributeValue,
			LmlTagData lmlTagData);

	@Override
	public String[] getAttributeNames() {
		return aliases;
	}
}
