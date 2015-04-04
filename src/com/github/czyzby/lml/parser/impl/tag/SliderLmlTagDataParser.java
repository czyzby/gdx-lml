package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public class SliderLmlTagDataParser extends ProgressBarLmlTagDataParser {
	private static final ObjectMap<String, LmlTagAttributeParser> ATTRIBUTE_PARSERS;

	private final ObjectMap<String, LmlTagAttributeParser> attributeParsers =
			new ObjectMap<String, LmlTagAttributeParser>(ATTRIBUTE_PARSERS);

	static {
		ATTRIBUTE_PARSERS = GdxMaps.newObjectMap();
	}

	public static void registerParser(final LmlTagAttributeParser parser) {
		for (final String alias : parser.getAttributeNames()) {
			ATTRIBUTE_PARSERS.put(alias.toUpperCase(), parser);
		}
	}

	public static void unregisterParser(final String withAlias) {
		ATTRIBUTE_PARSERS.remove(withAlias);
	}

	@Override
	protected void parseAttributes(final LmlTagData lmlTagData, final LmlParser parser, final Actor actor) {
		super.parseAttributes(lmlTagData, parser, actor);
		if (GdxMaps.isNotEmpty(attributeParsers)) {
			for (final Entry<String, String> attribute : lmlTagData.getAttributes()) {
				if (attributeParsers.containsKey(attribute.key)) {
					attributeParsers.get(attribute.key).apply(actor, parser, attribute.value, lmlTagData);
				}
			}
		}
	}

	@Override
	public void registerAttributeParser(final LmlTagAttributeParser parser) {
		for (final String alias : parser.getAttributeNames()) {
			attributeParsers.put(alias.toUpperCase(), parser);
		}
	}

	@Override
	public void unregisterAttributeParser(final String attributeName) {
		attributeParsers.remove(attributeName);
	}

	@Override
	protected Slider getNewInstanceWithoutStyle(final LmlParser parser, final float min, final float max,
			final float stepSize, final boolean vertical) {
		return new Slider(min, max, stepSize, vertical, parser.getSkin());
	}

	@Override
	protected Slider getNewInstanceWithStyle(final LmlTagData lmlTagData, final LmlParser parser,
			final float min, final float max, final float stepSize, final boolean vertical) {
		return new Slider(min, max, stepSize, vertical, parser.getSkin(), getStyleName(lmlTagData, parser));
	}
}
