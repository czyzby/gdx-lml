package com.github.czyzby.lml.vis.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.actor.SliderLmlTag;
import com.github.czyzby.lml.parser.impl.tag.builder.FloatRangeLmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.VisSlider;

/** Handles {@link Slider} actor. Cannot have children. Mapped to "slider". "visSlider".
 *
 * @author MJ */
public class VisSliderLmlTag extends SliderLmlTag {
    public VisSliderLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected VisSlider getNewInstanceOfProgressBar(final FloatRangeLmlActorBuilder rangeBuilder) {
        return new VisSlider(rangeBuilder.getMin(), rangeBuilder.getMax(), rangeBuilder.getStepSize(),
                rangeBuilder.isVertical(), rangeBuilder.getStyleName());
    }
}
