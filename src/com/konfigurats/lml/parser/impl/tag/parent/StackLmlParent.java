package com.konfigurats.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;

public class StackLmlParent extends AbstractLmlParent<Stack> {
	public StackLmlParent(final LmlTagData tagData, final Stack actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		if (child != null) {
			actor.add(child);
		}
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		if (isDataNotEmpty(data)) {
			handleChild(getLabelFromRawDataBetweenTags(data, parser), null, parser);
		}
	}
}
