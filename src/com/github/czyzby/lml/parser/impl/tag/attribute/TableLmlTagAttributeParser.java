package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum TableLmlTagAttributeParser implements LmlTagAttributeParser {
	ALIGN("tableAlign") {
		@Override
		protected void apply(final Table table, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			table.align(LmlAttributes.parseAlignment(table, parser, attributeValue));
		}
	},
	FILL_PARENT("fillParent") {
		@Override
		protected void apply(final Table table, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			table.setFillParent(LmlAttributes.parseBoolean(table, parser, attributeValue));
		}
	},
	PAD("tablePad") {
		@Override
		protected void apply(final Table table, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			table.pad(LmlAttributes.parseVerticalValue(table, table, parser, attributeValue));
		}
	},
	PAD_LEFT("tablePadLeft") {
		@Override
		protected void apply(final Table table, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			table.padLeft(LmlAttributes.parseHorizontalValue(table, table, parser, attributeValue));
		}
	},
	PAD_RIGHT("tablePadRight") {
		@Override
		protected void apply(final Table table, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			table.padRight(LmlAttributes.parseHorizontalValue(table, table, parser, attributeValue));
		}
	},
	PAD_TOP("tablePadTop") {
		@Override
		protected void apply(final Table table, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			table.padTop(LmlAttributes.parseVerticalValue(table, table, parser, attributeValue));
		}
	},
	PAD_BOTTOM("tablePadBottom") {
		@Override
		protected void apply(final Table table, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			table.padBottom(LmlAttributes.parseVerticalValue(table, table, parser, attributeValue));
		}
	},
	ROUND("round") {
		@Override
		protected void apply(final Table table, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			table.setRound(LmlAttributes.parseBoolean(table, parser, attributeValue));
		}
	},
	DEBUG_RECURSIVELY("debugRecursively") {
		@Override
		protected void apply(final Table table, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			table.setDebug(LmlAttributes.parseBoolean(table, parser, attributeValue), true);
		}
	};

	private final String[] aliases;

	private TableLmlTagAttributeParser(final String... aliases) {
		this.aliases = aliases;
	}

	@Override
	public void apply(final Object actor, final LmlParser parser, final String attributeValue,
			final LmlTagData lmlTagData) {
		apply((Table) actor, parser, attributeValue, lmlTagData);
	}

	protected abstract void apply(Table table, LmlParser parser, String attributeValue, LmlTagData lmlTagData);

	@Override
	public String[] getAttributeNames() {
		return aliases;
	}
}
