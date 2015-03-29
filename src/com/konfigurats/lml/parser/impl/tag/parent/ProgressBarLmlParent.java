package com.konfigurats.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;

public class ProgressBarLmlParent extends NonParentalLmlParent<ProgressBar> {
	public ProgressBarLmlParent(final LmlTagData tagData, final ProgressBar actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
	}
}
