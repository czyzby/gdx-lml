package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.attribute.TextFieldLmlTagAttributeParser;

public class TextFieldLmlParent extends AbstractLmlParent<TextField> {
	private static final ObjectMap<String, LmlTagAttributeParser> ATTRIBUTE_PARSERS;

	static {
		final ObjectMap<String, LmlTagAttributeParser> parsers = GdxMaps.newObjectMap();
		final Array<TextFieldLmlTagAttributeParser> postConstructParsers =
				GdxArrays.newArray(TextFieldLmlTagAttributeParser.CURSOR_POSITION,
						TextFieldLmlTagAttributeParser.SELECT_ALL,
						TextFieldLmlTagAttributeParser.SELECTION_END,
						TextFieldLmlTagAttributeParser.SELECTION_START);
		for (final LmlTagAttributeParser parser : postConstructParsers) {
			for (final String alias : parser.getAttributeNames()) {
				parsers.put(alias.toUpperCase(), parser);
			}
		}
		ATTRIBUTE_PARSERS = GdxMaps.toImmutable(parsers);
	}
	private final LmlTagData tagData;

	public TextFieldLmlParent(final LmlTagData tagData, final TextField actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
		this.tagData = tagData;
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		throwErrorIfStrict(parser, "TextFields cannot have children.");
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
		actor.pack();
		for (final Entry<String, String> attribute : tagData.getAttributes()) {
			if (ATTRIBUTE_PARSERS.containsKey(attribute.key)) {
				ATTRIBUTE_PARSERS.get(attribute.key).apply(actor, parser, attribute.value, tagData);
			}
		}
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		actor.appendText(parser.parseStringData(data, actor));
	}
}
