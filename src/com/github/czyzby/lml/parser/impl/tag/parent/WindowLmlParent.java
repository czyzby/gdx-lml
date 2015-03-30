package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.util.gdx.Alignment;

public class WindowLmlParent<WindowWidget extends Window> extends TableLmlParent<WindowWidget> {
	public static final String TO_TITLE_TABLE_ATTRIBUTE = "TOTITLETABLE";
	private boolean wasTitleTableConverted;

	public WindowLmlParent(final LmlTagData tagData, final WindowWidget actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		if (parseBoolean(childTagData, TO_TITLE_TABLE_ATTRIBUTE, parser)) {
			validateTitleTable();
			appendCellToTable(actor.getButtonTable(), child, childTagData, parser);
		} else {
			appendCellToTable(actor, child, childTagData, parser);
		}
	}

	private void validateTitleTable() {
		if (!wasTitleTableConverted) {
			wasTitleTableConverted = true;
			actor.getButtonTable().setFillParent(true);
			actor.getButtonTable().align(Alignment.TOP.getAlignment());
		}
	}
}
