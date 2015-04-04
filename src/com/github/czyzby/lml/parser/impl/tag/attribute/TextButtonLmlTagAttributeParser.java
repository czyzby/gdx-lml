package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum TextButtonLmlTagAttributeParser implements LmlTagAttributeParser {
	HREF("href") {
		@Override
		protected void apply(final TextButton textButton, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			CommonLmlTagAttributeParser.ON_CLICK.apply(textButton, parser, attributeValue, lmlTagData);
		};
	},
	TEXT("text", "value") {
		@Override
		protected void apply(final TextButton textButton, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			textButton.setText(LmlAttributes.parseString(textButton, parser, attributeValue));
		}
	},
	TEXT_ALIGN("textAlign") {
		@Override
		protected void apply(final TextButton textButton, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			textButton.getLabel().setAlignment(
					LmlAttributes.parseAlignment(textButton, parser, attributeValue));
		}
	},
	TEXT_WRAP("textWrap", "wrap") {
		@Override
		protected void apply(final TextButton textButton, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			textButton.getLabel().setWrap(LmlAttributes.parseBoolean(textButton, parser, attributeValue));
		}
	},
	TEXT_ELLIPSIS("textEllipsis", "ellipsis") {
		@Override
		protected void apply(final TextButton textButton, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			textButton.getLabel().setEllipsis(LmlAttributes.parseBoolean(textButton, parser, attributeValue));
		}
	};
	private final String[] aliases;

	private TextButtonLmlTagAttributeParser(final String... aliases) {
		this.aliases = aliases;
	}

	@Override
	public void apply(final Object actor, final LmlParser parser, final String attributeValue,
			final LmlTagData lmlTagData) {
		apply((TextButton) actor, parser, attributeValue, lmlTagData);
	}

	protected abstract void apply(TextButton textButton, LmlParser parser, String attributeValue,
			LmlTagData lmlTagData);

	@Override
	public String[] getAttributeNames() {
		return aliases;
	}
}
