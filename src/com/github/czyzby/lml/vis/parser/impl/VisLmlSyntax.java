package com.github.czyzby.lml.vis.parser.impl;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.impl.DefaultLmlSyntax;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ActorLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ButtonGroupLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ButtonLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ContainerLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.HorizontalGroupLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.StackLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.TooltipLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.TouchpadLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.VerticalGroupLmlTagProvider;
import com.github.czyzby.lml.util.LmlUserObject.StandardTableTarget;
import com.github.czyzby.lml.util.LmlUserObject.TableExtractor;
import com.github.czyzby.lml.vis.parser.impl.attribute.ColorPickerLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.FocusBorderEnabledLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.ResponsiveColorPickerLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.building.GroupTypeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.building.MenuItemImageLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.building.NumberSelectorNameLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.building.NumberSelectorPrecisionLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.building.ShowWindowBorderLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.button.ButtonImageLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.button.ImageButtonGenerateDisabledLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.button.TextButtonImageLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.collapsible.CollapsedLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.draggable.BlockInputLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.draggable.DragListenerLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.draggable.DraggedAlphaLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.draggable.DraggedFadingInterpolationLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.draggable.DraggedFadingTimeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.draggable.DraggedMovingInterpolationLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.draggable.InvisibleWhenDraggedLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.draggable.pane.AcceptForeignLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.draggable.pane.DragPaneListenerLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.draggable.pane.GroupIdLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.draggable.pane.MaxChildrenLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.grid.GridSpacingLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.grid.ItemHeightLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.grid.ItemSizeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.grid.ItemWidthLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.BlinkTimeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.CursorLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.DigitsOnlyLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.InputAlignLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.MaxLengthLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.MessageLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.PasswordCharacterLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.PasswordModeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.PrefRowsLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.RestoreLastValidLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.SelectAllLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.TextFieldFilterLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.TextFieldListenerLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.input.ValidationEnabledLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.linklabel.UrlLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.menu.MenuItemGenerateDisabledImageLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.menu.MenuItemShortcutLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.numberselector.ProgrammaticChangeEventsLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.numberselector.SelectorMaxLengthLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.picker.AllowAlphaEditLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.picker.CloseAfterPickingLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.picker.ColorPickerListenerLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.picker.ColorPickerResponsiveListenerLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.split.MaxSplitLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.split.MinSplitLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.split.SplitAmountLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.AttachDefaultTabListenerLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.OnAllTabsRemovalLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.OnTabRemoveLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.OnTabSwitchLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.TabDeselectLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.TabHidingActionLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.TabListenerLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.TabSelectedLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.TabShowingActionLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.tab.OnTabDisposeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.tab.OnTabHideLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.tab.OnTabSaveLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.tab.OnTabShowLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.tab.TabCloseableLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.tab.TabDirtyLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.tab.TabDisableLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.tab.TabSavableLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.tabbed.tab.TabTitleLmlAttribute;
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
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.form.FormSuccessMessageLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.form.RequireCheckedLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.validator.form.RequireUncheckedLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.window.AddCloseButtonLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.window.CloseOnEscapeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.window.OnResultLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.CollapsibleWidgetLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.ColorPickerLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.ColumnGroupLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.DragPaneLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.DraggableLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.FormValidatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.GridGroupLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.LinkLabelLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.MenuBarLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.MenuItemLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.MenuLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.MenuPopupLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.MenuSeparatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.NumberSelectorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.SeparatorLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.TabLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.TabbedPaneLmlTagProvider;
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

/** Replaces regular Scene2D actor tags with VisUI widgets. Supports the same core syntax - most tags from original LML
 * are either pointing to the same widgets or to VisUI equivalents, and all the attributes and macros you know from LML
 * are also supported. This syntax, however, adds extra tags and attributes of unique VisUI actors, that are simply
 * absent in regular Scene2D. See {@link #registerActorTags()} method source for all registered actor tags. Macro tags
 * are unchanged.
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
        addTagProvider(new ButtonGroupLmlTagProvider(), "buttonGroup", "buttonTable");
        addTagProvider(new ButtonLmlTagProvider(), "button");
        addTagProvider(new ContainerLmlTagProvider(), "container", "single");
        addTagProvider(new HorizontalGroupLmlTagProvider(), "horizontal", "horizontalGroup");
        addTagProvider(new StackLmlTagProvider(), "stack");
        addTagProvider(new TooltipLmlTagProvider(), "tooltip"); // VisTooltipLmlTagProvider supports Vis tooltips.
        addTagProvider(new TouchpadLmlTagProvider(), "touchpad", "touch");
        addTagProvider(new VerticalGroupLmlTagProvider(), "vertical", "verticalGroup");

        // Vis actor equivalents:
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
        addTagProvider(new ColorPickerLmlTagProvider(), "colorPicker");
        addTagProvider(new ColumnGroupLmlTagProvider(), "columnGroup");
        addTagProvider(new DraggableLmlTagProvider(), "drag", "draggable");
        addTagProvider(new DragPaneLmlTagProvider(), "dragPane");
        addTagProvider(new FormValidatorLmlTagProvider(), "form", "formValidator", "formTable");
        addTagProvider(new GridGroupLmlTagProvider(), "gridGroup", "grid");
        addTagProvider(new LinkLabelLmlTagProvider(), "linkLabel", "link");
        addTagProvider(new MenuBarLmlTagProvider(), "menuBar", "bar");
        addTagProvider(new MenuItemLmlTagProvider(), "menuItem", "item");
        addTagProvider(new MenuLmlTagProvider(), "menu");
        addTagProvider(new MenuPopupLmlTagProvider(), "popupMenu", "subMenu");
        addTagProvider(new MenuSeparatorLmlTagProvider(), "menuSeparator");
        addTagProvider(new NumberSelectorLmlTagProvider(), "numberSelector", "numSelector", "selector");
        addTagProvider(new SeparatorLmlTagProvider(), "separator");
        addTagProvider(new TabbedPaneLmlTagProvider(), "tabbedPane", "tabs");
        addTagProvider(new TabLmlTagProvider(), "tab");
        addTagProvider(new VisTooltipLmlTagProvider(), "visTooltip");
        addTagProvider(new VisValidatableTextFieldLmlTagProvider(), "validatable", "validatableTextField",
                "visValidatableTextField");

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
        registerColorPickerAttributes();
        registerDraggableAttributes();
        registerDragPaneAttributes();
        registerGridGroupAttributes();
        registerMenuAttributes();
        registerNumberSelectorAttributes();
        registerLinkLabelAttributes();
        registerTabbedPaneAttributes();
        registerValidatableTextFieldAttributes();
        registerValidatorAttributes();
    }

    // Extra common attributes:

    @Override
    protected void registerBuildingAttributes() {
        super.registerBuildingAttributes();
        // DragPaneLmlActorBuilder:
        addBuildingAttributeProcessor(new GroupTypeLmlAttribute(), "type");
        // VisWindowLmlActorBuilder:
        addBuildingAttributeProcessor(new ShowWindowBorderLmlAttribute(), "showBorder", "showWindowBorder");
        // NumberSelectorLmlActorBuilder:
        addBuildingAttributeProcessor(new NumberSelectorNameLmlAttribute(), "name");
        addBuildingAttributeProcessor(new NumberSelectorPrecisionLmlAttribute(), "precision");
        // MenuItemLmlActorBuilder:
        addBuildingAttributeProcessor(new MenuItemImageLmlAttribute(), "icon", "image", "drawable");
    }

    @Override
    protected void registerCommonAttributes() {
        super.registerCommonAttributes();
        // BorderOwner:
        addAttributeProcessor(new FocusBorderEnabledLmlAttribute(), "focusBorder", "focusBorderEnabled");
        // Actor (ColorPicker attachment):
        addAttributeProcessor(new ColorPickerLmlAttribute(), "colorPicker");
        addAttributeProcessor(new ResponsiveColorPickerLmlAttribute(), "responsiveColorPicker");
    }

    @Override
    protected void registerTooltipAttributes() {
        super.registerTooltipAttributes();
        // Tooltip (VisUI pre-LibGDX 1.6.5 implementation):
        addAttributeProcessor(new DelayLmlAttribute(), "delay", "appearDelay");
        addAttributeProcessor(new TooltipFadeTimeLmlAttribute(), "fadeTime", "fadingTime");
    }

    // Scene2D equivalents extra attributes:

    @Override
    protected void registerButtonAttributes() {
        super.registerButtonAttributes();
        // VisImageButton:
        addAttributeProcessor(new ButtonImageLmlAttribute(), "image", "icon");
        addAttributeProcessor(new ImageButtonGenerateDisabledLmlAttribute(), "generateDisabled",
                "generateDisabledImage");
        // VisImageTextButton:
        addAttributeProcessor(new TextButtonImageLmlAttribute(), "image", "icon");
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
        addAttributeProcessor(new DigitsOnlyLmlAttribute(), "digitsOnly", "numeric");
        addAttributeProcessor(new InputAlignLmlAttribute(), "textAlign", "inputAlign", "textAlignment");
        addAttributeProcessor(new MaxLengthLmlAttribute(), "max", "maxLength");
        addAttributeProcessor(new MessageLmlAttribute(), "message", "messageText");
        addAttributeProcessor(new PasswordCharacterLmlAttribute(), "passwordCharacter", "passwordChar", "passChar",
                "passCharacter");
        addAttributeProcessor(new PasswordModeLmlAttribute(), "passwordMode", "password", "passMode", "pass");
        addAttributeProcessor(new SelectAllLmlAttribute(), "selectAll");
        addAttributeProcessor(new TextFieldFilterLmlAttribute(), "filter", "textFilter", "textFieldFilter");
        addAttributeProcessor(new TextFieldListenerLmlAttribute(), "listener", "textListener", "textFieldListener");
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

    /** ColorPicker attributes. */
    protected void registerColorPickerAttributes() {
        addAttributeProcessor(new AllowAlphaEditLmlAttribute(), "allowAlphaEdit", "allowAlpha");
        addAttributeProcessor(new CloseAfterPickingLmlAttribute(), "closeAfterPickingFinished", "closeAfter");
        addAttributeProcessor(new ColorPickerListenerLmlAttribute(), "listener");
        addAttributeProcessor(new ColorPickerResponsiveListenerLmlAttribute(), "responsiveListener");
    }

    /** Draggable listener attributes. */
    protected void registerDraggableAttributes() {
        addAttributeProcessor(new BlockInputLmlAttribute(), "blockInput");
        addAttributeProcessor(new DraggedAlphaLmlAttribute(), "alpha");
        addAttributeProcessor(new DraggedFadingInterpolationLmlAttribute(), "fadingInterpolation");
        addAttributeProcessor(new DraggedFadingTimeLmlAttribute(), "fadingTime");
        addAttributeProcessor(new DraggedMovingInterpolationLmlAttribute(), "movingInterpolation");
        addAttributeProcessor(new DragListenerLmlAttribute(), "listener");
        addAttributeProcessor(new InvisibleWhenDraggedLmlAttribute(), "invisible", "invisibleWhenDragged");
    }

    /** DragPane attributes. */
    protected void registerDragPaneAttributes() {
        addAttributeProcessor(new AcceptForeignLmlAttribute(), "foreign", "acceptForeign");
        addAttributeProcessor(new DragPaneListenerLmlAttribute(), "listener");
        addAttributeProcessor(new GroupIdLmlAttribute(), "groupId");
        addAttributeProcessor(new MaxChildrenLmlAttribute(), "maxChildren");
    }

    /** GridGroup attributes. */
    protected void registerGridGroupAttributes() {
        addAttributeProcessor(new GridSpacingLmlAttribute(), "spacing");
        addAttributeProcessor(new ItemHeightLmlAttribute(), "itemHeight");
        addAttributeProcessor(new ItemSizeLmlAttribute(), "itemSize");
        addAttributeProcessor(new ItemWidthLmlAttribute(), "itemWidth");
    }

    /** Menu-related attributes. */
    protected void registerMenuAttributes() {
        // MenuItem:
        addAttributeProcessor(new MenuItemGenerateDisabledImageLmlAttribute(), "generateDisabled");
        addAttributeProcessor(new MenuItemShortcutLmlAttribute(), "shortcut");
    }

    /** NumberSelector attributes. */
    protected void registerNumberSelectorAttributes() {
        addAttributeProcessor(new ProgrammaticChangeEventsLmlAttribute(), "programmaticChangeEvents");
        addAttributeProcessor(new SelectorMaxLengthLmlAttribute(), "maxLength");
    }

    /** LinkLabel attributes. */
    protected void registerLinkLabelAttributes() {
        addAttributeProcessor(new UrlLmlAttribute(), "url", "href");
    }

    /** TabbedPane (and its tab children) attributes. */
    protected void registerTabbedPaneAttributes() {
        // TabbedPane (pane's main table with TabbedPane attached):
        addAttributeProcessor(new AttachDefaultTabListenerLmlAttribute(), "defaultListener", "attachDefaultListener");
        addAttributeProcessor(new OnAllTabsRemovalLmlAttribute(), "onAllRemoved", "onAllTabsRemoved", "onClear",
                "onTabsClear");
        addAttributeProcessor(new OnTabRemoveLmlAttribute(), "onRemove", "onTabRemove");
        addAttributeProcessor(new OnTabSwitchLmlAttribute(), "onSwitch", "onTabSwitch");
        addAttributeProcessor(new TabDeselectLmlAttribute(), "allowTabDeselect", "tabDeselect");
        addAttributeProcessor(new TabHidingActionLmlAttribute(), "tabHideAction");
        addAttributeProcessor(new TabListenerLmlAttribute(), "tabListener", "tabbedPaneListener");
        addAttributeProcessor(new TabSelectedLmlAttribute(), "selected", "selectedTab");
        addAttributeProcessor(new TabShowingActionLmlAttribute(), "tabShowAction");
        // Tab (VisTabTable):
        addAttributeProcessor(new OnTabDisposeLmlAttribute(), "onDispose", "onTabDispose", "onRemove", "onTabRemove");
        addAttributeProcessor(new OnTabHideLmlAttribute(), "onTabHide");
        addAttributeProcessor(new OnTabSaveLmlAttribute(), "onSave", "onTabSave");
        addAttributeProcessor(new OnTabShowLmlAttribute(), "onTabShow");
        addAttributeProcessor(new TabCloseableLmlAttribute(), "closeable", "closeableByUser");
        addAttributeProcessor(new TabDirtyLmlAttribute(), "dirty");
        addAttributeProcessor(new TabDisableLmlAttribute(), "disable", "disabled");
        addAttributeProcessor(new TabSavableLmlAttribute(), "savable");
        addAttributeProcessor(new TabTitleLmlAttribute(), "title", "name", "tabTitle", "tabName");
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
        // FormValidator (VisFormTable):
        addAttributeProcessor(new FormSuccessMessageLmlAttribute(), "success", "successMsg", "successMessage");
        // FormValidator children:
        addAttributeProcessor(new DisableOnFormErrorLmlAttribute(), "disableOnError", "disableOnFormError",
                "formDisable");
        addAttributeProcessor(new ErrorMessageLabelLmlAttribute(), "errorMessage", "errorLabel", "errorMsgLabel",
                "errorMessageLabel");
        addAttributeProcessor(new RequireCheckedLmlAttribute(), "requireChecked", "formChecked", "notCheckedError",
                "uncheckedError");
        addAttributeProcessor(new RequireUncheckedLmlAttribute(), "requireUnchecked", "requireNotChecked",
                "formUnchecked", "checkedError");
    }
}
