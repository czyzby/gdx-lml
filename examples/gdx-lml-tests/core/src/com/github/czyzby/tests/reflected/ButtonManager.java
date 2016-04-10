package com.github.czyzby.tests.reflected;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.annotation.LmlInject;

/** Manages button actors. An example of {@link LmlInject} usage.
 *
 * @author MJ */
public class ButtonManager {
    // {examples} is converted to the argument with this name, which is an array of all examples. In main.lml, we assign
    // each button to ID equal to one of examples. This array will contain all buttons in the order of the argument
    // array. We don't actually need these - we're doing this because we can (TM). Buttons are printed with #toString().
    @LmlActor("{examples}") private Array<TextButton> exampleButtons;

    // Utility. Holds a reference to a button with current LML template.
    private Button checkedButton;

    /** @param button will become currently checked button. */
    public void switchCheckedButton(final Button button) {
        if (checkedButton != null) {
            // Unchecking previous button.
            checkedButton.setProgrammaticChangeEvents(false);
            checkedButton.setChecked(false);
        }
        button.setChecked(true);
        checkedButton = button;
    }

    /** @return current example buttons converted to string. */
    public String printButtons() {
        return "\n" + Strings.join("\n", exampleButtons);
    }
}
