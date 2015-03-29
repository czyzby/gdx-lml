package com.konfigurats.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.tag.parent.TreeLmlParent;

public class TreeLmlTagDataParser extends AbstractGroupLmlTagDataParser<Tree> {
	public static final String ICON_SPACING_LEFT_ATTRIBUTE = "ICONSPACINGLEFT";
	public static final String ICON_SPACING_RIGHT_ATTRIBUTE = "ICONSPACINGRIGHT";
	public static final String Y_SPACING_ATTRIBUTE = "YSPACING";
	public static final String PADDING_ATTRIBUTE = "PADDING";

	@Override
	protected Tree parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final Tree tree = new Tree(parser.getSkin(), getStyleName(lmlTagData, parser));
		setFillParent(tree, lmlTagData, parser);
		setPaddings(lmlTagData, parser, tree);
		setIconSpacing(lmlTagData, parser, tree);
		return tree;
	}

	private void setPaddings(final LmlTagData lmlTagData, final LmlParser parser, final Tree tree) {
		if (lmlTagData.containsAttribute(Y_SPACING_ATTRIBUTE)) {
			tree.setYSpacing(parseFloat(lmlTagData, Y_SPACING_ATTRIBUTE, parser, tree));
		}
		if (lmlTagData.containsAttribute(PADDING_ATTRIBUTE)) {
			tree.setPadding(parseFloat(lmlTagData, PADDING_ATTRIBUTE, parser, tree));
		}
	}

	private void setIconSpacing(final LmlTagData lmlTagData, final LmlParser parser, final Tree tree) {
		if (lmlTagData.containsAllAttributes(ICON_SPACING_LEFT_ATTRIBUTE, ICON_SPACING_RIGHT_ATTRIBUTE)) {
			tree.setIconSpacing(parseFloat(lmlTagData, ICON_SPACING_LEFT_ATTRIBUTE, parser, tree),
					parseFloat(lmlTagData, ICON_SPACING_RIGHT_ATTRIBUTE, parser, tree));
		} else if (lmlTagData.containsAnyAttribute(ICON_SPACING_LEFT_ATTRIBUTE, ICON_SPACING_RIGHT_ATTRIBUTE)) {
			throwErrorIfStrict(parser,
					"Both left and right icon spacing attributes have to be set for a list.");
		}
	}

	@Override
	protected LmlParent<Tree> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
			final LmlParent<?> parent) {
		return new TreeLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent, parser);
	}
}
