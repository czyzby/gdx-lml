package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.attribute.CellLmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;
import com.github.czyzby.lml.parser.impl.util.RowActor;

public class TableLmlParent extends AbstractLmlParent<Table> {
	public static final String ONE_COLUMN_ATTRIBUTE = "ONECOLUMN";
	public static final String ROW_ATTRIBUTE = "ROW";

	private static final ObjectMap<String, LmlTagAttributeParser> ATTRIBUTE_PARSERS;
	static {
		ATTRIBUTE_PARSERS = GdxMaps.newObjectMap();
		for (final LmlTagAttributeParser parser : CellLmlTagAttributeParser.values()) {
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

	private final boolean isOneColumn;

	public TableLmlParent(final LmlTagData tagData, final Table actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
		isOneColumn = LmlAttributes.parseBoolean(actor, parser, tagData.getAttribute(ONE_COLUMN_ATTRIBUTE));
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		appendCellToTable(actor, child, childTagData, parser);
	}

	protected void appendCellToTable(final Table table, final Actor child, final LmlTagData childTagData,
			final LmlParser parser) {
		final Cell<?> cell;
		if (child == RowActor.ROW) {
			cell = table.row();
		} else {
			cell = table.add(child);
			if (isOneColumn) {
				table.row();
			}
		}
		parseCellAttributes(cell, childTagData, parser);
		addRow(childTagData, cell, parser);
	}

	protected void parseCellAttributes(final Cell<?> cell, final LmlTagData lmlTagData, final LmlParser parser) {
		for (final Entry<String, String> attribute : lmlTagData.getAttributes()) {
			if (ATTRIBUTE_PARSERS.containsKey(attribute.key)) {
				ATTRIBUTE_PARSERS.get(attribute.key).apply(cell, parser, attribute.value, lmlTagData);
			}
		}
	}

	private void addRow(final LmlTagData lmlTagData, final Cell<?> cell, final LmlParser parser) {
		if (!isOneColumn
				&& LmlAttributes
						.parseBoolean(cell.getTable(), parser, lmlTagData.getAttribute(ROW_ATTRIBUTE))) {
			cell.row();
		}
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
		actor.pack();
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		actor.add(parser.parseStringData(data, actor));
		if (isOneColumn) {
			actor.row();
		}
	}
}
