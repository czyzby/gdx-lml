package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.parent.ScrollPaneLmlParent;

public class ScrollPaneLmlTagDataParser extends AbstractGroupLmlTagDataParser<ScrollPane> {
	public static final String CLAMP_ATTRIBUTE = "CLAMP";
	public static final String CANCEL_TOUCH_FOCUS_ATTRIBUTE = "CANCELTOUCHFOCUS";
	public static final String FLICK_SCROLL_ATTRIBUTE = "FLICKSCROLL";
	public static final String SMOOTH_SCROLLING_ATTRIBUTE = "SMOOTHSCROLLING";
	public static final String SCROLLBAR_ON_TOP_ATTRIBUTE = "SCROLLBARONTOP";
	public static final String VARIABLE_SIZE_KNOBS_ATTRIBUTE = "VARIABLESIZEKNOBS";
	public static final String FLING_TIME_ATTRIBUTE = "FLINGTIME";
	public static final String FORCE_SCROLL_Y_ATTRIBUTE = "FORCESCROLLY";
	public static final String FORCE_SCROLL_ATTRIBUTE = "FORCESCROLL";
	public static final String FORCE_SCROLL_X_ATTRIBUTE = "FORCESCROLLX";
	public static final String FADE_SCROLL_BARS_ATTRIBUTE = "FADESCROLLBARS";
	public static final String FADE_SCROLL_BARS_DELAY_ATTRIBUTE = "FADESCROLLBARSALPHA";
	public static final String FADE_SCROLL_BARS_ALPHA_ATTRIBUTE = "FADESCROLLBARSDELAY";
	public static final String OVERSCROLL_ATTRIBUTE = "OVERSCROLL";
	public static final String OVERSCROLL_X_ATTRIBUTE = "OVERSCROLLX";
	public static final String OVERSCROLL_Y_ATTRIBUTE = "OVERSCROLLY";
	public static final String OVERSCROLL_DISTANCE_ATTRIBUTE = "OVERSCROLLDISTANCE";
	public static final String OVERSCROLL_SPEED_MIN_ATTRIBUTE = "OVERSCROLLSPEEDMIN";
	public static final String OVERSCROLL_SPEED_MAX_ATTRIBUTE = "OVERSCROLLSPEEDMAX";
	public static final String SCROLLBAR_POSITION_X_ATTRIBUTE = "SCROLLBARPOSITIONX";
	public static final String SCROLLBAR_POSITION_Y_ATTRIBUTE = "SCROLLBARPOSITIONY";
	public static final String SCROLLING_DISABLED_ATTRIBUTE = "SCROLLINGDISABLED";
	public static final String SCROLLING_DISABLED_X_ATTRIBUTE = "SCROLLINGDISABLEDX";
	public static final String SCROLLING_DISABLED_Y_ATTRIBUTE = "SCROLLINGDISABLEDY";
	public static final String VELOCITY_ATTRIBUTE = "VELOCITY";
	public static final String VELOCITY_X_ATTRIBUTE = "VELOCITYX";
	public static final String VELOCITY_Y_ATTRIBUTE = "VELOCITYY";
	public static final String SCROLL_PERCENT_ATTRIBUTE = "SCROLLPOSITION";
	public static final String SCROLL_PERCENT_X_ATTRIBUTE = "SCROLLPOSITIONX";
	public static final String SCROLL_PERCENT_Y_ATTRIBUTE = "SCROLLPOSITIONY";

	@Override
	protected ScrollPane parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final ScrollPane scrollPane =
				new ScrollPane(null, parser.getSkin(), getStyleName(lmlTagData, parser));
		parsePaneSettings(lmlTagData, parser, scrollPane);
		return scrollPane;
	}

	private void parsePaneSettings(final LmlTagData lmlTagData, final LmlParser parser,
			final ScrollPane scrollPane) {
		setBooleanParameters(lmlTagData, parser, scrollPane);
		if (lmlTagData.containsAttribute(FLING_TIME_ATTRIBUTE)) {
			scrollPane.setFlingTime(parseFloat(lmlTagData, FLING_TIME_ATTRIBUTE, parser, scrollPane));
		}
		setOverscrollSetup(lmlTagData, parser, scrollPane);
		setFadeScrollBarsSetup(lmlTagData, parser, scrollPane);
		setVelocity(lmlTagData, parser, scrollPane);
		setScrollPercent(lmlTagData, parser, scrollPane);
	}

	private void setOverscrollSetup(final LmlTagData lmlTagData, final LmlParser parser,
			final ScrollPane scrollPane) {
		if (lmlTagData.containsAllAttributes(OVERSCROLL_DISTANCE_ATTRIBUTE, OVERSCROLL_SPEED_MIN_ATTRIBUTE,
				OVERSCROLL_SPEED_MAX_ATTRIBUTE)) {
			scrollPane.setupOverscroll(
					parseFloat(lmlTagData, OVERSCROLL_DISTANCE_ATTRIBUTE, parser, scrollPane),
					parseFloat(lmlTagData, OVERSCROLL_SPEED_MIN_ATTRIBUTE, parser, scrollPane),
					parseFloat(lmlTagData, OVERSCROLL_SPEED_MAX_ATTRIBUTE, parser, scrollPane));
		} else if (lmlTagData.containsAnyAttribute(OVERSCROLL_DISTANCE_ATTRIBUTE,
				OVERSCROLL_SPEED_MIN_ATTRIBUTE, OVERSCROLL_SPEED_MAX_ATTRIBUTE)) {
			throwErrorIfStrict(parser, "Distance, min speed and max speed have to be set for overscroll.");
		}
	}

	private void setFadeScrollBarsSetup(final LmlTagData lmlTagData, final LmlParser parser,
			final ScrollPane scrollPane) {
		if (lmlTagData.containsAllAttributes(FADE_SCROLL_BARS_ALPHA_ATTRIBUTE,
				FADE_SCROLL_BARS_DELAY_ATTRIBUTE)) {
			scrollPane.setupFadeScrollBars(
					parseFloat(lmlTagData, FADE_SCROLL_BARS_ALPHA_ATTRIBUTE, parser, scrollPane),
					parseFloat(lmlTagData, FADE_SCROLL_BARS_DELAY_ATTRIBUTE, parser, scrollPane));
		} else if (lmlTagData.containsAnyAttribute(FADE_SCROLL_BARS_ALPHA_ATTRIBUTE,
				FADE_SCROLL_BARS_DELAY_ATTRIBUTE)) {
			throwErrorIfStrict(parser, "Both alpha and delay has to be set for fade scroll bars.");
		}
	}

	private void setVelocity(final LmlTagData lmlTagData, final LmlParser parser, final ScrollPane scrollPane) {
		if (lmlTagData.containsAttribute(VELOCITY_ATTRIBUTE)) {
			final float velocity = parseFloat(lmlTagData, VELOCITY_ATTRIBUTE, parser, scrollPane);
			scrollPane.setVelocityX(velocity);
			scrollPane.setVelocityY(velocity);
		}
		if (lmlTagData.containsAttribute(VELOCITY_X_ATTRIBUTE)) {
			scrollPane.setVelocityX(parseFloat(lmlTagData, VELOCITY_X_ATTRIBUTE, parser, scrollPane));
		}
		if (lmlTagData.containsAttribute(VELOCITY_Y_ATTRIBUTE)) {
			scrollPane.setVelocityY(parseFloat(lmlTagData, VELOCITY_Y_ATTRIBUTE, parser, scrollPane));
		}
	}

	private void setScrollPercent(final LmlTagData lmlTagData, final LmlParser parser,
			final ScrollPane scrollPane) {
		if (lmlTagData.containsAttribute(SCROLL_PERCENT_ATTRIBUTE)) {
			final float percent = parseFloat(lmlTagData, SCROLL_PERCENT_ATTRIBUTE, parser, scrollPane);
			scrollPane.setScrollPercentX(percent);
			scrollPane.setScrollPercentY(percent);
		}
		if (lmlTagData.containsAttribute(SCROLL_PERCENT_X_ATTRIBUTE)) {
			scrollPane.setScrollPercentX(parseFloat(lmlTagData, SCROLL_PERCENT_X_ATTRIBUTE, parser,
					scrollPane));
		}
		if (lmlTagData.containsAttribute(SCROLL_PERCENT_Y_ATTRIBUTE)) {
			scrollPane.setScrollPercentY(parseFloat(lmlTagData, SCROLL_PERCENT_Y_ATTRIBUTE, parser,
					scrollPane));
		}
	}

	private void setBooleanParameters(final LmlTagData lmlTagData, final LmlParser parser,
			final ScrollPane scrollPane) {
		if (lmlTagData.containsAttribute(CLAMP_ATTRIBUTE)) {
			scrollPane.setClamp(parseBoolean(lmlTagData, CLAMP_ATTRIBUTE, parser, scrollPane));
		}
		if (lmlTagData.containsAttribute(FADE_SCROLL_BARS_ATTRIBUTE)) {
			scrollPane.setFadeScrollBars(parseBoolean(lmlTagData, FADE_SCROLL_BARS_ATTRIBUTE, parser,
					scrollPane));
		}
		if (lmlTagData.containsAttribute(CANCEL_TOUCH_FOCUS_ATTRIBUTE)) {
			scrollPane.setCancelTouchFocus(parseBoolean(lmlTagData, CANCEL_TOUCH_FOCUS_ATTRIBUTE, parser,
					scrollPane));
		}
		if (lmlTagData.containsAttribute(FLICK_SCROLL_ATTRIBUTE)) {
			scrollPane.setFlickScroll(parseBoolean(lmlTagData, FLICK_SCROLL_ATTRIBUTE, parser, scrollPane));
		}
		if (lmlTagData.containsAttribute(SMOOTH_SCROLLING_ATTRIBUTE)) {
			scrollPane.setSmoothScrolling(parseBoolean(lmlTagData, SMOOTH_SCROLLING_ATTRIBUTE, parser,
					scrollPane));
		}
		if (lmlTagData.containsAttribute(SCROLLBAR_ON_TOP_ATTRIBUTE)) {
			scrollPane.setScrollbarsOnTop(parseBoolean(lmlTagData, SCROLLBAR_ON_TOP_ATTRIBUTE, parser,
					scrollPane));
		}
		if (lmlTagData.containsAttribute(VARIABLE_SIZE_KNOBS_ATTRIBUTE)) {
			scrollPane.setVariableSizeKnobs(parseBoolean(lmlTagData, VARIABLE_SIZE_KNOBS_ATTRIBUTE, parser,
					scrollPane));
		}
		setFillParent(scrollPane, lmlTagData, parser);
		setForceScroll(lmlTagData, parser, scrollPane);
		setOverscroll(lmlTagData, parser, scrollPane);
		setScrollingDisabled(lmlTagData, parser, scrollPane);
		setScrollbarPositions(lmlTagData, parser, scrollPane);
	}

	private void setForceScroll(final LmlTagData lmlTagData, final LmlParser parser,
			final ScrollPane scrollPane) {
		if (lmlTagData.containsAttribute(FORCE_SCROLL_ATTRIBUTE)) {
			final boolean forceScroll = parseBoolean(lmlTagData, FORCE_SCROLL_ATTRIBUTE, parser, scrollPane);
			scrollPane.setForceScroll(forceScroll, forceScroll);
		} else if (lmlTagData.containsAllAttributes(FORCE_SCROLL_X_ATTRIBUTE, FORCE_SCROLL_Y_ATTRIBUTE)) {
			scrollPane.setForceScroll(parseBoolean(lmlTagData, FORCE_SCROLL_X_ATTRIBUTE, parser, scrollPane),
					parseBoolean(lmlTagData, FORCE_SCROLL_Y_ATTRIBUTE, parser, scrollPane));
		} else if (lmlTagData.containsAnyAttribute(FORCE_SCROLL_X_ATTRIBUTE, FORCE_SCROLL_Y_ATTRIBUTE)) {
			throwErrorIfStrict(parser, "Both X and Y has to be set for force scroll.");
		}
	}

	private void setOverscroll(final LmlTagData lmlTagData, final LmlParser parser,
			final ScrollPane scrollPane) {
		if (lmlTagData.containsAttribute(OVERSCROLL_ATTRIBUTE)) {
			final boolean overscroll = parseBoolean(lmlTagData, OVERSCROLL_ATTRIBUTE, parser, scrollPane);
			scrollPane.setOverscroll(overscroll, overscroll);
		} else if (lmlTagData.containsAllAttributes(OVERSCROLL_X_ATTRIBUTE, OVERSCROLL_Y_ATTRIBUTE)) {
			scrollPane.setOverscroll(parseBoolean(lmlTagData, OVERSCROLL_X_ATTRIBUTE, parser, scrollPane),
					parseBoolean(lmlTagData, OVERSCROLL_Y_ATTRIBUTE, parser, scrollPane));
		} else if (lmlTagData.containsAnyAttribute(OVERSCROLL_X_ATTRIBUTE, OVERSCROLL_Y_ATTRIBUTE)) {
			throwErrorIfStrict(parser, "Both X and Y has to be set for overscroll.");
		}
	}

	private void setScrollingDisabled(final LmlTagData lmlTagData, final LmlParser parser,
			final ScrollPane scrollPane) {
		if (lmlTagData.containsAttribute(SCROLLING_DISABLED_ATTRIBUTE)) {
			final boolean disabled =
					parseBoolean(lmlTagData, SCROLLING_DISABLED_ATTRIBUTE, parser, scrollPane);
			scrollPane.setScrollingDisabled(disabled, disabled);
		} else if (lmlTagData.containsAllAttributes(SCROLLING_DISABLED_X_ATTRIBUTE,
				SCROLLING_DISABLED_Y_ATTRIBUTE)) {
			scrollPane.setScrollingDisabled(
					parseBoolean(lmlTagData, SCROLLING_DISABLED_X_ATTRIBUTE, parser, scrollPane),
					parseBoolean(lmlTagData, SCROLLING_DISABLED_Y_ATTRIBUTE, parser, scrollPane));
		} else if (lmlTagData.containsAnyAttribute(SCROLLING_DISABLED_X_ATTRIBUTE,
				SCROLLING_DISABLED_Y_ATTRIBUTE)) {
			throwErrorIfStrict(parser, "Both X and Y has to be set for scrolling disabled.");
		}
	}

	private void setScrollbarPositions(final LmlTagData lmlTagData, final LmlParser parser,
			final ScrollPane scrollPane) {
		if (lmlTagData.containsAllAttributes(SCROLLBAR_POSITION_X_ATTRIBUTE, SCROLLBAR_POSITION_Y_ATTRIBUTE)) {
			scrollPane.setScrollBarPositions(
					parseBoolean(lmlTagData, SCROLLBAR_POSITION_X_ATTRIBUTE, parser, scrollPane),
					parseBoolean(lmlTagData, SCROLLBAR_POSITION_Y_ATTRIBUTE, parser, scrollPane));
		} else if (lmlTagData.containsAnyAttribute(SCROLLBAR_POSITION_X_ATTRIBUTE,
				SCROLLBAR_POSITION_Y_ATTRIBUTE)) {
			throwErrorIfStrict(parser, "Both X and Y has to be set for scrollbar positions.");
		}
	}

	@Override
	protected LmlParent<ScrollPane> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return new ScrollPaneLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent, parser);
	}
}
