package com.github.czyzby.lml.vis.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.kiwi.util.gdx.scene2d.Actors;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.impl.tag.builder.AlignedLmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.Separator;

/** Handles {@link Separator} widgets. Cannot have any children. Mapped to "separator".
 *
 * @author MJ
 * @see MenuSeparatorLmlTag */
public class SeparatorLmlTag extends AbstractNonParentalActorLmlTag {
    public SeparatorLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected LmlActorBuilder getNewInstanceOfBuilder() {
        final LmlActorBuilder builder = new AlignedLmlActorBuilder();
        // Separator uses "default"/"vertical" style names instead of "default-horizontal"/"default-vertical".
        builder.setStyleName(Actors.DEFAULT_STYLE);
        return builder;
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        final AlignedLmlActorBuilder alignedBuilder = (AlignedLmlActorBuilder) builder;
        if (alignedBuilder.isVertical() && Actors.DEFAULT_STYLE.equals(alignedBuilder.getStyleName())) {
            return new Separator(true); // Uses default vertical style.
        }
        return new Separator(builder.getStyleName());
    }
}
