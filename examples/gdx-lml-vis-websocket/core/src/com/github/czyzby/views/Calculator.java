package com.github.czyzby.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.czyzby.Core;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.github.czyzby.lml.parser.impl.tag.macro.util.Equation;
import com.kotcrab.vis.ui.widget.VisTextArea;

/** A simple calculator. Uses internal LML {@link Equation} to process input.
 *
 * @author MJ */
public class Calculator extends AbstractLmlView {
    /** This is internal LML API. The same code is used for your calculations in LML templates, so you can use not only
     * numbers, but also logical values (true, false), binary operators and strings in your equations. No typing can
     * lead to funny evaluations: for example, 0..4 is considered a string rather than a failed attempt at creating a
     * float. However, most errors will lead to exceptions. Limited to int and float ranges; does not detect
     * overflows. */
    private final Equation calculator = new Equation();

    @LmlActor("input") VisTextArea equationInput;
    @LmlActor("result") Label result;

    public Calculator() {
        super(Core.newStage());
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/Calculator.lml");
    }

    @LmlAction("add")
    void addEquasionSign(final TextButton button) {
        // Inserting sign in current cursor position:
        final int cursor = equationInput.getCursorPosition();
        final String input = equationInput.getText();
        equationInput.setText(input.substring(0, cursor) + button.getText() + input.substring(cursor, input.length()));
        equationInput.setCursorPosition(cursor + 1);
    }

    @LmlAction("calculate")
    void calculate() {
        try {
            result.setText(calculator.getResult(equationInput.getText()));
        } catch (final Exception exception) {
            result.setText(":C");
            Gdx.app.error("Calculator", exception.getMessage());
        }
    }

    @Override
    public void show() {
        // Clearing input on each view showing.
        equationInput.setText(Strings.EMPTY_STRING);
    }

    @Override
    public String getViewId() {
        return "calc";
    }
}
