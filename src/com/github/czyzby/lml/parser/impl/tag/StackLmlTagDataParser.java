package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.parent.StackLmlParent;

public class StackLmlTagDataParser extends AbstractGroupLmlTagDataParser<Stack> {
	@Override
	protected Stack parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final Stack stack = new Stack();
		setFillParent(stack, lmlTagData, parser);
		return stack;
	}

	@Override
	protected LmlParent<Stack> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
			final LmlParent<?> parent) {
		return new StackLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent, parser);
	}
}
