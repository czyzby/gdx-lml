package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.attribute.SplitPaneLmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.tag.parent.SplitPaneLmlParent;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public class SplitPaneLmlTagDataParser extends AbstractLmlTagDataParser<SplitPane> {
	/** Has to be parsed manually, in the SplitPane constructor - affects style. */
	private static final String VERTICAL_ATTRIBUTE = "VERTICAL";
	private static final ObjectMap<String, LmlTagAttributeParser> ATTRIBUTE_PARSERS;

	private final ObjectMap<String, LmlTagAttributeParser> attributeParsers =
			new ObjectMap<String, LmlTagAttributeParser>(ATTRIBUTE_PARSERS);

	static {
		ATTRIBUTE_PARSERS = GdxMaps.newObjectMap();
		for (final LmlTagAttributeParser parser : SplitPaneLmlTagAttributeParser.values()) {
			registerParser(parser);
		}
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
		for (final Entry<String, String> attribute : lmlTagData.getAttributes()) {
			if (attributeParsers.containsKey(attribute.key)) {
				attributeParsers.get(attribute.key).apply(actor, parser, attribute.value, lmlTagData);
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
	protected SplitPane parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		if (containsStyleAttribute(lmlTagData)) {
			return new SplitPane(null, null, LmlAttributes.parseBoolean(null, parser,
					lmlTagData.getAttribute(VERTICAL_ATTRIBUTE)), parser.getSkin(), getStyleName(lmlTagData,
					parser));
		}
		// Default style name is not "default", using no-style-name constructor.
		return new SplitPane(null, null, LmlAttributes.parseBoolean(null, parser,
				lmlTagData.getAttribute(VERTICAL_ATTRIBUTE)), parser.getSkin());
	}

	@Override
	protected LmlParent<SplitPane> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return new SplitPaneLmlParent(lmlTagData, parseChild(lmlTagData, parser), parent, parser);
	}
}
