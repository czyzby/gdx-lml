package com.konfigurats.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;

public class TextButtonLmlParent<TextButtonWidget extends TextButton> extends
		TableLmlParent<TextButtonWidget> {
	private final StringBuilder text;

	public TextButtonLmlParent(final LmlTagData tagData, final TextButtonWidget actor,
			final LmlParent<?> parent, final LmlParser parser) {
		super(tagData, actor, parent, parser);
		text = new StringBuilder(actor.getText());
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		text.append(parser.parseStringData(data, actor));
	};

	@Override
	public void doOnTagClose(final LmlParser parser) {
		super.doOnTagClose(parser);
		actor.setText(text.toString());
	}
}
