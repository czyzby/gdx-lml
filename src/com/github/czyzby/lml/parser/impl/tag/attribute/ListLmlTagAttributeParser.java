package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum ListLmlTagAttributeParser implements LmlTagAttributeParser {
	SELECTED("selected") {
		@Override
		public void apply(final List<?> list, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			list.setUserObject(LmlAttributes.parseInt(list, parser, attributeValue));
		}
	};
	private final String[] aliases;

	private ListLmlTagAttributeParser(final String... aliases) {
		this.aliases = aliases;
	}

	@Override
	public void apply(final Object actor, final LmlParser parser, final String attributeValue,
			final LmlTagData lmlTagData) {
		apply((List<?>) actor, parser, attributeValue, lmlTagData);
	}

	protected abstract void apply(List<?> list, LmlParser parser, String attributeValue, LmlTagData lmlTagData);

	@Override
	public String[] getAttributeNames() {
		return aliases;
	}
}
