
package com.github.czyzby.lml.parser.impl.attribute.label;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Changes font scale for the {@link Label}.
 * Mapped to "fontScale".
 *
 * @see Label#setFontScale(float)
 * @author Metaphore */
public class LabelFontScaleLmlAttribute implements LmlAttribute<Label> {
    @Override
    public Class<Label> getHandledType() {
        return Label.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Label actor, final String rawAttributeData) {
        actor.setFontScale(parser.parseFloat(rawAttributeData, actor));
    }
}
