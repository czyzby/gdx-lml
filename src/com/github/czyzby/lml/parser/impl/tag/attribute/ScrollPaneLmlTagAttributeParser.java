package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum ScrollPaneLmlTagAttributeParser implements LmlTagAttributeParser {
    CLAMP("clamp") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setClamp(LmlAttributes.parseBoolean(scrollPane, parser, attributeValue));
        }
    },
    CANCEL_TOUCH_FOCUS("cancelTouchFocus") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setCancelTouchFocus(LmlAttributes.parseBoolean(scrollPane, parser, attributeValue));
        }
    },
    FLICK_SCROLL("flickScroll") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setFlickScroll(LmlAttributes.parseBoolean(scrollPane, parser, attributeValue));
        }
    },
    FLICK_SCROLL_TAP_SQUARE_SIZE("flickScrollTapSquareSize", "tapSquareSize") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setFlickScrollTapSquareSize(LmlAttributes.parseFloat(scrollPane, parser, attributeValue));
        }
    },
    SMOOTH_SCROLLING("smoothScrolling") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setSmoothScrolling(LmlAttributes.parseBoolean(scrollPane, parser, attributeValue));
        }
    },
    SCROLLBARS_ON_TOP("scrollbarsOnTop") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setScrollbarsOnTop(LmlAttributes.parseBoolean(scrollPane, parser, attributeValue));
        }
    },
    VARIABLE_SIZE_KNOBS("variableSizeKnobs") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setVariableSizeKnobs(LmlAttributes.parseBoolean(scrollPane, parser, attributeValue));
        }
    },
    FLING_TIME("flingTime") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setFlingTime(LmlAttributes.parseFloat(scrollPane, parser, attributeValue));
        }
    },
    FORCE_SCROLL("forceScroll") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            final boolean forceScroll = LmlAttributes.parseBoolean(scrollPane, parser, attributeValue);
            scrollPane.setForceScroll(forceScroll, forceScroll);
        }
    },
    FORCE_SCROLL_Y("forceScrollY") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setForceScroll(scrollPane.isForceScrollX(),
                    LmlAttributes.parseBoolean(scrollPane, parser, attributeValue));
        }
    },
    FORCE_SCROLL_X("forceScrollX") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setForceScroll(LmlAttributes.parseBoolean(scrollPane, parser, attributeValue),
                    scrollPane.isForceScrollY());
        }
    },
    FADE_SCROLL_BARS("fadeScrollBars") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setFadeScrollBars(LmlAttributes.parseBoolean(scrollPane, parser, attributeValue));
        }
    },
    FADE_SCROLL_BARS_DELAY("fadeScrollBarsDelay", "fadeScrollBarsAlphaSeconds") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            final float fadeAlphaSeconds = determineFadeScrollsAlphaSeconds(scrollPane, parser, lmlTagData);
            final float fadeDelaySeconds = determineFadeScrollsDelay(scrollPane, parser, lmlTagData);
            scrollPane.setupFadeScrollBars(fadeAlphaSeconds, fadeDelaySeconds);
        }

        private float determineFadeScrollsAlphaSeconds(final ScrollPane scrollPane, final LmlParser parser,
                final LmlTagData lmlTagData) {
            // No getters...
            if (lmlTagData.containsAttribute(FADE_SCROLL_BARS_ALPHA_ATTRIBUTE)) {
                return LmlAttributes.parseFloat(scrollPane, parser,
                        lmlTagData.getAttribute(FADE_SCROLL_BARS_ALPHA_ATTRIBUTE));
            }
            return DEFAULY_DELAY;
        }

        private float determineFadeScrollsDelay(final ScrollPane scrollPane, final LmlParser parser,
                final LmlTagData lmlTagData) {
            // No getters...
            if (lmlTagData.containsAttribute(FADE_SCROLL_BARS_DELAY_ATTRIBUTE)) {
                return LmlAttributes.parseFloat(scrollPane, parser,
                        lmlTagData.getAttribute(FADE_SCROLL_BARS_DELAY_ATTRIBUTE));
            }
            return DEFAULY_DELAY;
        }
    },
    OVERSCROLL("overscroll", "overscrollX", "overscrollY") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            // No getters...
            final boolean overscrollX = determineOverscrollX(scrollPane, parser, lmlTagData);
            final boolean overscrollY = determineOverscrollY(scrollPane, parser, lmlTagData);
            scrollPane.setOverscroll(overscrollX, overscrollY);
        }

        private boolean determineOverscrollX(final ScrollPane scrollPane, final LmlParser parser,
                final LmlTagData lmlTagData) {
            if (lmlTagData.containsAttribute(OVERSCROLL_X_ATTRIBUTE)) {
                return LmlAttributes.parseBoolean(scrollPane, parser, lmlTagData.getAttribute(OVERSCROLL_X_ATTRIBUTE));
            } else if (lmlTagData.containsAttribute(OVERSCROLL_ATTRIBUTE)) {
                return LmlAttributes.parseBoolean(scrollPane, parser, lmlTagData.getAttribute(OVERSCROLL_ATTRIBUTE));
            }
            return DEFAULT_OVERSCROLL;
        }

        private boolean determineOverscrollY(final ScrollPane scrollPane, final LmlParser parser,
                final LmlTagData lmlTagData) {
            if (lmlTagData.containsAttribute(OVERSCROLL_Y_ATTRIBUTE)) {
                return LmlAttributes.parseBoolean(scrollPane, parser, lmlTagData.getAttribute(OVERSCROLL_Y_ATTRIBUTE));
            } else if (lmlTagData.containsAttribute(OVERSCROLL_ATTRIBUTE)) {
                return LmlAttributes.parseBoolean(scrollPane, parser, lmlTagData.getAttribute(OVERSCROLL_ATTRIBUTE));
            }
            return DEFAULT_OVERSCROLL;
        }

    },
    OVERSCROLL_DISTANCE("overscrollDistance", "overscrollSpeedMin", "overscrollSpeedMax") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            final float distance = getOverscrollDistance(scrollPane, parser, lmlTagData);
            final float speedMin = getSpeedMin(scrollPane, parser, lmlTagData);
            final float speedMax = getSpeedMax(scrollPane, parser, lmlTagData);
            if (speedMin > speedMax) {
                throw new LmlParsingException("Scroll pane min speed cannot be bigger than max.", parser);
            }
            scrollPane.setupOverscroll(distance, speedMin, speedMax);
        }

        private float getOverscrollDistance(final ScrollPane scrollPane, final LmlParser parser,
                final LmlTagData lmlTagData) {
            if (lmlTagData.containsAttribute(OVERSCROLL_DISTANCE_ATTRIBUTE)) {
                return LmlAttributes.parseFloat(scrollPane, parser,
                        lmlTagData.getAttribute(OVERSCROLL_DISTANCE_ATTRIBUTE));
            }
            return DEFAULT_OVERSCROLL_DISTANCE;
        }

        private float getSpeedMin(final ScrollPane scrollPane, final LmlParser parser, final LmlTagData lmlTagData) {
            if (lmlTagData.containsAttribute(OVERSCROLL_SPEED_MIN_ATTRIBUTE)) {
                return LmlAttributes.parseFloat(scrollPane, parser,
                        lmlTagData.getAttribute(OVERSCROLL_SPEED_MIN_ATTRIBUTE));
            }
            return DEFAULT_OVERSCROLL_SPEED_MIN;
        }

        private float getSpeedMax(final ScrollPane scrollPane, final LmlParser parser, final LmlTagData lmlTagData) {
            if (lmlTagData.containsAttribute(OVERSCROLL_SPEED_MAX_ATTRIBUTE)) {
                return LmlAttributes.parseFloat(scrollPane, parser,
                        lmlTagData.getAttribute(OVERSCROLL_SPEED_MAX_ATTRIBUTE));
            }
            return DEFAULT_OVERSCROLL_SPEED_MAX;
        }
    },
    SCROLLBAR_POSITION("scrollbarPositionX", "scrollbarPositionY") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            final boolean positionX = lmlTagData.containsAttribute(SCROLLBAR_POSITION_X_ATTRIBUTE) ? LmlAttributes
                    .parseBoolean(scrollPane, parser, lmlTagData.getAttribute(SCROLLBAR_POSITION_X_ATTRIBUTE))
                    : DEFAULT_SCROLLBAR_POSITION;
            final boolean positionY = lmlTagData.containsAttribute(SCROLLBAR_POSITION_Y_ATTRIBUTE) ? LmlAttributes
                    .parseBoolean(scrollPane, parser, lmlTagData.getAttribute(SCROLLBAR_POSITION_Y_ATTRIBUTE))
                    : DEFAULT_SCROLLBAR_POSITION;
            scrollPane.setScrollBarPositions(positionX, positionY);
        }
    },
    SCROLLING_DISABLED("scrollingDisabled", "scrollingDisabledX", "scrollingDisabledY") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            final boolean disableX = getDisableX(scrollPane, parser, lmlTagData);
            final boolean disableY = getDisableY(scrollPane, parser, lmlTagData);
            scrollPane.setScrollingDisabled(disableX, disableY);
        }

        private boolean getDisableX(final ScrollPane scrollPane, final LmlParser parser, final LmlTagData lmlTagData) {
            if (lmlTagData.containsAttribute(SCROLLING_DISABLED_X_ATTRIBUTE)) {
                return LmlAttributes.parseBoolean(scrollPane, parser,
                        lmlTagData.getAttribute(SCROLLING_DISABLED_X_ATTRIBUTE));
            } else if (lmlTagData.containsAttribute(SCROLLING_DISABLED_ATTRIBUTE)) {
                return LmlAttributes.parseBoolean(scrollPane, parser,
                        lmlTagData.getAttribute(SCROLLING_DISABLED_ATTRIBUTE));
            }
            return DEFAULT_DISABLED_SETTING;
        }

        private boolean getDisableY(final ScrollPane scrollPane, final LmlParser parser, final LmlTagData lmlTagData) {
            if (lmlTagData.containsAttribute(SCROLLING_DISABLED_Y_ATTRIBUTE)) {
                return LmlAttributes.parseBoolean(scrollPane, parser,
                        lmlTagData.getAttribute(SCROLLING_DISABLED_Y_ATTRIBUTE));
            } else if (lmlTagData.containsAttribute(SCROLLING_DISABLED_ATTRIBUTE)) {
                return LmlAttributes.parseBoolean(scrollPane, parser,
                        lmlTagData.getAttribute(SCROLLING_DISABLED_ATTRIBUTE));
            }
            return DEFAULT_DISABLED_SETTING;
        }
    },
    VELOCITY("velocity") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            final float velocity = LmlAttributes.parseFloat(scrollPane, parser, attributeValue);
            scrollPane.setVelocityX(velocity);
            scrollPane.setVelocityY(velocity);
        }
    },
    VELOCITY_X("velocityX") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setVelocityX(LmlAttributes.parseFloat(scrollPane, parser, attributeValue));
        }
    },
    VELOCITY_Y("velocityY") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setVelocityX(LmlAttributes.parseFloat(scrollPane, parser, attributeValue));
        }
    },
    SCROLL_PERCENT("scrollPercent") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            final float scrollPercent = LmlAttributes.parseFloat(scrollPane, parser, attributeValue);
            scrollPane.setScrollPercentX(scrollPercent);
            scrollPane.setScrollPercentY(scrollPercent);
        }
    },
    SCROLL_PERCENT_X("scrollPercentX") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setScrollPercentX(LmlAttributes.parseFloat(scrollPane, parser, attributeValue));
        }
    },
    SCROLL_PERCENT_Y("scrollPercentY") {
        @Override
        protected void apply(final ScrollPane scrollPane, final LmlParser parser, final String attributeValue,
                final LmlTagData lmlTagData) {
            scrollPane.setScrollPercentY(LmlAttributes.parseFloat(scrollPane, parser, attributeValue));
        }
    };
    /** Hardcoded in ScrollPane class. */
    protected static final float DEFAULY_DELAY = 1f;
    protected static final boolean DEFAULT_OVERSCROLL = true;
    protected static final boolean DEFAULT_SCROLLBAR_POSITION = true;
    protected static final boolean DEFAULT_DISABLED_SETTING = false;
    protected static final float DEFAULT_OVERSCROLL_DISTANCE = 50f, DEFAULT_OVERSCROLL_SPEED_MIN = 30f,
            DEFAULT_OVERSCROLL_SPEED_MAX = 200f;

    protected static final String FADE_SCROLL_BARS_DELAY_ATTRIBUTE = "FADESCROLLBARSDELAY";
    protected static final String FADE_SCROLL_BARS_ALPHA_ATTRIBUTE = "FADESCROLLBARSALPHASECONDS";
    protected static final String OVERSCROLL_ATTRIBUTE = "OVERSCROLL";
    protected static final String OVERSCROLL_X_ATTRIBUTE = "OVERSCROLLX";
    protected static final String OVERSCROLL_Y_ATTRIBUTE = "OVERSCROLLY";
    protected static final String SCROLLBAR_POSITION_X_ATTRIBUTE = "SCROLLBARPOSITIONX";
    protected static final String SCROLLBAR_POSITION_Y_ATTRIBUTE = "SCROLLBARPOSITIONY";
    protected static final String OVERSCROLL_DISTANCE_ATTRIBUTE = "OVERSCROLLDISTANCE";
    protected static final String OVERSCROLL_SPEED_MIN_ATTRIBUTE = "OVERSCROLLSPEEDMIN";
    protected static final String OVERSCROLL_SPEED_MAX_ATTRIBUTE = "OVERSCROLLSPEEDMAX";
    protected static final String SCROLLING_DISABLED_ATTRIBUTE = "SCROLLINGDISABLED";
    protected static final String SCROLLING_DISABLED_X_ATTRIBUTE = "SCROLLINGDISABLEDX";
    protected static final String SCROLLING_DISABLED_Y_ATTRIBUTE = "SCROLLINGDISABLEDY";

    // Have to be set after construction, just to be sure.
    public static final String SCROLL_PERCENT_ATTRIBUTE = "SCROLLPERCENT";
    public static final String SCROLL_PERCENT_X_ATTRIBUTE = "SCROLLPERCENTX";
    public static final String SCROLL_PERCENT_Y_ATTRIBUTE = "SCROLLPERCENTY";

    private final String[] aliases;

    private ScrollPaneLmlTagAttributeParser(final String... aliases) {
        this.aliases = aliases;
    }

    @Override
    public void apply(final Object actor, final LmlParser parser, final String attributeValue,
            final LmlTagData lmlTagData) {
        apply((ScrollPane) actor, parser, attributeValue, lmlTagData);
    }

    protected abstract void apply(ScrollPane scrollPane, LmlParser parser, String attributeValue,
            LmlTagData lmlTagData);

    @Override
    public String[] getAttributeNames() {
        return aliases;
    }
}
