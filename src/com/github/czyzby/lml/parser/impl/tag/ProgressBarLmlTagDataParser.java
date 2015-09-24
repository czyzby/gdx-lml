package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.attribute.ProgressBarLmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.tag.parent.ProgressBarLmlParent;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public class ProgressBarLmlTagDataParser extends AbstractLmlTagDataParser<ProgressBar> {
    private static final float DEFAULT_STEPS_AMOUNT = 100f;
    private static final float DEFAULT_MIN = 0f;
    private static final float DEFAULT_RANGE_SIZE = 1f;

    // Have to be parsed upon creation.
    public static final String MIN_ATTRIBUTE = "MIN";
    public static final String MAX_ATTRIBUTE = "MAX";
    public static final String STEP_SIZE_ATTRIBUTE = "STEPSIZE";
    public static final String VERTICAL_ATTRIBUTE = "VERTICAL";

    private static final ObjectMap<String, LmlTagAttributeParser> ATTRIBUTE_PARSERS;

    private final ObjectMap<String, LmlTagAttributeParser> attributeParsers = new ObjectMap<String, LmlTagAttributeParser>(
            ATTRIBUTE_PARSERS);

    static {
        ATTRIBUTE_PARSERS = GdxMaps.newObjectMap();
        for (final LmlTagAttributeParser parser : ProgressBarLmlTagAttributeParser.values()) {
            registerParser(parser);
        }
    }

    public static void registerParser(final LmlTagAttributeParser parser) {
        for (final String alias : parser.getAttributeNames()) {
            ATTRIBUTE_PARSERS.put(alias.toUpperCase(), parser);
        }
    }

    public static void unregisterParser(final String withAlias) {
        ATTRIBUTE_PARSERS.remove(withAlias);
    }

    @Override
    protected void parseAttributes(final LmlTagData lmlTagData, final LmlParser parser, final Actor actor) {
        super.parseAttributes(lmlTagData, parser, actor);
        for (final Entry<String, String> attribute : lmlTagData.getAttributes()) {
            if (attributeParsers.containsKey(attribute.key)) {
                attributeParsers.get(attribute.key).apply(actor, parser, attribute.value, lmlTagData);
            }
        }
    }

    @Override
    public void registerAttributeParser(final LmlTagAttributeParser parser) {
        for (final String alias : parser.getAttributeNames()) {
            attributeParsers.put(alias.toUpperCase(), parser);
        }
    }

    @Override
    public void unregisterAttributeParser(final String attributeName) {
        attributeParsers.remove(attributeName);
    }

    @Override
    protected ProgressBar parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
        final float min = getMin(lmlTagData, parser);
        final float max = getMax(lmlTagData, parser, min);
        final float stepSize = getStepSize(lmlTagData, parser, min, max);
        final boolean vertical = LmlAttributes.parseBoolean(null, parser, lmlTagData.getAttribute(VERTICAL_ATTRIBUTE));
        if (containsStyleAttribute(lmlTagData)) {
            return getNewInstanceWithStyle(lmlTagData, parser, min, max, stepSize, vertical);
        }
        // Default style name is not "default", using no-style-name constructor.
        return getNewInstanceWithoutStyle(parser, min, max, stepSize, vertical);
    }

    protected ProgressBar getNewInstanceWithoutStyle(final LmlParser parser, final float min, final float max,
            final float stepSize, final boolean vertical) {
        return new ProgressBar(min, max, stepSize, vertical, parser.getSkin());
    }

    protected ProgressBar getNewInstanceWithStyle(final LmlTagData lmlTagData, final LmlParser parser, final float min,
            final float max, final float stepSize, final boolean vertical) {
        return new ProgressBar(min, max, stepSize, vertical, parser.getSkin(), getStyleName(lmlTagData, parser));
    }

    protected float getMin(final LmlTagData lmlTagData, final LmlParser parser) {
        return lmlTagData.containsAttribute(MIN_ATTRIBUTE)
                ? LmlAttributes.parseFloat(null, parser, lmlTagData.getAttribute(MIN_ATTRIBUTE)) : DEFAULT_MIN;
    }

    protected float getMax(final LmlTagData lmlTagData, final LmlParser parser, final float min) {
        return lmlTagData.containsAttribute(MAX_ATTRIBUTE)
                ? LmlAttributes.parseFloat(null, parser, lmlTagData.getAttribute(MAX_ATTRIBUTE))
                : min + DEFAULT_RANGE_SIZE;
    }

    protected float getStepSize(final LmlTagData lmlTagData, final LmlParser parser, final float min, final float max) {
        final float stepSize;
        if (lmlTagData.containsAttribute(STEP_SIZE_ATTRIBUTE)) {
            final String stepSizeAttribute = LmlAttributes.parseString(null, parser,
                    lmlTagData.getAttribute(STEP_SIZE_ATTRIBUTE));
            if (stepSizeAttribute.charAt(stepSizeAttribute.length() - 1) == PERCENT_OPERATOR) {
                stepSize = getStepSizeFromPercent(min, max, stepSizeAttribute);
            } else {
                stepSize = Float.parseFloat(stepSizeAttribute);
            }
        } else {
            stepSize = (max - min) / DEFAULT_STEPS_AMOUNT;
        }
        if (stepSize > max - min) {
            throw new LmlParsingException("Progress bar step size cannot be greater than range size.", parser);
        }
        return stepSize;
    }

    private static float getStepSizeFromPercent(final float min, final float max, final String stepSizeAttribute) {
        return (max - min) * Float.parseFloat(stepSizeAttribute.substring(0, stepSizeAttribute.length() - 1));
    }

    @Override
    protected LmlParent<ProgressBar> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
            final LmlParent<?> parent) {
        return new ProgressBarLmlParent(lmlTagData, parseChild(lmlTagData, parser), parent, parser);
    }
}
