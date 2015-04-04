package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.attribute.ScrollPaneLmlTagAttributeParser;

public class ScrollPaneLmlParent extends AbstractLmlParent<ScrollPane> {
	private final LmlTagData tagData;

	public ScrollPaneLmlParent(final LmlTagData tagData, final ScrollPane actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
		this.tagData = tagData;
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
		if (tagData.containsAttribute(ScrollPaneLmlTagAttributeParser.SCROLL_PERCENT_ATTRIBUTE)) {
			ScrollPaneLmlTagAttributeParser.SCROLL_PERCENT.apply(actor, parser,
					tagData.getAttribute(ScrollPaneLmlTagAttributeParser.SCROLL_PERCENT_ATTRIBUTE), tagData);
		}
		if (tagData.containsAttribute(ScrollPaneLmlTagAttributeParser.SCROLL_PERCENT_X_ATTRIBUTE)) {
			ScrollPaneLmlTagAttributeParser.SCROLL_PERCENT_X
					.apply(actor, parser,
							tagData.getAttribute(ScrollPaneLmlTagAttributeParser.SCROLL_PERCENT_X_ATTRIBUTE),
							tagData);
		}
		if (tagData.containsAttribute(ScrollPaneLmlTagAttributeParser.SCROLL_PERCENT_Y_ATTRIBUTE)) {
			ScrollPaneLmlTagAttributeParser.SCROLL_PERCENT_Y
					.apply(actor, parser,
							tagData.getAttribute(ScrollPaneLmlTagAttributeParser.SCROLL_PERCENT_Y_ATTRIBUTE),
							tagData);
		}
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		handleChild(getLabelFromRawDataBetweenTags(data, parser), null, parser);
	}
}
