package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public class DialogLmlParent extends TableLmlParent<Dialog> {
	public static final String TO_BUTTON_TABLE_ATTRIBUTE = "TOBUTTONTABLE";
	public static final String TO_DIALOG_TABLE_ATTRIBUTE = "TODIALOGTABLE";
	public static final String ON_RESULT_ATTRIBUTE = "ONRESULT";

	public DialogLmlParent(final LmlTagData tagData, final Dialog actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		final boolean containsResult = childTagData.containsAttribute(ON_RESULT_ATTRIBUTE);
		if (parseBoolean(childTagData, TO_BUTTON_TABLE_ATTRIBUTE, parser) || containsResult) {
			appendCellToTable(actor.getButtonTable(), child, childTagData, parser);
		} else if (parseBoolean(childTagData, TO_DIALOG_TABLE_ATTRIBUTE, parser)) {
			appendCellToTable(actor, child, childTagData, parser);
		} else {
			appendCellToTable(actor.getContentTable(), child, childTagData, parser);
		}
		if (containsResult) {
			String onResultAttribute = childTagData.getAttribute(ON_RESULT_ATTRIBUTE);
			if (onResultAttribute.charAt(0) == ACTION_OPERATOR) {
				onResultAttribute = onResultAttribute.substring(1);
			}
			actor.setObject(child, parser.findAction(onResultAttribute, actor));
		}
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		actor.text(parser.parseStringData(data, actor));
		actor.getContentTable().row();
	}
}
