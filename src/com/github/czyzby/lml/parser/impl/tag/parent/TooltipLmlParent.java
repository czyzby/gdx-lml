package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.gdx.widget.reflected.Tooltip;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public class TooltipLmlParent extends TableLmlParent {
	private final Tooltip tooltip;

	public TooltipLmlParent(final LmlTagData tagData, final Tooltip tooltip, final LmlParent<?> parent,
			final LmlParser parser) {
		// Passing null as actor to avoid adding tooltips as regular table (and groups) children. They are
		// attached with custom tooltip methods later.
		super(tagData, null, parent, parser);
		this.tooltip = tooltip;
		isOneColumn = LmlAttributes.parseBoolean(tooltip, parser, tagData.getAttribute(ONE_COLUMN_ATTRIBUTE));
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		appendCellToTable(tooltip.getContent(), child, childTagData, parser);
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
		tooltip.getContent().pack();
		attachTooltip();
	}

	private void attachTooltip() {
		LmlParent<?> parent = this.parent;
		while (parent != null) {
			if (parent.getActor() != null) {
				tooltip.attachTo(parent.getActor());
				break;
			}
			parent = parent.getParent();
		}
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		tooltip.getContent().add(parser.parseStringData(data, tooltip));
		if (isOneColumn) {
			tooltip.getContent().row();
		}
	}
}