package com.github.czyzby.lml.vis.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.vis.parser.impl.tag.builder.NumberSelectorLmlActorBuilder;
import com.kotcrab.vis.ui.widget.NumberSelector;

/** Handles {@link NumberSelector} actor. Mapped to "numberSelector", "selector".
 *
 * @author Kotcrab */
@SuppressWarnings("deprecation")
public class NumberSelectorLmlTag extends VisTableLmlTag {
    public NumberSelectorLmlTag(final LmlParser parser, final LmlTag parentTag, final StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        final NumberSelectorLmlActorBuilder selectorBuilder = (NumberSelectorLmlActorBuilder) builder;
        selectorBuilder.validateRange(getParser());
        return new NumberSelector(builder.getStyleName(), selectorBuilder.getName(), selectorBuilder.getValue(),
                selectorBuilder.getMin(), selectorBuilder.getMax(), selectorBuilder.getStepSize(),
                selectorBuilder.getPrecision());
    }

    @Override
    protected NumberSelectorLmlActorBuilder getNewInstanceOfBuilder() {
        return new NumberSelectorLmlActorBuilder();
    }
}
