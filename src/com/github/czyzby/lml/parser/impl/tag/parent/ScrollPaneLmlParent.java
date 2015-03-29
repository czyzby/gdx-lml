package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public class ScrollPaneLmlParent extends AbstractLmlParent<ScrollPane> {
	public ScrollPaneLmlParent(final LmlTagData tagData, final ScrollPane actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		if (child == null) {
			return;
		}
		if (actor.getWidget() != null) {
			throwErrorIfStrict(parser, "ScrollPane cannot have multiple children.");
		}
		actor.setWidget(child);
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		handleChild(getLabelFromRawDataBetweenTags(data, parser), null, parser);
	}
}
