package com.github.czyzby.lml.parser.impl.attribute.building;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.builder.FloatRangeLmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlBuildingAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Used to parse initial value of numeric range widgets. For example, this might be the initial progress of a loading
 * bar. Used by Touchpad as deadzone radius value. Expects a float. Mapped to "value".
 *
 * @author MJ */
public class RangeInitialValueLmlAttribute implements LmlBuildingAttribute<FloatRangeLmlActorBuilder> {
    @Override
    public Class<?>[] getHandledTypes() {
        return new Class<?>[] { ProgressBar.class, Slider.class, Touchpad.class };
    }

    @Override
    public boolean process(final LmlParser parser, final LmlTag tag, final FloatRangeLmlActorBuilder builder,
            final String rawAttributeData) {
        builder.setValue(parser.parseFloat(rawAttributeData));
        return FULLY_PARSED;
    }
}
