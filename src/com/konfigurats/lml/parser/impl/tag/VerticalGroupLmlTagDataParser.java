package com.konfigurats.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.tag.parent.VerticalGroupLmlParent;

public class VerticalGroupLmlTagDataParser extends AbstractGroupLmlTagDataParser<VerticalGroup> {
	public static final String ALIGNMENT_ATTRIBUTE = "GROUPALIGN";
	public static final String PAD_ATTRIBUTE = "GROUPPAD";
	public static final String PAD_TOP_ATTRIBUTE = "GROUPPADTOP";
	public static final String PAD_BOTTOM_ATTRIBUTE = "GROUPPADBOTTOM";
	public static final String PAD_LEFT_ATTRIBUTE = "GROUPPADLEFT";
	public static final String PAD_RIGHT_ATTRIBUTE = "GROUPPADRIGHT";
	public static final String FILL_ATTRIBUTE = "GROUPFILL";
	public static final String SPACING_ATTRIBUTE = "GROUPSPACE";
	public static final String REVERSE_ATTRIBUTE = "REVERSE";

	@Override
	protected VerticalGroup parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final VerticalGroup group = new VerticalGroup();
		setFillParent(group, lmlTagData, parser);
		if (lmlTagData.containsAttribute(ALIGNMENT_ATTRIBUTE)) {
			group.align(parseAlignment(lmlTagData, ALIGNMENT_ATTRIBUTE, parser, group));
		}
		if (lmlTagData.containsAttribute(FILL_ATTRIBUTE)) {
			group.fill(parseFloat(lmlTagData, FILL_ATTRIBUTE, parser, group));
		}
		if (lmlTagData.containsAttribute(REVERSE_ATTRIBUTE)) {
			group.reverse(parseBoolean(lmlTagData, REVERSE_ATTRIBUTE, parser, group));
		}
		setPadding(lmlTagData, parser, group);
		return group;
	}

	private void setPadding(final LmlTagData lmlTagData, final LmlParser parser, final VerticalGroup group) {
		if (lmlTagData.containsAttribute(PAD_ATTRIBUTE)) {
			group.pad(parseFloat(lmlTagData, PAD_ATTRIBUTE, parser, group));
		}
		if (lmlTagData.containsAttribute(PAD_TOP_ATTRIBUTE)) {
			group.padTop(parseFloat(lmlTagData, PAD_TOP_ATTRIBUTE, parser, group));
		}
		if (lmlTagData.containsAttribute(PAD_BOTTOM_ATTRIBUTE)) {
			group.padBottom(parseFloat(lmlTagData, PAD_BOTTOM_ATTRIBUTE, parser, group));
		}
		if (lmlTagData.containsAttribute(PAD_LEFT_ATTRIBUTE)) {
			group.padLeft(parseFloat(lmlTagData, PAD_LEFT_ATTRIBUTE, parser, group));
		}
		if (lmlTagData.containsAttribute(PAD_RIGHT_ATTRIBUTE)) {
			group.padRight(parseFloat(lmlTagData, PAD_RIGHT_ATTRIBUTE, parser, group));
		}
		if (lmlTagData.containsAttribute(SPACING_ATTRIBUTE)) {
			group.space(parseFloat(lmlTagData, SPACING_ATTRIBUTE, parser, group));
		}
	}

	@Override
	protected LmlParent<VerticalGroup> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return new VerticalGroupLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent,
				parser);
	}

}
