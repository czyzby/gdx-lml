package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum TreeLmlTagAttributeParser implements LmlTagAttributeParser {
	FILL_PARENT("fillParent") {
		@Override
		public void apply(final Tree tree, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			tree.setFillParent(LmlAttributes.parseBoolean(tree, parser, attributeValue));
		}
	},
	ICON_SPACING("iconSpacing") {
		@Override
		protected void apply(final Tree tree, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			final float spacing = LmlAttributes.parseFloat(tree, parser, attributeValue);
			tree.setIconSpacing(spacing, spacing);
		}
	},
	ICON_SPACING_RIGHT("iconSpacingRight") {
		@Override
		protected void apply(final Tree tree, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			final float leftSpacing;
			// No getters...
			if (lmlTagData.containsAttribute(ICON_SPACING_LEFT_ATTRIBUTE)) {
				leftSpacing =
						LmlAttributes.parseFloat(tree, parser,
								lmlTagData.getAttribute(ICON_SPACING_LEFT_ATTRIBUTE));
			} else if (lmlTagData.containsAttribute(ICON_SPACING_ATTRIBUTE)) {
				leftSpacing =
						LmlAttributes.parseFloat(tree, parser,
								lmlTagData.getAttribute(ICON_SPACING_ATTRIBUTE));
			} else {
				leftSpacing = DEFAULT_SPACING;
			}
			tree.setIconSpacing(leftSpacing, LmlAttributes.parseFloat(tree, parser, attributeValue));
		}
	},
	ICON_SPACING_LEFT("iconSpacingLeft") {
		@Override
		protected void apply(final Tree tree, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			final float rightSpacing;
			// No getters...
			if (lmlTagData.containsAttribute(ICON_SPACING_RIGHT_ATTRIBUTE)) {
				rightSpacing =
						LmlAttributes.parseFloat(tree, parser,
								lmlTagData.getAttribute(ICON_SPACING_RIGHT_ATTRIBUTE));
			} else if (lmlTagData.containsAttribute(ICON_SPACING_ATTRIBUTE)) {
				rightSpacing =
						LmlAttributes.parseFloat(tree, parser,
								lmlTagData.getAttribute(ICON_SPACING_ATTRIBUTE));
			} else {
				rightSpacing = DEFAULT_SPACING;
			}
			tree.setIconSpacing(LmlAttributes.parseFloat(tree, parser, attributeValue), rightSpacing);
		}
	},
	Y_SPACING("ySpacing", "ySpace") {
		@Override
		protected void apply(final Tree tree, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			tree.setYSpacing(LmlAttributes.parseFloat(tree, parser, attributeValue));
		}
	},
	PADDING("treePad", "padding") {
		@Override
		protected void apply(final Tree tree, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			tree.setPadding(LmlAttributes.parseFloat(tree, parser, attributeValue));
		}
	};
	protected static final String ICON_SPACING_ATTRIBUTE = "ICONSPACING";
	protected static final String ICON_SPACING_LEFT_ATTRIBUTE = "ICONSPACINGLEFT";
	protected static final String ICON_SPACING_RIGHT_ATTRIBUTE = "ICONSPACINGRIGHT";
	/** Default values, hard-coded in Tree class. */
	protected static final float DEFAULT_SPACING = 2f;
	private final String[] aliases;

	private TreeLmlTagAttributeParser(final String... aliases) {
		this.aliases = aliases;
	}

	@Override
	public void apply(final Object actor, final LmlParser parser, final String attributeValue,
			final LmlTagData lmlTagData) {
		apply((Tree) actor, parser, attributeValue, lmlTagData);
	}

	protected abstract void apply(Tree tree, LmlParser parser, String attributeValue, LmlTagData lmlTagData);

	@Override
	public String[] getAttributeNames() {
		return aliases;
	}
}
