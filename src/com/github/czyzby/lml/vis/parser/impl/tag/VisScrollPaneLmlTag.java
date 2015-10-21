package com.github.czyzby.lml.vis.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.actor.ScrollPaneLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.VisScrollPane;

/**
 * Handles {@link VisScrollPane} actor. Can have a single child tag which will be set as scroll pane's managed widget.
 * Parses plain text between tags to a label and sets it as its managed widget. Mapped to "scrollPane", "scroll",
 * "scrollable".
 * @author Kotcrab
 */
public class VisScrollPaneLmlTag extends ScrollPaneLmlTag {
    public VisScrollPaneLmlTag(LmlParser parser, LmlTag parentTag, String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(LmlActorBuilder builder) {
        return new VisScrollPane(null, builder.getStyleName());
    }

    @Override
    protected Class<?> getActorType() {
        return VisScrollPane.class;
    }
}
