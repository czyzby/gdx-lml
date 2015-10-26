package com.github.czyzby.lml.vis.parser.impl.attribute.validator.form;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.vis.ui.reflected.VisFormTable;

/** Sets the selected widget as button to disable if there are any errors in the form. Expects a boolean. If true, calls
 * {@link VisFormTable#setButtonToDisable(Button)}. Mapped to "disableOnError", "disableOnFormError", "formDisable".
 *
 * @author MJ */
public class DisableOnFormErrorLmlAttribute extends AbstractFormChildLmlAttribute<Button> {
    @Override
    public Class<Button> getHandledType() {
        return Button.class;
    }

    @Override
    protected void processFormAttribute(final LmlParser parser, final LmlTag tag, final VisFormTable parent,
            final Button actor, final String rawAttributeData) {
        if (parser.parseBoolean(rawAttributeData, actor)) {
            parent.setButtonToDisable(actor);
        }
    }
}
