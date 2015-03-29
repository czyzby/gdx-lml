package com.konfigurats.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.tag.parent.ListLmlParent;

public class ListLmlTagDataParser extends AbstractLmlTagDataParser<List<String>> {
	public static final String SELECTED_ATTRIBUTE = "SELECTED";

	@Override
	protected List<String> parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final List<String> list = new List<String>(parser.getSkin(), getStyleName(lmlTagData, parser));
		if (lmlTagData.containsAttribute(FILL_PARENT_ATTRIBUTE)) {
			list.setFillParent(parseBoolean(lmlTagData, FILL_PARENT_ATTRIBUTE, parser, list));
		}
		if (lmlTagData.containsAttribute(SELECTED_ATTRIBUTE)) {
			list.setUserObject(parseInt(lmlTagData, SELECTED_ATTRIBUTE, parser, list));
		}
		return list;
	}

	@Override
	protected LmlParent<List<String>> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return new ListLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent, parser);
	}
}
