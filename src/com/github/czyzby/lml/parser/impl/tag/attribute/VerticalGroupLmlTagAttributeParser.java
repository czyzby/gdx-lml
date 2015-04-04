package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum VerticalGroupLmlTagAttributeParser implements LmlTagAttributeParser {
	FILL_PARENT("fillParent") {
		@Override
		public void apply(final VerticalGroup verticalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			verticalGroup.setFillParent(LmlAttributes.parseBoolean(verticalGroup, parser, attributeValue));
		}
	},
	ALIGNMENT("groupAlign") {
		@Override
		protected void apply(final VerticalGroup verticalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			verticalGroup.align(LmlAttributes.parseAlignment(verticalGroup, parser, attributeValue));
		}
	},
	PAD("groupPad") {
		@Override
		protected void apply(final VerticalGroup verticalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			verticalGroup.pad(LmlAttributes.parseFloat(verticalGroup, parser, attributeValue));
		}
	},
	PAD_TOP("groupPadTop") {
		@Override
		protected void apply(final VerticalGroup verticalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			verticalGroup.padTop(LmlAttributes.parseFloat(verticalGroup, parser, attributeValue));
		}
	},
	PAD_BOTTOM("groupPadBottom") {
		@Override
		protected void apply(final VerticalGroup verticalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			verticalGroup.padBottom(LmlAttributes.parseFloat(verticalGroup, parser, attributeValue));
		}
	},
	PAD_LEFT("groupPadLeft") {
		@Override
		protected void apply(final VerticalGroup verticalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			verticalGroup.padLeft(LmlAttributes.parseFloat(verticalGroup, parser, attributeValue));
		}
	},
	PAD_RIGHT("groupPadRight") {
		@Override
		protected void apply(final VerticalGroup verticalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			verticalGroup.padRight(LmlAttributes.parseFloat(verticalGroup, parser, attributeValue));
		}
	},
	FILL("groupFill") {
		@Override
		protected void apply(final VerticalGroup verticalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			verticalGroup.fill(LmlAttributes.parseFloat(verticalGroup, parser, attributeValue));
		}
	},
	SPACING("groupSpace", "groupSpacing") {
		@Override
		protected void apply(final VerticalGroup verticalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			verticalGroup.space(LmlAttributes.parseFloat(verticalGroup, parser, attributeValue));
		}
	},
	REVERSE("reverse") {
		@Override
		protected void apply(final VerticalGroup verticalGroup, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			verticalGroup.reverse(LmlAttributes.parseBoolean(verticalGroup, parser, attributeValue));
		}
	};
	private final String[] aliases;

	private VerticalGroupLmlTagAttributeParser(final String... aliases) {
		this.aliases = aliases;
	}

	@Override
	public void apply(final Object actor, final LmlParser parser, final String attributeValue,
			final LmlTagData lmlTagData) {
		apply((VerticalGroup) actor, parser, attributeValue, lmlTagData);
	}

	protected abstract void apply(VerticalGroup verticalGroup, LmlParser parser, String attributeValue,
			LmlTagData lmlTagData);

	@Override
	public String[] getAttributeNames() {
		return aliases;
	}
}
