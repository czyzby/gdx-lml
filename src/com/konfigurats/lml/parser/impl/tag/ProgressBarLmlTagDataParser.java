package com.konfigurats.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.konfigurats.lml.error.LmlParsingException;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.tag.parent.ProgressBarLmlParent;

public class ProgressBarLmlTagDataParser extends AbstractLmlTagDataParser<ProgressBar> {
	private static final float DEFAULT_STEP_SIZE = 100f;
	private static final float DEFAULT_MIN = 0f;
	private static final float DEFAULT_RANGE_SIZE = 1f;

	public static final String MIN_ATTRIBUTE = "MIN";
	public static final String MAX_ATTRIBUTE = "MAX";
	public static final String VALUE_ATTRIBUTE = "VALUE";
	public static final String STEP_SIZE_ATTRIBUTE = "STEPSIZE";
	public static final String FILL_PARENT_ATTRIBUTE = "FILLPARENT";
	public static final String VERTICAL_ATTRIBUTE = "VERTICAL";
	public static final String ANIMATE_DURATION_ATTRIBUTE = "ANIMATEDURATION";

	@Override
	protected ProgressBar parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final ProgressBar progressBar = getNewInstanceOfWidget(lmlTagData, parser);
		if (lmlTagData.containsAttribute(ANIMATE_DURATION_ATTRIBUTE)) {
			progressBar.setAnimateDuration(parseFloat(lmlTagData, ANIMATE_DURATION_ATTRIBUTE, parser,
					progressBar));
		}
		if (lmlTagData.containsAttribute(FILL_PARENT_ATTRIBUTE)) {
			progressBar.setFillParent(parseBoolean(lmlTagData, FILL_PARENT_ATTRIBUTE, parser, progressBar));
		}
		if (lmlTagData.containsAttribute(VALUE_ATTRIBUTE)) {
			progressBar.setValue(parseFloat(lmlTagData, VALUE_ATTRIBUTE, parser, progressBar));
		}
		return progressBar;
	}

	protected ProgressBar getNewInstanceOfWidget(final LmlTagData lmlTagData, final LmlParser parser) {
		final float min = getMin(lmlTagData, parser);
		final float max = getMax(lmlTagData, parser, min);
		final float stepSize = getStepSize(lmlTagData, parser, min, max);
		if (containsStyleAttribute(lmlTagData)) {
			return new ProgressBar(min, max, stepSize, parseBoolean(lmlTagData, VERTICAL_ATTRIBUTE, parser,
					null), parser.getSkin(), getStyleName(lmlTagData, parser));
		}
		// Default style name is not "default", using no style name arg constructor.
		return new ProgressBar(min, max, stepSize,
				parseBoolean(lmlTagData, VERTICAL_ATTRIBUTE, parser, null), parser.getSkin());
	}

	protected float getMin(final LmlTagData lmlTagData, final LmlParser parser) {
		return lmlTagData.containsAttribute(MIN_ATTRIBUTE) ? parseFloat(lmlTagData, MIN_ATTRIBUTE, parser,
				null) : DEFAULT_MIN;
	}

	protected float getMax(final LmlTagData lmlTagData, final LmlParser parser, final float min) {
		return lmlTagData.containsAttribute(MAX_ATTRIBUTE) ? parseFloat(lmlTagData, MAX_ATTRIBUTE, parser,
				null) : min + DEFAULT_RANGE_SIZE;
	}

	protected float getStepSize(final LmlTagData lmlTagData, final LmlParser parser, final float min,
			final float max) {
		final float stepSize =
				lmlTagData.containsAttribute(STEP_SIZE_ATTRIBUTE) ? parseFloat(lmlTagData,
						STEP_SIZE_ATTRIBUTE, parser, null) : (max - min) / DEFAULT_STEP_SIZE;
		if (stepSize > max - min) {
			throw new LmlParsingException("Progress bar step size cannot be greater than range size.", parser);
		}
		return stepSize;
	}

	@Override
	protected LmlParent<ProgressBar> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return new ProgressBarLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent,
				parser);
	}

}
