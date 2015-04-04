package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum HorizontalGroupLmlTagAttributeParser implements LmlTagAttributeParser {
	FILL_PARENT("fillParent") {
		@Override
		public void apply(final HorizontalGroup horizontalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			horizontalGroup
					.setFillParent(LmlAttributes.parseBoolean(horizontalGroup, parser, attributeValue));
		}
	},
	ALIGNMENT("groupAlign") {
		@Override
		protected void apply(final HorizontalGroup horizontalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			horizontalGroup.align(LmlAttributes.parseAlignment(horizontalGroup, parser, attributeValue));
		}
	},
	PAD("groupPad") {
		@Override
		protected void apply(final HorizontalGroup horizontalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			horizontalGroup.pad(LmlAttributes.parseFloat(horizontalGroup, parser, attributeValue));
		}
	},
	PAD_TOP("groupPadTop") {
		@Override
		protected void apply(final HorizontalGroup horizontalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			horizontalGroup.padTop(LmlAttributes.parseFloat(horizontalGroup, parser, attributeValue));
		}
	},
	PAD_BOTTOM("groupPadBottom") {
		@Override
		protected void apply(final HorizontalGroup horizontalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			horizontalGroup.padBottom(LmlAttributes.parseFloat(horizontalGroup, parser, attributeValue));
		}
	},
	PAD_LEFT("groupPadLeft") {
		@Override
		protected void apply(final HorizontalGroup horizontalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			horizontalGroup.padLeft(LmlAttributes.parseFloat(horizontalGroup, parser, attributeValue));
		}
	},
	PAD_RIGHT("groupPadRight") {
		@Override
		protected void apply(final HorizontalGroup horizontalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			horizontalGroup.padRight(LmlAttributes.parseFloat(horizontalGroup, parser, attributeValue));
		}
	},
	FILL("groupFill") {
		@Override
		protected void apply(final HorizontalGroup horizontalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			horizontalGroup.fill(LmlAttributes.parseFloat(horizontalGroup, parser, attributeValue));
		}
	},
	SPACING("groupSpace", "groupSpacing") {
		@Override
		protected void apply(final HorizontalGroup horizontalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			horizontalGroup.space(LmlAttributes.parseFloat(horizontalGroup, parser, attributeValue));
		}
	},
	REVERSE("reverse") {
		@Override
		protected void apply(final HorizontalGroup horizontalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			horizontalGroup.reverse(LmlAttributes.parseBoolean(horizontalGroup, parser, attributeValue));
		}
	};
	private final String[] aliases;

	private HorizontalGroupLmlTagAttributeParser(final String... aliases) {
		this.aliases = aliases;
	}

	@Override
	public void apply(final Object actor, final LmlParser parser, final String attributeValue,
			final LmlTagData lmlTagData) {
		apply((HorizontalGroup) actor, parser, attributeValue, lmlTagData);
	}

	protected abstract void apply(HorizontalGroup horizontalGroup, LmlParser parser, String attributeValue,
			LmlTagData lmlTagData);

	@Override
	public String[] getAttributeNames() {
		return aliases;
	}
}
