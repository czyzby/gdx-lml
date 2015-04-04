package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.attribute.TextButtonLmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.tag.parent.TextButtonLmlParent;

/** Additionally to button and table tags, text button supports: <ul> <li>text - button's label text.</li>
 * <li>textAlign - button's label alignment.</li> <li>textWrap - button's label wrapping setting. Use with
 * caution. true/false.</li> <li>textEllipsis - button's label ellipsis setting. true/false.</li> </ul>
 *
 * @author MJ */
public class TextButtonLmlTagDataParser extends ButtonLmlTagDataParser {
	private static final ObjectMap<String, LmlTagAttributeParser> ATTRIBUTE_PARSERS;

	private final ObjectMap<String, LmlTagAttributeParser> attributeParsers =
			new ObjectMap<String, LmlTagAttributeParser>(ATTRIBUTE_PARSERS);

	static {
		ATTRIBUTE_PARSERS = GdxMaps.newObjectMap();
		for (final LmlTagAttributeParser parser : TextButtonLmlTagAttributeParser.values()) {
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
	protected TextButton parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		return new TextButton(Strings.EMPTY_STRING, parser.getSkin(), getStyleName(lmlTagData, parser));
	}

	@Override
	protected LmlParent<Table> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
			final LmlParent<?> parent) {
		return new TextButtonLmlParent(lmlTagData, (TextButton) parseChild(lmlTagData, parser), parent,
				parser);
	}
}
