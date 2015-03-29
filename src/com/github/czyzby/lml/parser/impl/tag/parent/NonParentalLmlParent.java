package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public class NonParentalLmlParent<Widget extends Actor> extends AbstractLmlParent<Widget> {
	public NonParentalLmlParent(final LmlTagData tagData, final Widget actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		if (child != null) {
			throwErrorIfStrict(parser, "This widget cannot have children: " + getTagName() + ".");
		}
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		if (isDataNotEmpty(data)) {
			throwErrorIfStrict(parser, "This widget cannot have children: " + getTagName() + ".");
		}
	}
}
