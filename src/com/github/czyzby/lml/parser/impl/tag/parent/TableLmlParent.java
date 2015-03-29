package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.RowActor;

public class TableLmlParent<TableWidget extends Table> extends AbstractLmlParent<TableWidget> {
	public static final String ALIGN_ATTRIBUTE = "ALIGN";
	public static final String COLSPAN_ATTRIBUTE = "COLSPAN";
	public static final String EXPAND_ATTRIBUTE = "EXPAND";
	public static final String EXPAND_X_ATTRIBUTE = "EXPANDX";
	public static final String EXPAND_Y_ATTRIBUTE = "EXPANDY";
	public static final String FILL_ATTRIBUTE = "FILL";
	public static final String FILL_X_ATTRIBUTE = "FILLX";
	public static final String FILL_Y_ATTRIBUTE = "FILLY";
	public static final String PAD_ATTRIBUTE = "PAD";
	public static final String PAD_TOP_ATTRIBUTE = "PADTOP";
	public static final String PAD_BOTTOM_ATTRIBUTE = "PADBOTTOM";
	public static final String PAD_LEFT_ATTRIBUTE = "PADLEFT";
	public static final String PAD_RIGHT_ATTRIBUTE = "PADRIGHT";
	public static final String SPACE_ATTRIBUTE = "SPACE";
	public static final String SPACE_TOP_ATTRIBUTE = "SPACETOP";
	public static final String SPACE_BOTTOM_ATTRIBUTE = "SPACEBOTTOM";
	public static final String SPACE_LEFT_ATTRIBUTE = "SPACELEFT";
	public static final String SPACE_RIGHT_ATTRIBUTE = "SPACERIGHT";
	public static final String UNIFORM_ATTRIBUTE = "UNIFORM";
	public static final String UNIFORM_X_ATTRIBUTE = "UNIFORMX";
	public static final String UNIFORM_Y_ATTRIBUTE = "UNIFORMY";
	public static final String HEIGHT_ATTRIBUTE = "HEIGHT";
	public static final String MIN_HEIGHT_ATTRIBUTE = "MINHEIGHT";
	public static final String PREF_HEIGHT_ATTRIBUTE = "PREFHEIGHT";
	public static final String MAX_HEIGHT_ATTRIBUTE = "MAXHEIGHT";
	public static final String WIDTH_ATTRIBUTE = "WIDTH";
	public static final String MIN_WIDTH_ATTRIBUTE = "MINWIDTH";
	public static final String PREF_WIDTH_ATTRIBUTE = "PREFWIDTH";
	public static final String MAX_WIDTH_ATTRIBUTE = "MAXWIDTH";
	public static final String ROW_ATTRIBUTE = "ROW";

	public TableLmlParent(final LmlTagData tagData, final TableWidget actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		appendCellFromTable(actor, child, childTagData, parser);
	}

	protected void appendCellFromTable(final Table table, final Actor child, final LmlTagData childTagData,
			final LmlParser parser) {
		Cell<?> cell;
		if (child == RowActor.ROW) {
			cell = table.row();
		} else {
			cell = table.add(child);
		}
		setCellAttributes(childTagData, cell, parser);
	}

	protected void setCellAttributes(final LmlTagData lmlTagData, final Cell<?> cell, final LmlParser parser) {
		if (lmlTagData.containsAttribute(ALIGN_ATTRIBUTE)) {
			cell.align(parseAlignment(lmlTagData, ALIGN_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(COLSPAN_ATTRIBUTE)) {
			cell.colspan(parseInt(lmlTagData, COLSPAN_ATTRIBUTE, parser));
		}
		setExpand(lmlTagData, cell, parser);
		setFill(lmlTagData, cell, parser);
		setUniform(lmlTagData, cell, parser);
		setPadding(lmlTagData, cell, parser);
		setSpacing(lmlTagData, cell, parser);
		setHeight(lmlTagData, cell, parser);
		setWidth(lmlTagData, cell, parser);

		addRow(lmlTagData, cell, parser);
	}

	private void addRow(final LmlTagData lmlTagData, final Cell<?> cell, final LmlParser parser) {
		if (lmlTagData.containsAttribute(ROW_ATTRIBUTE) && parseBoolean(lmlTagData, ROW_ATTRIBUTE, parser)) {
			cell.row();
		}
	}

	private void setHeight(final LmlTagData lmlTagData, final Cell<?> cell, final LmlParser parser) {
		if (lmlTagData.containsAttribute(HEIGHT_ATTRIBUTE)) {
			cell.height(parseFloat(lmlTagData, HEIGHT_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(MIN_HEIGHT_ATTRIBUTE)) {
			cell.minHeight(parseFloat(lmlTagData, MIN_HEIGHT_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(PREF_HEIGHT_ATTRIBUTE)) {
			cell.prefHeight(parseFloat(lmlTagData, PREF_HEIGHT_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(MAX_HEIGHT_ATTRIBUTE)) {
			cell.maxHeight(parseFloat(lmlTagData, MAX_HEIGHT_ATTRIBUTE, parser));
		}
	}

	private void setWidth(final LmlTagData lmlTagData, final Cell<?> cell, final LmlParser parser) {
		if (lmlTagData.containsAttribute(WIDTH_ATTRIBUTE)) {
			cell.width(parseFloat(lmlTagData, WIDTH_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(MIN_WIDTH_ATTRIBUTE)) {
			cell.minWidth(parseFloat(lmlTagData, MIN_WIDTH_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(PREF_WIDTH_ATTRIBUTE)) {
			cell.prefWidth(parseFloat(lmlTagData, PREF_WIDTH_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(MAX_WIDTH_ATTRIBUTE)) {
			cell.maxWidth(parseFloat(lmlTagData, MAX_WIDTH_ATTRIBUTE, parser));
		}
	}

	private void setExpand(final LmlTagData lmlTagData, final Cell<?> cell, final LmlParser parser) {
		if (lmlTagData.containsAttribute(EXPAND_ATTRIBUTE)) {
			final boolean expand = parseBoolean(lmlTagData, EXPAND_ATTRIBUTE, parser);
			cell.expand(expand, expand);
		}
		if (lmlTagData.containsAttribute(EXPAND_X_ATTRIBUTE)) {
			cell.expand(parseBoolean(lmlTagData, EXPAND_X_ATTRIBUTE, parser),
					Integer.valueOf(1).equals(cell.getExpandY()));
		}
		if (lmlTagData.containsAttribute(EXPAND_Y_ATTRIBUTE)) {
			cell.expand(Integer.valueOf(1).equals(cell.getExpandX()),
					parseBoolean(lmlTagData, EXPAND_Y_ATTRIBUTE, parser));
		}
	}

	private void setFill(final LmlTagData lmlTagData, final Cell<?> cell, final LmlParser parser) {
		if (lmlTagData.containsAttribute(FILL_ATTRIBUTE)) {
			final boolean fill = parseBoolean(lmlTagData, FILL_ATTRIBUTE, parser);
			cell.fill(fill, fill);
		}
		if (lmlTagData.containsAttribute(FILL_X_ATTRIBUTE)) {
			cell.fill(parseBoolean(lmlTagData, FILL_X_ATTRIBUTE, parser),
					Integer.valueOf(1).equals(cell.getFillY()));
		}
		if (lmlTagData.containsAttribute(FILL_Y_ATTRIBUTE)) {
			cell.fill(Integer.valueOf(1).equals(cell.getFillX()),
					parseBoolean(lmlTagData, FILL_Y_ATTRIBUTE, parser));
		}
	}

	private void setUniform(final LmlTagData lmlTagData, final Cell<?> cell, final LmlParser parser) {
		if (lmlTagData.containsAttribute(UNIFORM_ATTRIBUTE)) {
			final boolean uniform = parseBoolean(lmlTagData, UNIFORM_ATTRIBUTE, parser);
			cell.uniform(uniform, uniform);
		}
		if (lmlTagData.containsAttribute(UNIFORM_X_ATTRIBUTE)) {
			boolean uniformY = false;
			try {
				uniformY = cell.getUniformY();
			} catch (final Exception exception) {
				// Ugly, ugly API. Table throws NullPointerException if uniform is unset - values are stored
				// in wrapped Booleans.
			}
			cell.uniform(parseBoolean(lmlTagData, UNIFORM_X_ATTRIBUTE, parser), uniformY);
		}
		if (lmlTagData.containsAttribute(UNIFORM_Y_ATTRIBUTE)) {
			boolean uniformX = false;
			try {
				uniformX = cell.getUniformX();
			} catch (final Exception exception) {
				// Ugly, ugly API. Table throws NullPointerException if uniform is unset - values are stored
				// in wrapped Booleans.
			}
			cell.uniform(uniformX, parseBoolean(lmlTagData, UNIFORM_Y_ATTRIBUTE, parser));
		}
	}

	private void setPadding(final LmlTagData lmlTagData, final Cell<?> cell, final LmlParser parser) {
		if (lmlTagData.containsAttribute(PAD_ATTRIBUTE)) {
			cell.pad(parseFloat(lmlTagData, PAD_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(PAD_TOP_ATTRIBUTE)) {
			cell.padTop(parseFloat(lmlTagData, PAD_TOP_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(PAD_BOTTOM_ATTRIBUTE)) {
			cell.padBottom(parseFloat(lmlTagData, PAD_BOTTOM_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(PAD_LEFT_ATTRIBUTE)) {
			cell.padLeft(parseFloat(lmlTagData, PAD_LEFT_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(PAD_RIGHT_ATTRIBUTE)) {
			cell.padRight(parseFloat(lmlTagData, PAD_RIGHT_ATTRIBUTE, parser));
		}
	}

	private void setSpacing(final LmlTagData lmlTagData, final Cell<?> cell, final LmlParser parser) {
		if (lmlTagData.containsAttribute(SPACE_ATTRIBUTE)) {
			cell.space(parseFloat(lmlTagData, SPACE_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(SPACE_TOP_ATTRIBUTE)) {
			cell.spaceTop(parseFloat(lmlTagData, SPACE_TOP_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(SPACE_BOTTOM_ATTRIBUTE)) {
			cell.spaceBottom(parseFloat(lmlTagData, SPACE_BOTTOM_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(SPACE_LEFT_ATTRIBUTE)) {
			cell.spaceLeft(parseFloat(lmlTagData, SPACE_LEFT_ATTRIBUTE, parser));
		}
		if (lmlTagData.containsAttribute(SPACE_RIGHT_ATTRIBUTE)) {
			cell.spaceRight(parseFloat(lmlTagData, SPACE_RIGHT_ATTRIBUTE, parser));
		}
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
		actor.pack();
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		actor.add(parser.parseStringData(data, actor));
	}

}
