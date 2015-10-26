package com.github.czyzby.lml.vis.ui.reflected;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.github.czyzby.kiwi.util.common.Strings;
import com.kotcrab.vis.ui.util.form.FormInputValidator;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

/** Represents a {@link SimpleFormValidator}'s widgets container. Additionally to features of {@link VisTable},
 * maintains a reference to an internal form validator. Meant to store actors that represent a form. Automatically adds
 * {@link VisValidatableTextField} to form.
 *
 * @author MJ */
public class VisFormTable extends VisTable {
    private static final FormInputValidator MOCK_UP_VALIDATOR = new FormInputValidator(Strings.EMPTY_STRING) {
        @Override
        protected boolean validate(final String input) {
            return true;
        }
    }; // TODO Remove once SimpleFormValidator#custom(VisValidableTextField) becomes available.
    private final SimpleFormValidator formValidator = createFormValidator();

    /** @return a new instance of {@link SimpleFormValidator}, managed by this table. */
    protected SimpleFormValidator createFormValidator() {
        return new SimpleFormValidator(null, null);
    }

    /** @return internal {@link SimpleFormValidator} instance. */
    public SimpleFormValidator getFormValidator() {
        return formValidator;
    }

    @Override
    public <T extends Actor> Cell<T> add(final T actor) {
        if (actor instanceof VisValidatableTextField) {
            formValidator.custom((VisValidatableTextField) actor, MOCK_UP_VALIDATOR);
        }
        return super.add(actor);
    }

    /** See {@link SimpleFormValidator#setErrorMsgLabel(Label)}.
     *
     * @param label will show form errors. */
    public void setErrorMessageLabel(final Label label) {
        formValidator.setErrorMsgLabel(label);
    }

    public void setButtonToDisable(final Button button) {
        formValidator.setButtonToDisable(button);
    }
}
