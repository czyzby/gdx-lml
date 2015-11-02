package com.github.czyzby.tests.reflected;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.scene2d.Actors;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.annotation.LmlAfter;
import com.github.czyzby.lml.annotation.LmlBefore;
import com.github.czyzby.lml.annotation.LmlInject;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlView;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.github.czyzby.lml.parser.impl.tag.macro.NewAttributeLmlMacroTag.AttributeParsingData;
import com.github.czyzby.lml.scene2d.ui.reflected.ReflectedLmlDialog;
import com.github.czyzby.lml.util.LmlUtilities;
import com.github.czyzby.tests.Main;
import com.github.czyzby.tests.reflected.widgets.BlinkingLabel;

/** Main view of the application. Since it extends {@link AbstractLmlView}, it is both {@link LmlView} (allowing its
 * {@link Stage} to be filled) and {@link ActionContainer} (allowing it methods to be reflected and available in LML
 * templates. Thanks to {@link LmlParser#createView(Class, com.badlogic.gdx.files.FileHandle)} method, parsed root
 * actors go directly into this view's {@link #getStage()}, and an instance of this class is registered as an action
 * container with "main" ID (returned by {@link #getViewId()}).
 *
 * @author MJ */
public class MainView extends AbstractLmlView {
    // Contains template to parse:
    @LmlActor("templateInput") private TextArea templateInput;
    // Is filled with parsed actors after template processing:
    @LmlActor("resultTable") private Table resultTable;
    // Manages buttons. Will be created and filled by the parser.
    @LmlInject private ButtonManager buttonManager;

    /** Creates a new main view with default stage. */
    public MainView() {
        // You'd generally want to construct the stage with a custom SpriteBatch, sharing it across all views.
        super(new Stage(new ScreenViewport()));
    }

    @Override
    public String getViewId() {
        return "main";
    }

    /* Method annotation examples: */

    @LmlBefore
    public void example() {
        // This method will be invoked before parsing of the main.lml template. You can uncomment the log or some other
        // method to see how it works.
        // Gdx.app.log(Lml.LOGGER_TAG, "About to parse main.lml.");
    }

    @LmlAfter
    public void example(final LmlParser parser) {
        // This method will be invoked after main.lml is parsed and MainView's fields are fully injected and processed.
        // Note that both LmlBefore- and LmlAfter-annotated methods can have either no arguments or a single argument:
        // LmlParser; parser argument will never be null - the parser used to process template will be injected.
        // Gdx.app.log(Lml.LOGGER_TAG, "Parsed main.lml with: " + parser);
    }

    /* Reflected methods, available in LML views: */

    /* templates/main.lml */

    /** @return action that sets the action invisible and slowly fades it in. */
    public Action fadeIn() {
        // Used by main window just after view show.
        return Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.5f, Interpolation.fade));
    }

    /** Parses template currently stored in template text area and adds the created actors to result table. */
    public void parseTemplate() {
        final String template = templateInput.getText();
        Main.getParser().getData().addActionContainer(getViewId(), this); // Making our methods available in templates.
        try {
            final Array<Actor> actors = Main.getParser().parseTemplate(template);
            resultTable.clear();
            for (final Actor actor : actors) {
                resultTable.add(actor).row();
            }
        } catch (final Exception exception) {
            onParsingError(exception);
        }
    }

    private void onParsingError(final Exception exception) {
        // Printing the message without stack trace - we don't want to completely flood the console and its usually not
        // relevant anyway. Change to '(...), "Unable to parse LML template:", exception);' for stacks.
        Gdx.app.error("ERROR", "Unable to parse LML template: " + exception.getMessage());
        resultTable.clear();
        resultTable.add("Error occurred. Sorry.");
        Main.getParser().fillStage(getStage(), Gdx.files.internal("templates/dialogs/error.lml"));
    }

    /** Switches the current content of template input to the content of a chosen file.
     *
     * @param actor invokes the action. Expected to have an ID that points to a template. */
    @LmlAction("switch")
    public void switchTemplate(final Button actor) {
        buttonManager.switchCheckedButton(actor);
        // In LML template, we set each button's ID to a template name. Now we extract these:
        final String templateName = LmlUtilities.getActorId(actor);
        templateInput.setText(Gdx.files.internal(toExamplePath(templateName)).readString());
        parseTemplate();
    }

    // Converts template name to a example template path.
    private static String toExamplePath(final String templateName) {
        return "templates/examples/" + templateName + ".lml";
    }

    /* templates/dialogs/error.lml */

    @LmlAction("onErrorApprove")
    public void acceptError(final Dialog dialog) {
        ((Label) LmlUtilities.getActorWithId(dialog, "resultMessage")).setText("Thanks!");
    }

    @LmlAction("onErrorDecline")
    public boolean declineError(final Dialog dialog) {
        ((Label) LmlUtilities.getActorWithId(dialog, "resultMessage")).setText("It's not like you have a choice.");
        // Returning true boolean cancels dialog hiding:
        return ReflectedLmlDialog.CANCEL_HIDING;
    }

    /* templates/examples/checkBox.lml */

    /** @param checkBox will have its text changed. */
    public void switchCase(final CheckBox checkBox) {
        if (checkBox.isChecked()) {
            checkBox.setText(checkBox.getText().toString().toUpperCase());
        } else {
            checkBox.setText(checkBox.getText().toString().toLowerCase());
        }
    }

    /* templates/examples/progressBar.lml */

    @LmlAction("load")
    public void advanceLoadingProgress(final ProgressBar progressBar) {
        progressBar.setValue(progressBar.getValue() + progressBar.getStepSize() * 5f);
    }

    /* templates/examples/actions.lml */

    public String someAction() {
        return "Extracted from method of MainView.";
    }

    @LmlAction("someNamedAction")
    public String getSomeText() {
        return "@LmlAction-annotated method result.";
    }

    /** @param container has to be sized.
     * @return semi-random size depending on length of container's toString() result. */
    @LmlAction({ "size", "getRandomSize" })
    public float getSize(final Container<?> container) {
        return container.toString().length() * 30f * MathUtils.random();
    }

    /** @param button its text will be modified if it's not too long. */
    public void pressButton(final TextButton button) {
        if (button.getText().length() < 30) {
            button.setText(button.getText() + "!");
        } else {
            button.setText("Isn't it enough?!");
        }
    }

    /** @param bar will have a random initial value set. */
    public void setInitialValue(final ProgressBar bar) {
        bar.setValue(MathUtils.random(bar.getMinValue(), bar.getMaxValue()));
    }

    /* templates/examples/actorMacro.lml */

    /** @return a new instance of customized actor. */
    public Label getSomeActor() {
        final Label actor = new Label("Actor created in plain Java.", Main.getParser().getData().getDefaultSkin());
        actor.setColor(Color.PINK);
        return actor;
    }

    /* templates/examples/evaluate.lml */

    @LmlAction("stringConsumingMethod")
    public String toUpperCase(final String value) {
        return value.toUpperCase();
    }

    /* templates/examples/while.lml */

    /** @return returns a random value between 0 and 1. */
    @LmlAction("random")
    public float getRandomValue() {
        return MathUtils.random();
    }

    /* templates/examples/newAttribute.lml */

    /** @param data contains data necessary to parse LML attribute. */
    @LmlAction("upperCaseAttribute")
    public void processUpperCaseAttribute(final AttributeParsingData data) {
        if (data.getActor() instanceof Label) {
            final Label label = (Label) data.getActor();
            label.setText(
                    label.getText() + data.getParser().parseString(data.getRawAttributeData(), label).toUpperCase());
        } else {
            data.getParser().throwErrorIfStrict("My attribute supports only labels.");
        }
    }

    /* templates/examples/newTag.lml */

    /** @return a new instance of customized widget: {@link BlinkingLabel}; */
    public BlinkingLabel getBlinkingLabel() {
        return new BlinkingLabel(Strings.EMPTY_STRING, Main.getParser().getData().getDefaultSkin(),
                Actors.DEFAULT_STYLE);
    }

    @Override
    public String toString() {
        // Printing all example buttons and current playground content:
        return resultTable + buttonManager.printButtons();
    }
}
