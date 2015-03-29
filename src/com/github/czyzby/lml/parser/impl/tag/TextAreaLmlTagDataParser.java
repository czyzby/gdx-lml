package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.parent.TextAreaLmlParent;

public class TextAreaLmlTagDataParser extends TextFieldLmlTagDataParser {
	@Override
	protected TextArea getNewInstance(final LmlTagData lmlTagData, final LmlParser parser) {
		return new TextArea(getInitialText(lmlTagData, parser), parser.getSkin(), getStyleName(lmlTagData,
				parser));
	}

	@Override
	protected LmlParent<TextField> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return new TextAreaLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent, parser);
	}
}
