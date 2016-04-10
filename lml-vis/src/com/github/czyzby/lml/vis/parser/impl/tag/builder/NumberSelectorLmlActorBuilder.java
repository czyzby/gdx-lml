package com.github.czyzby.lml.vis.parser.impl.tag.builder;

import com.github.czyzby.kiwi.util.gdx.scene2d.Actors;
import com.github.czyzby.lml.parser.impl.tag.builder.FloatRangeLmlActorBuilder;
import com.kotcrab.vis.ui.widget.NumberSelector;

/**
 * Used to construct {@link NumberSelector} widget
 * @author Kotcrab
 */
public class NumberSelectorLmlActorBuilder extends FloatRangeLmlActorBuilder {
    private String name;
    private int precision;

    @Override
    protected String getInitialStyleName() {
        // AlignedLmlActorBuilder defaults to "default-horizontal" which would be invalid for NumberSelector
        return Actors.DEFAULT_STYLE;
    }

    /** @return selector name */
    public String getName() {
        return name;
    }

    /** @param name selector name displayed before value input field */
    public void setName(final String name) {
        this.name = name;
    }

    /** @return selector precision */
    public int getPrecision() {
        return precision;
    }

    /**
     * @param precision selector precision defines how many digits after decimal point will be shown,
     * see {@link NumberSelector#setPrecision(int)}
     */
    public void setPrecision(final int precision) {
        this.precision = precision;
    }
}
