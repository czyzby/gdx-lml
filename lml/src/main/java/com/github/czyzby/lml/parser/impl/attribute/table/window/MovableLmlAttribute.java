package com.github.czyzby.lml.parser.impl.attribute.table.window;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** See {@link Window#setMovable(boolean)}. Mapped to "movable".
 *
 * @author MJ */
public class MovableLmlAttribute implements LmlAttribute<Window> {
    @Override
    public Class<Window> getHandledType() {
        return Window.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Window actor, final String rawAttributeData) {
        actor.setMovable(parser.parseBoolean(rawAttributeData, actor));
    }
}
