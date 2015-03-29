package com.konfigurats.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;

public class SliderLmlTagDataParser extends ProgressBarLmlTagDataParser {
	@Override
	protected ProgressBar getNewInstanceOfWidget(final LmlTagData lmlTagData, final LmlParser parser) {
		final float min = getMin(lmlTagData, parser);
		final float max = getMax(lmlTagData, parser, min);
		final float stepSize = getStepSize(lmlTagData, parser, min, max);
		if (containsStyleAttribute(lmlTagData)) {
			return new Slider(min, max, stepSize, parseBoolean(lmlTagData, VERTICAL_ATTRIBUTE, parser, null),
					parser.getSkin(), getStyleName(lmlTagData, parser));
		}
		// Default style name is not "default", using no style name arg constructor.
		return new Slider(min, max, stepSize, parseBoolean(lmlTagData, VERTICAL_ATTRIBUTE, parser, null),
				parser.getSkin());
	}
}
