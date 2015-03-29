package com.konfigurats.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;

public class TreeLmlParent extends AbstractLmlParent<Tree> {
	public TreeLmlParent(final LmlTagData tagData, final Tree actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		if (child != null) {
			actor.add(new Tree.Node(child));
		}
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		actor.add(new Tree.Node(getLabelFromRawDataBetweenTags(data, parser)));
	}
}
