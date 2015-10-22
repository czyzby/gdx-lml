package com.github.czyzby.lml.parser.impl.tag.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.impl.tag.builder.FloatRangeLmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Handles {@link ProgressBar} actor. Mapped to "progress", "progressBar", "loading", "loadingBar".
 *
 * @author MJ */
public class ProgressBarLmlTag extends AbstractNonParentalActorLmlTag {
    public ProgressBarLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected FloatRangeLmlActorBuilder getNewInstanceOfBuilder() {
        return new FloatRangeLmlActorBuilder();
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        final FloatRangeLmlActorBuilder rangeBuilder = (FloatRangeLmlActorBuilder) builder;
        rangeBuilder.validateRange(getParser());
        final ProgressBar actor = getNewInstanceOfProgressBar(rangeBuilder);
        actor.setValue(rangeBuilder.getValue());
        return actor;
    }

    /** @param rangeBuilder contains data necessary to construct a float-range-based widget.
     * @return a new instance of progress bar. */
    protected ProgressBar getNewInstanceOfProgressBar(final FloatRangeLmlActorBuilder rangeBuilder) {
        return new ProgressBar(rangeBuilder.getMin(), rangeBuilder.getMax(), rangeBuilder.getStepSize(),
                rangeBuilder.isVertical(), getSkin(rangeBuilder), rangeBuilder.getStyleName());
    }
}
