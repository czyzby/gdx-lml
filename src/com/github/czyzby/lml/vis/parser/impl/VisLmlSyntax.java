package com.github.czyzby.lml.vis.parser.impl;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.impl.DefaultLmlSyntax;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ActorLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ButtonLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ContainerLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.HorizontalGroupLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.StackLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.TooltipLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.TouchpadLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.VerticalGroupLmlTagProvider;
import com.github.czyzby.lml.util.LmlUserObject.StandardTableTarget;
import com.github.czyzby.lml.util.LmlUserObject.TableExtractor;
import com.github.czyzby.lml.vis.parser.impl.attribute.FocusBorderEnabledLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.building.NumberSelectorNameLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.building.ShowWindowBorderLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.button.ImageButtonGenerateDisabledLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.collapsible.CollapsedLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.BlinkTimeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.CursorLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.InputAlignLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.MaxLengthLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.MessageLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.PasswordCharacterLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.PasswordModeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.PrefRowsLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.RestoreLastValidLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.SelectAllLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.ValidationEnabledLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.numberselector.PrecisionLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.numberselector.ProgrammaticChangeEventsLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.split.MaxSplitLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.split.MinSplitLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.split.SplitAmountLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.table.UseCellDefaultsLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tooltip.DelayLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tooltip.TooltipFadeTimeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.CustomValidatorLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.ErrorMessageLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.GreaterOrEqualLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.GreaterThanLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.HideOnEmptyInputLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.LesserOrEqualLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.LesserThanLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.form.DisableOnFormErrorLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.form.ErrorMessageLabelLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.window.AddCloseButtonLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.window.CloseOnEscapeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.window.OnResultLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.CollapsibleWidgetLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.FormValidatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.NumberSelectorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisCheckBoxLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisDialogLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisImageButtonLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisImageLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisImageTextButtonLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisLabelLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisListLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisProgressBarLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisRadioButtonLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisScrollPaneLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisSelectBoxLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisSliderLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisSplitPaneLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisTableLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisTextAreaLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisTextButtonLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisTextFieldLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisTooltipLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisTreeLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisValidatableTextFieldLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisWindowLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.validator.CustomValidatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.validator.FloatValidatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.validator.GreaterThanValidatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.validator.IntegerValidatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.validator.LesserThanValidatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.validator.NotEmptyValidatorLmlTagProvider;
import com.kotcrab.vis.ui.widget.VisDialog;

/** Replaces regular Scene2D actor tags with Vis UI widgets. Supports the same core syntax (operators).
 *
 * @author MJ
 * @author Kotcrab */
public class VisLmlSyntax extends DefaultLmlSyntax {
    public VisLmlSyntax() {
        overrideTableExtractors();
    }

    /** Since some multi-table Vis widgets do not extend standard Scene2D widgets, table extractors from multi-table
     * actors need to be changed. */
    protected void overrideTableExtractors() {
        StandardTableTarget.MAIN.setTableExtractor(new TableExtractor() {
            @Override
            public Table extract(final Table table) {
                if (table instanceof Dialog) {
                    return ((Dialog) table).getContentTable();
                } else if (table instanceof VisDialog) {
                    return ((VisDialog) table).getContentTable();
                }
                return table;
            }
        });
        StandardTableTarget.BUTTON.setTableExtractor(new TableExtractor() {
            @Override
            public Table extract(final Table table) {
                if (table instanceof Dialog) {
                    return ((Dialog) table).getButtonTable();
                } else if (table instanceof VisDialog) {
                    return ((VisDialog) table).getButtonsTable();
                }
                return table;
            }
        });
    }

    @Override
    protected void registerActorTags() {
        // Standard Scene2D tags - abstract bases for Vis widgets or actors with no VisUI equivalents:
        addTagProvider(new ActorLmlTagProvider(), "actor", "group", "empty", "mock", "blank", "placeholder");
        addTagProvider(new ButtonLmlTagProvider(), "button");
        addTagProvider(new ContainerLmlTagProvider(), "container", "single");
        addTagProvider(new HorizontalGroupLmlTagProvider(), "horizontal", "horizontalGroup");
        addTagProvider(new StackLmlTagProvider(), "stack");
        addTagProvider(new TooltipLmlTagProvider(), "tooltip"); // VisTooltipLmlTagProvider supports Vis tooltips.
        addTagProvider(new TouchpadLmlTagProvider(), "touchpad", "touch");
        addTagProvider(new VerticalGroupLmlTagProvider(), "vertical", "verticalGroup");

        // Vis actors:
        addTagProvider(new VisCheckBoxLmlTagProvider(), "checkBox", "visCheckBox", "check");
        addTagProvider(new VisDialogLmlTagProvider(), "dialog", "visDialog", "popup");
        addTagProvider(new VisImageButtonLmlTagProvider(), "imageButton", "visImageButton");
        addTagProvider(new VisImageLmlTagProvider(), "image", "visImage", "img", "icon");
        addTagProvider(new VisImageTextButtonLmlTagProvider(), "imageTextButton", "visImageTextButton");
        addTagProvider(new VisLabelLmlTagProvider(), "label", "visLabel", "text", "txt", "li");
        addTagProvider(new VisListLmlTagProvider(), "list", "visList", "ul");
        addTagProvider(new VisProgressBarLmlTagProvider(), "progressBar", "visProgressBar", "progress", "loading",
                "loadingBar");
        addTagProvider(new VisRadioButtonLmlTagProvider(), "radioButton", "visRadioButton", "radio");
        addTagProvider(new VisScrollPaneLmlTagProvider(), "scrollPane", "visScrollPane", "scroll", "scrollable");
        addTagProvider(new VisSelectBoxLmlTagProvider(), "select", "selectBox", "visSelectBox");
        addTagProvider(new VisSliderLmlTagProvider(), "slider", "visSlider");
        addTagProvider(new VisSplitPaneLmlTagProvider(), "splitPane", "visSplitPane", "split", "splitable");
        addTagProvider(new VisTableLmlTagProvider(), "table", "visTable", "div", "td", "th");
        addTagProvider(new VisTextAreaLmlTagProvider(), "textArea", "inputArea", "multilineInput");
        addTagProvider(new VisTextButtonLmlTagProvider(), "textButton", "visTextButton", "a");
        addTagProvider(new VisTextFieldLmlTagProvider(), "textField", "visTextField", "input", "textInput");
        addTagProvider(new VisTreeLmlTagProvider(), "tree", "visTree", "root");
        addTagProvider(new VisWindowLmlTagProvider(), "window", "visWindow");

        // Vis unique actors:
        addTagProvider(new CollapsibleWidgetLmlTagProvider(), "collapsible", "collapsibleWidget");
        addTagProvider(new FormValidatorLmlTagProvider(), "form", "formValidator", "formTable");
        addTagProvider(new NumberSelectorLmlTagProvider(), "numberSelector", "numSelector", "selector");
        addTagProvider(new VisTooltipLmlTagProvider(), "visTooltip");
        addTagProvider(new VisValidatableTextFieldLmlTagProvider(), "validatable", "validatableTextField",
                "visValidatableTextField");

        // TODO other vis widgets

        // Vis validators:
        addTagProvider(new CustomValidatorLmlTagProvider(), "validator", "validate", "customValidator");
        addTagProvider(new FloatValidatorLmlTagProvider(), "floatValidator", "isFloat");
        addTagProvider(new GreaterThanValidatorLmlTagProvider(), "greaterThan", "greaterThanValidator");
        addTagProvider(new IntegerValidatorLmlTagProvider(), "integerValidator", "intValidator", "isInt", "isInteger");
        addTagProvider(new LesserThanValidatorLmlTagProvider(), "lesserThan", "lesserThanValidator");
        addTagProvider(new NotEmptyValidatorLmlTagProvider(), "notEmpty", "notEmptyValidator", "nonEmpty",
                "isNotEmpty");
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        registerVisAttributes();
    }

    /** Registers attributes of VisUI-specific actors. */
    protected void registerVisAttributes() {
        registerCollapsibleWidgetAttributes();
        registerNumberSelectorAttributes();
        registerValidatableTextFieldAttributes();
        registerValidatorAttributes();
        // TODO other vis attributes
    }

    // Common extra attributes:

    @Override
    protected void registerBuildingAttributes() {
        super.registerBuildingAttributes();
        // VisWindowLmlActorBuilder:
        addBuildingAttributeProcessor(new ShowWindowBorderLmlAttribute(), "showBorder", "showWindowBorder");
        // NumberSelectorLmlActorBuilder:
        addBuildingAttributeProcessor(new NumberSelectorNameLmlAttribute(), "name");
    }

    @Override
    protected void registerCommonAttributes() {
        super.registerCommonAttributes();
        // BorderOwner:
        addAttributeProcessor(new FocusBorderEnabledLmlAttribute(), "focusBorder", "focusBorderEnabled");
    }

    @Override
    protected void registerTooltipAttributes() {
        super.registerTooltipAttributes();
        // Tooltip (VisUI pre-LibGDX 1.6.5 implementation):
        addAttributeProcessor(new DelayLmlAttribute(), "delay", "appearDelay");
        addAttributeProcessor(new TooltipFadeTimeLmlAttribute(), "fadeTime", "fadingTime");
    }

    // Specific actor extra attributes:

    @Override
    protected void registerButtonAttributes() {
        super.registerButtonAttributes();
        // VisImageButton:
        addAttributeProcessor(new ImageButtonGenerateDisabledLmlAttribute(), "generateDisabled",
                "generateDisabledImage");
    }

    @Override
    protected void registerDialogAttributes() {
        super.registerDialogAttributes();
        // VisDialog children:
        addAttributeProcessor(new OnResultLmlAttribute(), "result", "onResult", "onDialogResult");
    }

    @Override
    protected void registerSplitPaneAttributes() {
        // VisSplitPane:
        addAttributeProcessor(new MaxSplitLmlAttribute(), "max", "maxSplit", "maxSplitAmount");
        addAttributeProcessor(new MinSplitLmlAttribute(), "min", "minSplit", "minSplitAmount");
        addAttributeProcessor(new SplitAmountLmlAttribute(), "split", "splitAmount", "value");
    }

    @Override
    protected void registerTableAttributes() {
        super.registerTableAttributes();
        // Table:
        addAttributeProcessor(new UseCellDefaultsLmlAttribute(), "useCellDefaults", "useVisDefaults",
                "useSpacingDefaults", "visDefaults");
    }

    @Override
    protected void registerTextFieldAttributes() {
        // VisTextField:
        addAttributeProcessor(new BlinkTimeLmlAttribute(), "blink", "blinkTime");
        addAttributeProcessor(new CursorLmlAttribute(), "cursor", "cursorPos", "cursorPosition");
        addAttributeProcessor(new InputAlignLmlAttribute(), "textAlign", "inputAlign", "textAlignment");
        addAttributeProcessor(new MaxLengthLmlAttribute(), "max", "maxLength");
        addAttributeProcessor(new MessageLmlAttribute(), "message", "messageText");
        addAttributeProcessor(new PasswordCharacterLmlAttribute(), "passwordCharacter", "passwordChar", "passChar",
                "passCharacter");
        addAttributeProcessor(new PasswordModeLmlAttribute(), "passwordMode", "password", "passMode", "pass");
        addAttributeProcessor(new SelectAllLmlAttribute(), "selectAll");
        // VisTextArea:
        addAttributeProcessor(new PrefRowsLmlAttribute(), "prefRows", "prefRowsAmount");
    }

    @Override
    protected void registerWindowAttributes() {
        super.registerWindowAttributes();
        // VisWindow:
        addAttributeProcessor(new AddCloseButtonLmlAttribute(), "closeButton", "addCloseButton");
        addAttributeProcessor(new CloseOnEscapeLmlAttribute(), "closeOnEscape");
    }

    // Unique Vis actors attributes:

    /** CollapsibleWidget attributes. */
    protected void registerCollapsibleWidgetAttributes() {
        addAttributeProcessor(new CollapsedLmlAttribute(), "collapse", "collapsed");
    }

    /** NumberSelector attributes */
    protected void registerNumberSelectorAttributes() {
        addAttributeProcessor(new PrecisionLmlAttribute(), "precision");
        addAttributeProcessor(new ProgrammaticChangeEventsLmlAttribute(), "programmaticChangeEvents");
    }

    /** VisValidatableTextField attributes. */
    protected void registerValidatableTextFieldAttributes() {
        addAttributeProcessor(new RestoreLastValidLmlAttribute(), "restore", "restoreLastValid");
        addAttributeProcessor(new ValidationEnabledLmlAttribute(), "enabled", "validate", "validationEnabled");
    }

    /** InputValidator implementations' attributes. */
    protected void registerValidatorAttributes() {
        // CustomValidator:
        addAttributeProcessor(new CustomValidatorLmlAttribute(), "validator", "validate", "method", "action", "check");
        // FormInputValidator:
        addAttributeProcessor(new ErrorMessageLmlAttribute(), "error", "errorMsg", "errorMessage", "formError");
        addAttributeProcessor(new HideOnEmptyInputLmlAttribute(), "hideOnEmpty", "hideErrorOnEmpty");
        // GreaterThanValidator:
        addAttributeProcessor(new GreaterOrEqualLmlAttribute(), "orEqual", "allowEqual", "greaterOrEqual");
        addAttributeProcessor(new GreaterThanLmlAttribute(), "value", "min", "greaterThan");
        // LesserThanValidator:
        addAttributeProcessor(new LesserOrEqualLmlAttribute(), "orEqual", "allowEqual", "lesserOrEqual");
        addAttributeProcessor(new LesserThanLmlAttribute(), "value", "max", "lesserThan");
        // FormValidator children:
        addAttributeProcessor(new DisableOnFormErrorLmlAttribute(), "disableOnError", "disableOnFormError",
                "formDisable");
        addAttributeProcessor(new ErrorMessageLabelLmlAttribute(), "errorMessage", "errorLabel", "errorMsgLabel",
                "errorMessageLabel");
    }
}
