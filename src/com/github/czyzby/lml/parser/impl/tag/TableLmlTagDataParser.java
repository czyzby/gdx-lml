package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.parent.TableLmlParent;

public class TableLmlTagDataParser<TableWidget extends Table> extends
		AbstractGroupLmlTagDataParser<TableWidget> {
	public static final String TABLE_ALIGN_ATTRIBUTE = "TABLEALIGN";
	public static final String TABLE_PAD_ATTRIBUTE = "TABLEPAD";
	public static final String TABLE_PAD_TOP_ATTRIBUTE = "TABLEPADTOP";
	public static final String TABLE_PAD_BOTTOM_ATTRIBUTE = "TABLEPADBOTTOM";
	public static final String TABLE_PAD_LEFT_ATTRIBUTE = "TABLEPADLEFT";
	public static final String TABLE_PAD_RIGHT_ATTRIBUTE = "TABLEPADRIGHT";
	public static final String ROUND_ATTRIBUTE = "ROUND";
	public static final String DEBUG_RECURSIVELY_ATTRIBUTE = "DEBUGRECURSIVELY";

	public static final String DEFAULT_ALIGN_ATTRIBUTE = "DEFAULTALIGN";
	public static final String DEFAULT_COLSPAN_ATTRIBUTE = "DEFAULTCOLSPAN";
	public static final String DEFAULT_EXPAND_ATTRIBUTE = "DEFAULTEXPAND";
	public static final String DEFAULT_EXPAND_X_ATTRIBUTE = "DEFAULTEXPANDX";
	public static final String DEFAULT_EXPAND_Y_ATTRIBUTE = "DEFAULTEXPANDY";
	public static final String DEFAULT_FILL_ATTRIBUTE = "DEFAULTFILL";
	public static final String DEFAULT_FILL_X_ATTRIBUTE = "DEFAULTFILLX";
	public static final String DEFAULT_FILL_Y_ATTRIBUTE = "DEFAULTFILLY";
	public static final String DEFAULT_PAD_ATTRIBUTE = "DEFAULTPAD";
	public static final String DEFAULT_PAD_TOP_ATTRIBUTE = "DEFAULTPADTOP";
	public static final String DEFAULT_PAD_BOTTOM_ATTRIBUTE = "DEFAULTPADBOTTOM";
	public static final String DEFAULT_PAD_LEFT_ATTRIBUTE = "DEFAULTPADLEFT";
	public static final String DEFAULT_PAD_RIGHT_ATTRIBUTE = "DEFAULTPADRIGHT";
	public static final String DEFAULT_SPACE_ATTRIBUTE = "DEFAULTSPACE";
	public static final String DEFAULT_SPACE_TOP_ATTRIBUTE = "DEFAULTSPACETOP";
	public static final String DEFAULT_SPACE_BOTTOM_ATTRIBUTE = "DEFAULTSPACEBOTTOM";
	public static final String DEFAULT_SPACE_LEFT_ATTRIBUTE = "DEFAULTSPACELEFT";
	public static final String DEFAULT_SPACE_RIGHT_ATTRIBUTE = "DEFAULTSPACERIGHT";
	public static final String DEFAULT_UNIFORM_ATTRIBUTE = "DEFAULTUNIFORM";
	public static final String DEFAULT_UNIFORM_X_ATTRIBUTE = "DEFAULTUNIFORMX";
	public static final String DEFAULT_UNIFORM_Y_ATTRIBUTE = "DEFAULTUNIFORMY";
	public static final String DEFAULT_HEIGHT_ATTRIBUTE = "DEFAULTHEIGHT";
	public static final String DEFAULT_MIN_HEIGHT_ATTRIBUTE = "DEFAULTMINHEIGHT";
	public static final String DEFAULT_PREF_HEIGHT_ATTRIBUTE = "DEFAULTPREFHEIGHT";
	public static final String DEFAULT_MAX_HEIGHT_ATTRIBUTE = "DEFAULTMAXHEIGHT";
	public static final String DEFAULT_WIDTH_ATTRIBUTE = "DEFAULTWIDTH";
	public static final String DEFAULT_MIN_WIDTH_ATTRIBUTE = "DEFAULTMINWIDTH";
	public static final String DEFAULT_PREF_WIDTH_ATTRIBUTE = "DEFAULTPREFWIDTH";
	public static final String DEFAULT_MAX_WIDTH_ATTRIBUTE = "DEFAULTMAXWIDTH";

	@Override
	protected TableWidget parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final TableWidget table = prepareNewTable(lmlTagData, parser);
		if (lmlTagData.containsAttribute(TABLE_ALIGN_ATTRIBUTE)) {
			table.align(parseAlignment(lmlTagData, TABLE_ALIGN_ATTRIBUTE, parser, table));
		}
		setFillParent(table, lmlTagData, parser);
		if (lmlTagData.containsAttribute(ROUND_ATTRIBUTE)) {
			table.setRound(parseBoolean(lmlTagData, ROUND_ATTRIBUTE, parser, table));
		}
		setPadding(lmlTagData, parser, table);
		setCellDefaults(lmlTagData, parser, table);
		return table;
	}

	@Override
	protected void setDebug(final LmlTagData lmlTagData, final TableWidget table, final LmlParser parser) {
		super.setDebug(lmlTagData, table, parser);
		if (lmlTagData.containsAttribute(DEBUG_RECURSIVELY_ATTRIBUTE)) {
			table.setDebug(parseBoolean(lmlTagData, DEBUG_RECURSIVELY_ATTRIBUTE, parser, table), true);
		}
	}

	@SuppressWarnings("unchecked")
	protected TableWidget prepareNewTable(final LmlTagData lmlTagData, final LmlParser parser) {
		return (TableWidget) new Table(parser.getSkin());
	}

	private void setPadding(final LmlTagData lmlTagData, final LmlParser parser, final Table table) {
		if (lmlTagData.containsAttribute(TABLE_PAD_ATTRIBUTE)) {
			table.pad(parseFloat(lmlTagData, TABLE_PAD_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(TABLE_PAD_TOP_ATTRIBUTE)) {
			table.padTop(parseFloat(lmlTagData, TABLE_PAD_TOP_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(TABLE_PAD_BOTTOM_ATTRIBUTE)) {
			table.padBottom(parseFloat(lmlTagData, TABLE_PAD_BOTTOM_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(TABLE_PAD_LEFT_ATTRIBUTE)) {
			table.padLeft(parseFloat(lmlTagData, TABLE_PAD_LEFT_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(TABLE_PAD_RIGHT_ATTRIBUTE)) {
			table.padRight(parseFloat(lmlTagData, TABLE_PAD_RIGHT_ATTRIBUTE, parser, table));
		}
	}

	private void setCellDefaults(final LmlTagData lmlTagData, final LmlParser parser, final Table table) {
		final Cell<?> defaults = table.defaults();
		if (lmlTagData.containsAttribute(DEFAULT_ALIGN_ATTRIBUTE)) {
			defaults.align(parseAlignment(lmlTagData, DEFAULT_ALIGN_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_COLSPAN_ATTRIBUTE)) {
			defaults.colspan(parseInt(lmlTagData, DEFAULT_COLSPAN_ATTRIBUTE, parser, table));
		}
		setDefaultExpand(lmlTagData, parser, defaults, table);
		setDefaultFill(lmlTagData, parser, defaults, table);
		setDefaultUniform(lmlTagData, parser, defaults, table);
		setDefaultPadding(lmlTagData, parser, defaults, table);
		setDefaultSpacing(lmlTagData, parser, defaults, table);
		setDefaultHeight(lmlTagData, parser, defaults, table);
		setDefaultWidth(lmlTagData, parser, defaults, table);
	}

	private void setDefaultHeight(final LmlTagData lmlTagData, final LmlParser parser,
			final Cell<?> defaults, final Table table) {
		if (lmlTagData.containsAttribute(DEFAULT_HEIGHT_ATTRIBUTE)) {
			defaults.height(parseFloat(lmlTagData, DEFAULT_HEIGHT_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_MIN_HEIGHT_ATTRIBUTE)) {
			defaults.minHeight(parseFloat(lmlTagData, DEFAULT_MIN_HEIGHT_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_PREF_HEIGHT_ATTRIBUTE)) {
			defaults.prefHeight(parseFloat(lmlTagData, DEFAULT_PREF_HEIGHT_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_MAX_HEIGHT_ATTRIBUTE)) {
			defaults.maxHeight(parseFloat(lmlTagData, DEFAULT_MAX_HEIGHT_ATTRIBUTE, parser, table));
		}
	}

	private void setDefaultWidth(final LmlTagData lmlTagData, final LmlParser parser, final Cell<?> defaults,
			final Table table) {
		if (lmlTagData.containsAttribute(DEFAULT_WIDTH_ATTRIBUTE)) {
			defaults.width(parseFloat(lmlTagData, DEFAULT_WIDTH_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_MIN_WIDTH_ATTRIBUTE)) {
			defaults.minWidth(parseFloat(lmlTagData, DEFAULT_MIN_WIDTH_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_PREF_WIDTH_ATTRIBUTE)) {
			defaults.prefWidth(parseFloat(lmlTagData, DEFAULT_PREF_WIDTH_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_MAX_WIDTH_ATTRIBUTE)) {
			defaults.maxWidth(parseFloat(lmlTagData, DEFAULT_MAX_WIDTH_ATTRIBUTE, parser, table));
		}
	}

	private void setDefaultExpand(final LmlTagData lmlTagData, final LmlParser parser,
			final Cell<?> defaults, final Table table) {
		if (lmlTagData.containsAttribute(DEFAULT_EXPAND_ATTRIBUTE)) {
			final boolean expand = parseBoolean(lmlTagData, DEFAULT_EXPAND_ATTRIBUTE, parser, table);
			defaults.expand(expand, expand);
		}
		if (parseBoolean(lmlTagData, DEFAULT_EXPAND_X_ATTRIBUTE, parser, table)) {
			defaults.expandX();
		}
		if (parseBoolean(lmlTagData, DEFAULT_EXPAND_Y_ATTRIBUTE, parser, table)) {
			defaults.expandY();
		}
	}

	private void setDefaultFill(final LmlTagData lmlTagData, final LmlParser parser, final Cell<?> defaults,
			final Table table) {
		if (lmlTagData.containsAttribute(DEFAULT_FILL_ATTRIBUTE)) {
			final boolean fill = parseBoolean(lmlTagData, DEFAULT_FILL_ATTRIBUTE, parser, table);
			defaults.fill(fill, fill);
		}
		if (parseBoolean(lmlTagData, DEFAULT_FILL_X_ATTRIBUTE, parser, table)) {
			defaults.fillX();
		}
		if (parseBoolean(lmlTagData, DEFAULT_FILL_Y_ATTRIBUTE, parser, table)) {
			defaults.fillY();
		}
	}

	private void setDefaultUniform(final LmlTagData lmlTagData, final LmlParser parser,
			final Cell<?> defaults, final Table table) {
		if (lmlTagData.containsAttribute(DEFAULT_UNIFORM_ATTRIBUTE)) {
			final boolean uniform = parseBoolean(lmlTagData, DEFAULT_UNIFORM_ATTRIBUTE, parser, table);
			defaults.uniform(uniform, uniform);
		}
		if (parseBoolean(lmlTagData, DEFAULT_UNIFORM_X_ATTRIBUTE, parser, table)) {
			defaults.uniformX();
		}
		if (parseBoolean(lmlTagData, DEFAULT_UNIFORM_Y_ATTRIBUTE, parser, table)) {
			defaults.uniformY();
		}
	}

	private void setDefaultPadding(final LmlTagData lmlTagData, final LmlParser parser,
			final Cell<?> defaults, final Table table) {
		if (lmlTagData.containsAttribute(DEFAULT_PAD_ATTRIBUTE)) {
			defaults.pad(parseFloat(lmlTagData, DEFAULT_PAD_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_PAD_TOP_ATTRIBUTE)) {
			defaults.padTop(parseFloat(lmlTagData, DEFAULT_PAD_TOP_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_PAD_BOTTOM_ATTRIBUTE)) {
			defaults.padBottom(parseFloat(lmlTagData, DEFAULT_PAD_BOTTOM_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_PAD_LEFT_ATTRIBUTE)) {
			defaults.padLeft(parseFloat(lmlTagData, DEFAULT_PAD_LEFT_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_PAD_RIGHT_ATTRIBUTE)) {
			defaults.padRight(parseFloat(lmlTagData, DEFAULT_PAD_RIGHT_ATTRIBUTE, parser, table));
		}
	}

	private void setDefaultSpacing(final LmlTagData lmlTagData, final LmlParser parser,
			final Cell<?> defaults, final Table table) {
		if (lmlTagData.containsAttribute(DEFAULT_SPACE_ATTRIBUTE)) {
			defaults.space(parseFloat(lmlTagData, DEFAULT_SPACE_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_SPACE_TOP_ATTRIBUTE)) {
			defaults.spaceTop(parseFloat(lmlTagData, DEFAULT_SPACE_TOP_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_SPACE_BOTTOM_ATTRIBUTE)) {
			defaults.spaceBottom(parseFloat(lmlTagData, DEFAULT_SPACE_BOTTOM_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_SPACE_LEFT_ATTRIBUTE)) {
			defaults.spaceLeft(parseFloat(lmlTagData, DEFAULT_SPACE_LEFT_ATTRIBUTE, parser, table));
		}
		if (lmlTagData.containsAttribute(DEFAULT_SPACE_RIGHT_ATTRIBUTE)) {
			defaults.spaceRight(parseFloat(lmlTagData, DEFAULT_SPACE_RIGHT_ATTRIBUTE, parser, table));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected LmlParent<TableWidget> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return (LmlParent<TableWidget>) new TableLmlParent<Table>(lmlTagData, parseChildWithValidTag(
				lmlTagData, parser), parent, parser);
	}
}
