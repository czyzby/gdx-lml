package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.parent.TextButtonLmlParent;

public class CheckBoxLmlTagDataParser extends TextButtonLmlTagDataParser<CheckBox> {
	@Override
	protected CheckBox prepareNewTable(final LmlTagData lmlTagData, final LmlParser parser) {
		final CheckBox checkBox =
				new CheckBox(EMPTY_STRING, parser.getSkin(), getStyleName(lmlTagData, parser));
		checkBox.setText(parseString(lmlTagData, TEXT_ATTRIBUTE, parser, checkBox));
		return checkBox;
	}

	@Override
	protected LmlParent<CheckBox> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return new TextButtonLmlParent<CheckBox>(lmlTagData, parseChildWithValidTag(lmlTagData, parser),
				parent, parser);
	}
}
