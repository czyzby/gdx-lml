package com.github.czyzby.lml.vis.parser.impl.attribute;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerListener;

/** Attaches a {@link ClickListener} that shows a {@link ColorPicker} dialog upon clicking, unless the actor is
 * disabled. Initial picker's color will match the color of the clicked widget: this is especially useful if listener is
 * attached to a simple image, label or text button actor. Expects an action ID that references a method which consumes
 * a {@link Color}. Method is invoked each time the color changes; if the dialog is cancelled, method will be invoked
 * with a {@code null}. Mapped to "responsiveColorPicker".
 *
 * @author MJ
 * @see ColorPickerLmlAttribute */
public class ResponsiveColorPickerLmlAttribute extends ColorPickerLmlAttribute {
    @Override
    protected ColorPickerListener prepareColorPickerListener(final ActorConsumer<?, Color> listener) {
        return new ColorPickerListener() {
            @Override
            public void finished(final Color newColor) {
                changed(newColor);
            }

            @Override
            public void changed(final Color newColor) {
                listener.consume(newColor);
            }

            @Override
            public void canceled() {
                changed(null);
            }
        };
    }
}
