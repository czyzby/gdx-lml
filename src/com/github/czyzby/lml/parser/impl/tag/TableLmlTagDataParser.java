package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.attribute.CellLmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.tag.attribute.TableLmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.tag.parent.TableLmlParent;

public class TableLmlTagDataParser extends AbstractLmlTagDataParser<Table> {
	private static final String DEFAULT_CELL_ATTRIBUTE_PREFIX = "DEFAULT";
	private static final ObjectMap<String, LmlTagAttributeParser> ATTRIBUTE_PARSERS;
	private static final ObjectMap<String, LmlTagAttributeParser> CELL_ATTRIBUTE_PARSERS;

	private final ObjectMap<String, LmlTagAttributeParser> attributeParsers =
			new ObjectMap<String, LmlTagAttributeParser>(ATTRIBUTE_PARSERS);
	private final ObjectMap<String, LmlTagAttributeParser> cellAttributeParsers =
			new ObjectMap<String, LmlTagAttributeParser>(CELL_ATTRIBUTE_PARSERS);

	static {
		ATTRIBUTE_PARSERS = GdxMaps.newObjectMap();
		CELL_ATTRIBUTE_PARSERS = GdxMaps.newObjectMap();
		for (final LmlTagAttributeParser parser : TableLmlTagAttributeParser.values()) {
			registerParser(parser);
		}
		for (final LmlTagAttributeParser parser : CellLmlTagAttributeParser.values()) {
			for (final String alias : parser.getAttributeNames()) {
				CELL_ATTRIBUTE_PARSERS.put(toCellDefaultAlias(alias), parser);
			}
		}
	}

	private static final String toCellDefaultAlias(final String alias) {
		return DEFAULT_CELL_ATTRIBUTE_PREFIX + alias.toUpperCase();
	}

	public static void registerParser(final LmlTagAttributeParser parser) {
		for (final String alias : parser.getAttributeNames()) {
			ATTRIBUTE_PARSERS.put(alias.toUpperCase(), parser);
		}
	}

	public static void unregisterParser(final String withAlias) {
		ATTRIBUTE_PARSERS.remove(withAlias);
	}

	public static void registerCellDefaultsParser(final LmlTagAttributeParser parser) {
		for (final String alias : parser.getAttributeNames()) {
			CELL_ATTRIBUTE_PARSERS.put(alias.toUpperCase(), parser);
		}
	}

	public static void unregisterCellDefaultsParser(final String withAlias) {
		CELL_ATTRIBUTE_PARSERS.remove(withAlias);
	}

	@Override
	protected void parseAttributes(final LmlTagData lmlTagData, final LmlParser parser, final Actor actor) {
		super.parseAttributes(lmlTagData, parser, actor);
		for (final Entry<String, String> attribute : lmlTagData.getAttributes()) {
			if (attributeParsers.containsKey(attribute.key)) {
				attributeParsers.get(attribute.key).apply(actor, parser, attribute.value, lmlTagData);
			}
			if (cellAttributeParsers.containsKey(attribute.key)) {
				cellAttributeParsers.get(attribute.key).apply(((Table) actor).defaults(), parser,
						attribute.value, lmlTagData);
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
	protected Table parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		return new Table(parser.getSkin());
	}

	@Override
	protected LmlParent<Table> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
			final LmlParent<?> parent) {
		return new TableLmlParent(lmlTagData, parseChild(lmlTagData, parser), parent, parser);
	}
}
