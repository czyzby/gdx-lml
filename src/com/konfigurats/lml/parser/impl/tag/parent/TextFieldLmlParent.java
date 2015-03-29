package com.konfigurats.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;

public class TextFieldLmlParent extends AbstractLmlParent<TextField> {
	public TextFieldLmlParent(final LmlTagData tagData, final TextField actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		throwErrorIfStrict(parser, "TextFields cannot have children.");
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
		actor.pack();
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		actor.appendText(parser.parseStringData(data, actor));
	}
}
