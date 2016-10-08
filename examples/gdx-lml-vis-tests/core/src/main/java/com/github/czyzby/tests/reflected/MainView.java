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
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.disposable.DisposableArray;
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
import com.github.czyzby.lml.util.Lml;
import com.github.czyzby.lml.util.LmlUtilities;
import com.github.czyzby.lml.vis.ui.VisTabTable;
import com.github.czyzby.tests.reflected.widgets.BlinkingLabel;
import com.kotcrab.vis.ui.util.ToastManager;
import com.kotcrab.vis.ui.util.adapter.ListAdapter;
import com.kotcrab.vis.ui.util.adapter.SimpleListAdapter;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.toast.ToastTable;

/** Main view of the application. Since it extends {@link AbstractLmlView}, it is both {@link LmlView} (allowing its
 * {@link Stage} to be filled) and {@link ActionContainer} (allowing it methods to be reflected and available in LML
 * templates. Thanks to {@link LmlParser#createView(Class, com.badlogic.gdx.files.FileHandle)} method, parsed root
 * actors go directly into this view's {@link #getStage()}, and an instance of this class is registered as an action
 * container with "main" ID (returned by {@link #getViewId()}).
 *
 * @author MJ */
public class MainView extends AbstractLmlView {
    // Contains template to parse:
    @LmlActor("templateInput") private VisTextArea templateInput;
    // Is filled with parsed actors after template processing:
    @LmlActor("resultTable") private Table resultTable;
    // Manages buttons. Will be created and filled by the parser.
    @LmlInject private ButtonManager buttonManager;
    // Used to create this view. @LmlInject-ing this field would also work.
    private LmlParser parser;
    // Used to dispose heavy widgets, like ColorPicker:
    private final DisposableArray<Disposable> heavyWidgets = DisposableArray.newArray();

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
    public void example(final LmlParser parser) {
        // This method will be invoked before parsing of the main.lml template. You can uncomment the log or some other
        // method to see how it works.
        // Gdx.app.log(Lml.LOGGER_TAG, "About to parse main.lml.");

        // Assigning parser - if you want to use @LmlInject instead to fill the parser field, try commenting this line:
        this.parser = parser;

        // Note that both LmlBefore- and LmlAfter-annotated methods can have either no arguments or a single argument:
        // LmlParser; parser argument will never be null - the parser used to process template will be injected.
    }

    @LmlAfter
    public void example() {
        // This method will be invoked after main.lml is parsed and MainView's fields are fully injected and processed.
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
        destroyHeavyWidgets();
        final String template = templateInput.getText();
        parser.getData().addActionContainer(getViewId(), this); // Making our methods available in templates.
        try {
            final Array<Actor> actors = parser.parseTemplate(template);
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
        Gdx.app.error(Lml.LOGGER_TAG, "Unable to parse LML template: " + exception);
        resultTable.clear();
        resultTable.add("Error occurred. Sorry.");
        parser.fillStage(getStage(), Gdx.files.internal("templates/dialogs/error.lml"));
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
        // Forcing view to recalculate preferred size (required to update scroll pane):
        templateInput.invalidateHierarchy();
        parseTemplate();
    }

    // Converts template name to a example template path.
    private static String toExamplePath(final String templateName) {
        return "templates/examples/" + templateName + ".lml";
    }

    /* templates/dialogs/error.lml */

    @LmlAction("onErrorApprove")
    public void acceptError(final VisDialog dialog) {
        ((Label) LmlUtilities.getActorWithId(dialog, "resultMessage")).setText("Thanks!");
    }

    @LmlAction("onErrorDecline")
    public boolean declineError(final VisDialog dialog) {
        ((Label) LmlUtilities.getActorWithId(dialog, "resultMessage")).setText("It's not like you have a choice.");
        // Returning true boolean cancels dialog hiding:
        return ReflectedLmlDialog.CANCEL_HIDING;
    }

    /* templates/examples/arrays.lml */

    @LmlAction("someMethodReturningArray")
    public Array<Float> getSomeNumbers() {
        return GdxArrays.newArray(4.2f, 3f);
    }

    /* templates/examples/checkBox.lml */

    /** @param button will have its text changed. */
    public void switchCase(final TextButton button) {
        if (button.isChecked()) {
            button.setText(button.getText().toString().toUpperCase());
        } else {
            button.setText(button.getText().toString().toLowerCase());
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
    public void pressButton(final VisTextButton button) {
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
        final Label actor = new Label("Actor created in plain Java.", parser.getData().getDefaultSkin());
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
        return new BlinkingLabel(Strings.EMPTY_STRING, parser.getData().getDefaultSkin(), Actors.DEFAULT_STYLE);
    }

    /* templates/examples/vis/collapsibleWidget.lml */

    @LmlAction("collapse")
    public void toggleCollapsedStatus(final CollapsibleWidget collapsibleWidget) {
        collapsibleWidget.setCollapsed(!collapsibleWidget.isCollapsed());
    }

    @LmlAction("uncollapseAll")
    public void showAllCollapsedWidgets() {
        for (final Actor actor : resultTable.getChildren()) {
            if (actor instanceof CollapsibleWidget) {
                ((CollapsibleWidget) actor).setCollapsed(false);
            }
        }
    }

    /* templates/examples/vis/colorPicker.lml */

    /** @param result result of a color picker dialog. Will become an actor's color. */
    @LmlAction("setColor")
    public void setColorFromColorPicker(final Color result) {
        if (result != null) { // null -> cancelled
            LmlUtilities.getActorWithId(resultTable, "set").setColor(result);
        }
    }

    /** @param currentColor currently selected color in a color picker dialog. Will become an actor's color. */
    @LmlAction("changeColor")
    public void changeColorWithColorPicker(final Color currentColor) {
        if (currentColor != null) {
            LmlUtilities.getActorWithId(resultTable, "change").setColor(currentColor);
        }
    }

    /** @param disposable will be eventually disposed after a new template is parsed or before application is closed. */
    @LmlAction("scheduleDispose")
    public void addHeavyWidget(final Disposable disposable) {
        heavyWidgets.add(disposable);
    }

    /** @param result result of a color picker dialog. Will become an actor's color. */
    @LmlAction("customColor")
    public void setColorFromCustomColorPicker(final Color result) {
        if (result != null) {
            LmlUtilities.getActorWithId(resultTable, "new").setColor(result);
        }
    }

    /* templates/examples/vis/listView.lml */

    @LmlAction("listAdapter")
    public ListAdapter<?> getCustomListAdapter() {
        final String prompt = "This list was created directly in Java and passed with a custom adapter that converts text to labels.";
        final Array<String> values = new Array<String>(Strings.split(prompt, ' '));
        return new SimpleListAdapter<String>(values);
    }

    @LmlAction("itemListener")
    public void handleItemClick(final String selectedItem) {
        // Printing selected item into the console:
        Gdx.app.log(Lml.LOGGER_TAG, "Selected: " + selectedItem);
    }

    /* templates/examples/vis/menu.lml */

    /** @param menuItem its text will be appended to the result table. */
    @LmlAction("chooseItem")
    public void selectMenuItem(final MenuItem menuItem) {
        final Label result = (Label) LmlUtilities.getActorWithId(resultTable, "result");
        result.setText(menuItem.getText());
    }

    /* templates/examples/vis/tabbedPane.lml */

    /** @return simple fading in action. */
    @LmlAction("showTab")
    public Action getTabShowingAction() {
        return Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.1f));
    }

    /** @return simple fading out action. */
    @LmlAction("hideTab")
    public Action getTabHidingAction() {
        return Actions.fadeOut(0.1f);
    }

    /** @param tabTable will contain an extra label each time this method is invoked. Expects that the table contains a
     *            label in first cell. */
    @LmlAction("showSomeTab")
    public void doOnCustomTabShow(final VisTabTable tabTable) {
        // Note that this method might be called a few times before the tag is even shown due to TabbedPane
        // implementation. This attribute should be used for some simple additional showing actions (visual-only, if
        // possible), rather than modifying table's state, like in this example.
        final Label label = (Label) tabTable.getCells().first().getActor();
        label.setText(label.getText() + "!");
    }

    /* templates/examples/vis/toast.lml */

    private ToastManager toastManager;

    @Override
    public void resize(final int width, final int height, final boolean centerCamera) {
        super.resize(width, height, centerCamera);
        if (toastManager != null) {
            toastManager.resize();
        }
    }

    private ToastManager getToastManager() {
        if (toastManager == null) {
            toastManager = new ToastManager(getStage());
        }
        return toastManager;
    }

    /** @param toast will be displayed on the stage using toast manager. */
    @LmlAction("addToast")
    public void addToast(final ToastTable toast) {
        final ToastManager manager = getToastManager();
        manager.show(toast);
        manager.toFront();
    }

    /* templates/examples/vis/validatableTextField.lml */

    @LmlAction("isNotBlank")
    public boolean isStringNotBlank(final String value) {
        return Strings.isNotBlank(value);
    }

    // Utility methods:

    @Override
    public void dispose() {
        super.dispose();
        destroyHeavyWidgets();
    }

    @LmlAction("closeDialog")
    public void closeDialog(Actor actor) {
        while (actor != null && !(actor instanceof VisWindow)) {
            actor = actor.getParent();
        }
        if (actor != null) {
            ((VisWindow) actor).fadeOut();
        }
    }

    private void destroyHeavyWidgets() {
        heavyWidgets.dispose();
        heavyWidgets.clear();
    }

    @Override
    public String toString() {
        // Printing all example buttons and current playground content:
        return resultTable + buttonManager.printButtons();
    }

    @LmlAction("clearLss")
    public void clearStyleSheets() {
        parser.getStyleSheet().clearStyles();
    }
}
