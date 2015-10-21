package com.github.czyzby.lml.vis.parser.impl.attribute.button;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** See {@link VisTextButton#setFocusBorderEnabled(boolean)}. Mapped to "focusBorder", "focusBorderEnabled".
 *
 * @author MJ */
public class TextButtonFocusBorderEnabledLmlAttribute implements LmlAttribute<VisTextButton> {
    @Override
    public Class<VisTextButton> getHandledType() {
        return VisTextButton.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final VisTextButton actor,
            final String rawAttributeData) {
        actor.setFocusBorderEnabled(parser.parseBoolean(rawAttributeData, actor));
    }
}
