package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public abstract class AbstractGroupLmlTagDataParser<Widget extends WidgetGroup> extends
		AbstractLmlTagDataParser<Widget> {
	protected void setFillParent(final Widget widget, final LmlTagData lmlTagData, final LmlParser parser) {
		if (lmlTagData.containsAttribute(FILL_PARENT_ATTRIBUTE)) {
			widget.setFillParent(parseBoolean(lmlTagData, FILL_PARENT_ATTRIBUTE, parser, widget));
		}
	}
}
