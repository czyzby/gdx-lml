package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.parent.SimpleActorLmlParent;

/** Appends an empty actor (as a child) or an simple actor group (as a parent).
 *
 * @author MJ */
public class EmptyLmlTagDataParser extends AbstractLmlTagDataParser<Actor> {
	@Override
	protected Actor parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		return new Actor();
	}

	@Override
	protected LmlParent<Actor> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
			final LmlParent<?> parent) {
		return new SimpleActorLmlParent(lmlTagData, parent, parser);
	}

}
