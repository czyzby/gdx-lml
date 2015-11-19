package com.github.czyzby.lml.parser.impl;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;
import com.github.czyzby.lml.parser.LmlSyntax;
import com.github.czyzby.lml.parser.impl.attribute.ActionLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.ColorAlphaLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.ColorBlueLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.ColorGreenLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.ColorLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.ColorRedLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.DebugLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.DisabledLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.FillParentLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.IdLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.LayoutEnabledLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.MultilineLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.OnChangeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.OnClickLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.OnCloseLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.OnCreateLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.RotationLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.ScaleLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.ScaleXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.ScaleYLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.TooltipLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.TouchableLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.TreeNodeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.VisibleLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.XLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.YLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.HorizontalLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.OnResultInitialLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.RangeInitialValueLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.RangeMaxValueLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.RangeMinValueLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.RangeStepSizeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.SkinLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.StyleLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.TextLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.ToButtonTableLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.ToDialogTableLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.ToTitleTableLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.TooltipManagerLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.building.VerticalLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerAdjustPaddingLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerAlignLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerBackgroundLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerClipLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerFillLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerFillXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerFillYLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerHeightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerMaxHeightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerMaxSizeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerMaxWidthLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerMinHeightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerMinSizeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerMinWidthLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerPrefHeightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerPrefSizeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerPrefWidthLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerRoundLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerSizeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerWidthLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.DebugRecursivelyLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.HorizontalGroupAlignmentLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.HorizontalGroupFillLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.HorizontalGroupPaddingBottomLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.HorizontalGroupPaddingLeftLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.HorizontalGroupPaddingLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.HorizontalGroupPaddingRightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.HorizontalGroupPaddingTopLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.HorizontalGroupReverseLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.HorizontalGroupSpacingLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.TransformLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.VerticalGroupAlignmentLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.VerticalGroupFillLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.VerticalGroupPaddingBottomLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.VerticalGroupPaddingLeftLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.VerticalGroupPaddingLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.VerticalGroupPaddingRightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.VerticalGroupPaddingTopLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.VerticalGroupReverseLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.VerticalGroupSpacingLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.button.MaxCheckCountLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.button.MinCheckCountLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.group.button.UncheckLastLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.image.ImageAlignmentLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.image.ScalingLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.BlinkTimeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.CursorLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.DigitsOnlyLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.InputAlignLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.MaxLengthLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.MessageLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.PasswordCharacterLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.PasswordModeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.PrefRowsLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.SelectAllLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.TextFieldFilterLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.input.TextFieldListenerLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.label.EllipsisLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.label.LabelAlignmentLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.label.LineAlignmentLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.label.TextAlignmentLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.label.WrapLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.list.MultipleLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.list.RangeSelectLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.list.RequiredLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.list.SelectedLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.list.SelectionDisabledLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.list.ToggleLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.progress.AnimateDurationLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.progress.OnCompleteLmlAtrribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollBarsOnTopLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollBarsPositionsLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollCancelTouchFocusLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollClampLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollDisabledLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollDisabledXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollDisabledYLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollFadeBarsLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollFadeBarsSetupLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollFlickLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollFlickTapSquareSizeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollFlingTimeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollForceLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollForceXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollForceYLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollOverscrollLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollOverscrollSetupLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollOverscrollXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollOverscrollYLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollPercentLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollPercentXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollPercentYLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollSmoothLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollVariableSizeKnobsLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollVelocityLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollVelocityXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.scroll.ScrollVelocityYLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.select.SelectBoxSelectedLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.split.MaxSplitLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.split.MinSplitLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.split.SplitAmountLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.OneColumnLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.TableAlignLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.TableBackgroundLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.TablePadBottomLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.TablePadLeftLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.TablePadLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.TablePadRightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.TablePadTopLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.TableRoundLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.button.ButtonImageLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.button.CheckedLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.button.TextButtonImageLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.AbstractCellLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellAlignLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellColspanLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellExpandLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellExpandXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellExpandYLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellFillLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellFillXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellFillYLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellGrowLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellGrowXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellGrowYLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellHeightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellMaxHeightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellMaxSizeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellMaxWidthLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellMinHeightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellMinSizeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellMinWidthLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellPadBottomLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellPadLeftLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellPadLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellPadRightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellPadTopLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellPrefHeightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellPrefSizeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellPrefWidthLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellSizeLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellSpaceBottomLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellSpaceLeftLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellSpaceLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellSpaceRightLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellSpaceTopLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellUniformLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellUniformXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellUniformYLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.CellWidthLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.RowLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.cell.TableCellDefaultsLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.dialog.OnResultLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.tooltip.AlwaysLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.tooltip.InstantLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.window.KeepWithinStageLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.window.ModalLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.window.MovableLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.window.ResizeBorderLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.window.ResizeableLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.window.TitleAlignmentLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.table.window.TitleLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.touchpad.DeadZoneLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.touchpad.ResetOnTouchUpLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.tree.IconSpacingLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.tree.TreePaddingLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.tree.YSpacingLmlAttribute;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ActorLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ButtonGroupLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ButtonLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.CheckBoxLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ContainerLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.DialogLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.HorizontalGroupLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ImageButtonLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ImageLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ImageTextButtonLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.LabelLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ListLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ProgressBarLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.ScrollPaneLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.SelectBoxLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.SliderLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.SplitPaneLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.StackLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.TableLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.TextAreaLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.TextButtonLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.TextFieldLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.TooltipLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.TouchpadLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.TreeLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.VerticalGroupLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.actor.provider.WindowLmlTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.ActorLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.AnyNotNullLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.ArgumentReplacementLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.AssignLmlMarcoTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.CalculationLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.CommentLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.ConditionalLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.EvaluateLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.ExceptionLmlMactoTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.ForEachLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.ImportAbsoluteLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.ImportClasspathLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.ImportExternallLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.ImportInternalLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.ImportLocalLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.LoggerDebugLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.LoggerErrorLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.LoggerInfoLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.LoopLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.MetaLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.NestedForEachLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.NewAttributeLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.NewTagLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.NullCheckLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.TableColumnLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.TableRowLmlMacroTagProvider;
import com.github.czyzby.lml.parser.impl.tag.macro.provider.WhileLmlMacroTagProvider;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlBuildingAttribute;
import com.github.czyzby.lml.parser.tag.LmlTagProvider;
import com.github.czyzby.lml.util.collection.IgnoreCaseStringMap;

/** Represents default LML syntax. This class can be overridden to change some parts of LML syntax; note that core LML
 * syntax structures change would basically require another parser implementation, but if all you need is to change
 * parenthesis type here and there, this can be easily achieved.
 *
 * @author MJ */
public class DefaultLmlSyntax implements LmlSyntax {
    /** Default syntax object instance. Since tag providers are usually stateless and immutable, this instance can be
     * shared among multiple LML parsers. However, macro tag providers can be created at runtime by using custom macros
     * created directly in LML templates. Note that because of this there MIGHT be collisions if multiple templates
     * define custom macros with the same name, so use concurrently with care. */
    public static final LmlSyntax INSTANCE = new DefaultLmlSyntax();

    /** Key: tag name (ignoring case); value: tag provider. */
    private final ObjectMap<String, LmlTagProvider> tagProviders = new IgnoreCaseStringMap<LmlTagProvider>();
    /** Key: tag name (ignoring case); value: macro tag provider. */
    private final ObjectMap<String, LmlTagProvider> macroTagProviders = new IgnoreCaseStringMap<LmlTagProvider>();
    /** Key: class of actor; value: map with attributes assigned to the widget (key: attribute name, ignoring case). */
    private final ObjectMap<Class<?>, ObjectMap<String, LmlAttribute<?>>> attributeProcessors = getLazyMapOfIgnoreCaseMaps();
    /** Key: class of actor builder; value: map with building attributes assigned to the builder (key: attribute name,
     * ignoring case, value: processor). */
    private final ObjectMap<Class<?>, ObjectMap<String, LmlBuildingAttribute<?>>> buildingAttributeProcessors = getLazyMapOfIgnoreCaseMaps();

    /** Constructs a new instance of default syntax, with default tag and attributes registered. */
    public DefaultLmlSyntax() {
        registerTags();
        registerAttributes();
    }

    /** @return a new instance of object map storing maps with keys as values, ignoring their case. Utility provider. */
    private static <Key, Value> ObjectMap<Key, ObjectMap<String, Value>> getLazyMapOfIgnoreCaseMaps() {
        // This map returns a new IgnoreCaseStringMap on each get(Key) call if there is no map assigned to the passed
        // key. This is very convenient for maps of collections, as you do not have to go through the whole lazy
        // initiation process.
        return LazyObjectMap.newMap(new ObjectProvider<ObjectMap<String, Value>>() {
            @Override
            public ObjectMap<String, Value> provide() {
                return new IgnoreCaseStringMap<Value>();
            }
        });
    }

    /** Warning: invoked by the constructor. Registers known default tags. Since providers registration might override
     * previous settings, this method - if overridden - should call super BEFORE registering new tags (or replacing the
     * old ones). */
    protected void registerTags() {
        registerActorTags();
        registerMacroTags();
    }

    /** Registers actor-based tags that create widgets.
     *
     * @see #registerTags() */
    protected void registerActorTags() {
        addTagProvider(new ActorLmlTagProvider(), "actor", "group", "empty", "mock", "blank", "placeholder");
        addTagProvider(new ButtonGroupLmlTagProvider(), "buttonGroup", "buttonTable");
        addTagProvider(new ButtonLmlTagProvider(), "button");
        addTagProvider(new CheckBoxLmlTagProvider(), "checkBox", "check");
        addTagProvider(new ContainerLmlTagProvider(), "container", "single");
        addTagProvider(new DialogLmlTagProvider(), "dialog", "popup");
        addTagProvider(new HorizontalGroupLmlTagProvider(), "horizontal", "horizontalGroup");
        addTagProvider(new ImageButtonLmlTagProvider(), "imageButton");
        addTagProvider(new ImageLmlTagProvider(), "image", "img", "icon");
        addTagProvider(new ImageTextButtonLmlTagProvider(), "imageTextButton");
        addTagProvider(new LabelLmlTagProvider(), "label", "text", "txt", "li");
        addTagProvider(new ListLmlTagProvider(), "list", "ul");
        addTagProvider(new ProgressBarLmlTagProvider(), "progressBar", "progress", "loadingBar", "loading");
        addTagProvider(new ScrollPaneLmlTagProvider(), "scrollPane", "scroll", "scrollable");
        addTagProvider(new SelectBoxLmlTagProvider(), "selectBox", "select");
        addTagProvider(new SliderLmlTagProvider(), "slider");
        addTagProvider(new SplitPaneLmlTagProvider(), "splitPane", "split", "splitable");
        addTagProvider(new StackLmlTagProvider(), "stack");
        addTagProvider(new TableLmlTagProvider(), "table", "div", "td", "th", "tr");
        addTagProvider(new TextAreaLmlTagProvider(), "textArea", "inputArea", "multilineInput");
        addTagProvider(new TextButtonLmlTagProvider(), "textButton", "a");
        addTagProvider(new TextFieldLmlTagProvider(), "textField", "input", "textInput");
        addTagProvider(new TooltipLmlTagProvider(), "tooltip");
        addTagProvider(new TouchpadLmlTagProvider(), "touchpad", "touch");
        addTagProvider(new TreeLmlTagProvider(), "tree", "root");
        addTagProvider(new VerticalGroupLmlTagProvider(), "vertical", "verticalGroup");
        addTagProvider(new WindowLmlTagProvider(), "window");
    }

    /** Registers macro tags that manipulate templates' structures.
     *
     * @see #registerTags() */
    protected void registerMacroTags() {
        addMacroTagProvider(new ActorLmlMacroTagProvider(), "actor", "widget");
        addMacroTagProvider(new AnyNotNullLmlMacroTagProvider(), "anyNotNull", "any", "anyExists", "anyPresent");
        addMacroTagProvider(new ArgumentReplacementLmlMacroTagProvider(), "replace", "replaceArguments", "replaceArgs",
                "argumentsReplace", "argsReplace", "noOp", "noOperation", "doNothing", "root");
        addMacroTagProvider(new AssignLmlMarcoTagProvider(), "assign", "var", "val", "toArgument");
        addMacroTagProvider(new CalculationLmlMacroTagProvider(), "calculate", "calculation", "equation", "calc");
        addMacroTagProvider(new CommentLmlMacroTagProvider(), "comment", "FIXME", "TODO", "/*");
        addMacroTagProvider(new ConditionalLmlMacroTagProvider(), "if", "test", "check", "try", "verify", "inspect",
                "validate", "onCondition", "condition", "conditional");
        addMacroTagProvider(new EvaluateLmlMacroTagProvider(), "eval", "evaluate", "invoke", "invokeAndAssign",
                "evaluateAndAssign");
        addMacroTagProvider(new ExceptionLmlMactoTagProvider(), "exception", "throw", "throwException", "error",
                "throwError", "system.exit");
        addMacroTagProvider(new ForEachLmlMacroTagProvider(), "forEach", "for", "each", "iterate", "iterateOver");
        addMacroTagProvider(new ImportAbsoluteLmlMacroTagProvider(), "absoluteImport", "absoluteInclude",
                "absoluteRequire", "absoluteTemplate");
        addMacroTagProvider(new ImportClasspathLmlMacroTagProvider(), "classpathImport", "classpathInclude",
                "classpathRequire", "classpathTemplate");
        addMacroTagProvider(new ImportExternallLmlMacroTagProvider(), "externalImport", "externalInclude",
                "externalRequire", "externalTemplate");
        addMacroTagProvider(new ImportInternalLmlMacroTagProvider(), "import", "include", "require", "template",
                "internalImport", "internalInclude", "internalRequire", "internalTemplate");
        addMacroTagProvider(new ImportLocalLmlMacroTagProvider(), "localImport", "localInclude", "localRequire",
                "localTemplate");
        addMacroTagProvider(new LoggerDebugLmlMacroTagProvider(), "debug", "logDebug", "trace", "logTrace");
        addMacroTagProvider(new LoggerErrorLmlMacroTagProvider(), "logError");
        addMacroTagProvider(new LoggerInfoLmlMacroTagProvider(), "log", "logInfo", "info");
        addMacroTagProvider(new LoopLmlMacroTagProvider(), "loop", "times", "repeat");
        addMacroTagProvider(new MetaLmlMacroTagProvider(), "macro");
        addMacroTagProvider(new NestedForEachLmlMacroTagProvider(), "forEachNested", "nested", "nestedForEach",
                "eachNested", "nest", "nestedLoop");
        addMacroTagProvider(new NewAttributeLmlMacroTagProvider(), "newAttribute", "attribute", "createAttribute",
                "newProperty");
        addMacroTagProvider(new NewTagLmlMacroTagProvider(), "newTag", "newActor", "tag", "createTag");
        addMacroTagProvider(new NullCheckLmlMacroTagProvider(), "notNull", "ifNotNull", "allNotNull", "ifPresent",
                "exists", "ifExists", "nullCheck", "ifTrue");
        addMacroTagProvider(new TableColumnLmlMacroTagProvider(), "column", "tableColumn", "columnDefaults");
        addMacroTagProvider(new TableRowLmlMacroTagProvider(), "row", "addRow", "newRow", "nextLine", "nextRow", "tr",
                "tableRow", "rowDefaults");
        addMacroTagProvider(new WhileLmlMacroTagProvider(), "while", "whileTrue", "repeatWhile", "until", "untilTrue");
    }

    /** Warning: invoked by the constructor. Registers known default attributes. Since processors registration might
     * override previous settings, this method - if overridden - should call super BEFORE registering new attributes (or
     * replacing the old ones). */
    protected void registerAttributes() {
        // Building attributes. Parsed before actor creation.
        registerBuildingAttributes();

        // Regular attributes. Parsed after the actor is constructed.
        registerCommonAttributes();
        registerButtonAttributes();
        registerButtonGroupAttributes();
        registerContainerAttributes();
        registerDialogAttributes();
        registerHorizontalGroupAttributes();
        registerImageAttributes();
        registerLabelAttributes();
        registerListAttributes();
        registerProgressBarAttributes();
        registerScrollBarAttributes();
        registerSplitPaneAttributes();
        registerSelectBoxAttributes();
        registerTableAttributes();
        registerTextFieldAttributes();
        registerTooltipAttributes();
        registerTouchpadAttributes();
        registerTreeAttributes();
        registerVerticalGroupAttributes();
        registerWindowAttributes();
    }

    /** Attributes used during widget creation. */
    protected void registerBuildingAttributes() {
        // Default LmlActorBuilder:
        addBuildingAttributeProcessor(new SkinLmlAttribute(), "skin");
        addBuildingAttributeProcessor(new StyleLmlAttribute(), "style", "class");
        addBuildingAttributeProcessor(new OnResultInitialLmlAttribute(), "result", "onResult", "onDialogResult");
        addBuildingAttributeProcessor(new ToButtonTableLmlAttribute(), "toButtonTable");
        addBuildingAttributeProcessor(new ToDialogTableLmlAttribute(), "toDialogTable", "addDirectlyToTable");
        addBuildingAttributeProcessor(new ToTitleTableLmlAttribute(), "toTitleTable");
        // Text:
        addBuildingAttributeProcessor(new TextLmlAttribute(), "text", "txt", "value");
        // Aligned:
        addBuildingAttributeProcessor(new HorizontalLmlAttribute(), "horizontal");
        addBuildingAttributeProcessor(new VerticalLmlAttribute(), "vertical");
        // FloatRange:
        addBuildingAttributeProcessor(new RangeInitialValueLmlAttribute(), "value");
        addBuildingAttributeProcessor(new RangeMaxValueLmlAttribute(), "max");
        addBuildingAttributeProcessor(new RangeMinValueLmlAttribute(), "min");
        addBuildingAttributeProcessor(new RangeStepSizeLmlAttribute(), "stepSize", "step");
        // Tooltip:
        addBuildingAttributeProcessor(new TooltipManagerLmlAttribute(), "tooltipManager");
    }

    /** Attributes applied to all actors. */
    protected void registerCommonAttributes() {
        addAttributeProcessor(new ActionLmlAttribute(), "action", "act", "initialAction", "initialAct", "onShow");
        addAttributeProcessor(new ColorAlphaLmlAttribute(), "alpha", "a"); // Actor
        addAttributeProcessor(new ColorBlueLmlAttribute(), "blue", "b");
        addAttributeProcessor(new ColorGreenLmlAttribute(), "green", "g");
        addAttributeProcessor(new ColorLmlAttribute(), "color");
        addAttributeProcessor(new ColorRedLmlAttribute(), "red", "r");
        addAttributeProcessor(new DebugLmlAttribute(), "debug");
        addAttributeProcessor(new IdLmlAttribute(), "id");
        addAttributeProcessor(new MultilineLmlAttribute(), "multiline");
        addAttributeProcessor(new OnChangeLmlAttribute(), "onChange", "change");
        addAttributeProcessor(new OnClickLmlAttribute(), "onClick", "click");
        addAttributeProcessor(new OnCloseLmlAttribute(), "onClose", "close", "onTagClose", "tagClose");
        addAttributeProcessor(new OnCreateLmlAttribute(), "onCreate", "create", "onInit", "init");
        addAttributeProcessor(new RotationLmlAttribute(), "rotation", "rotate", "angle", "degrees");
        addAttributeProcessor(new ScaleLmlAttribute(), "scale");
        addAttributeProcessor(new ScaleXLmlAttribute(), "scaleX");
        addAttributeProcessor(new ScaleYLmlAttribute(), "scaleY");
        addAttributeProcessor(new TooltipLmlAttribute(), "tooltip");
        addAttributeProcessor(new TouchableLmlAttribute(), "touchable");
        addAttributeProcessor(new TreeNodeLmlAttribute(), "node", "treeNode");
        addAttributeProcessor(new VisibleLmlAttribute(), "visible");
        addAttributeProcessor(new XLmlAttribute(), "x", "positionX", "posX", "xPos", "xPosition");
        addAttributeProcessor(new YLmlAttribute(), "y", "positionY", "posY", "yPos", "yPosition");

        addAttributeProcessor(new TransformLmlAttribute(), "transform"); // Group
        addAttributeProcessor(new DebugRecursivelyLmlAttribute(), "debugRecursively", "recursiveDebug",
                "debugChildren");

        // Since Layout is an interface and interfaces listing is not supported on GWT, widgets can be mapped only to
        // their superclasses. This requires Layout-based attributes to be registered to a class that can apply to any
        // Layout-implementing widget.
        addAttributeProcessor(new FillParentLmlAttribute(), "fillParent"); // Layout
        addAttributeProcessor(new LayoutEnabledLmlAttribute(), "layout", "layoutEnabled");

        // Same goes for Disableable. Fails if the widget does not implement the interface.
        addAttributeProcessor(new DisabledLmlAttribute(), "disabled", "disable", "isDisabled"); // Disableable
    }

    /** Button widget attributes. */
    protected void registerButtonAttributes() {
        addAttributeProcessor(new ButtonImageLmlAttribute(), "image", "icon"); // ImageButton
        addAttributeProcessor(new CheckedLmlAttribute(), "checked", "isChecked"); // Button
        addAttributeProcessor(new TextButtonImageLmlAttribute(), "image", "icon"); // ImageTextButton
    }

    /** ButtonGroup (ButtonTable) attributes. */
    protected void registerButtonGroupAttributes() {
        addAttributeProcessor(new MaxCheckCountLmlAttribute(), "max", "maxCheckCount");
        addAttributeProcessor(new MinCheckCountLmlAttribute(), "min", "minCheckCount");
        addAttributeProcessor(new UncheckLastLmlAttribute(), "uncheckLast");
    }

    /** Container widget attributes. */
    protected void registerContainerAttributes() {
        addAttributeProcessor(new ContainerAdjustPaddingLmlAttribute(), "adjustPadding");
        addAttributeProcessor(new ContainerAlignLmlAttribute(), "align");
        addAttributeProcessor(new ContainerBackgroundLmlAttribute(), "bg", "background");
        addAttributeProcessor(new ContainerClipLmlAttribute(), "clip");
        addAttributeProcessor(new ContainerFillLmlAttribute(), "fill");
        addAttributeProcessor(new ContainerFillXLmlAttribute(), "fillX");
        addAttributeProcessor(new ContainerFillYLmlAttribute(), "fillY");
        addAttributeProcessor(new ContainerHeightLmlAttribute(), "height");
        addAttributeProcessor(new ContainerMaxHeightLmlAttribute(), "maxHeight");
        addAttributeProcessor(new ContainerMaxSizeLmlAttribute(), "maxSize");
        addAttributeProcessor(new ContainerMaxWidthLmlAttribute(), "maxWidth");
        addAttributeProcessor(new ContainerMinHeightLmlAttribute(), "minHeight");
        addAttributeProcessor(new ContainerMinSizeLmlAttribute(), "minSize");
        addAttributeProcessor(new ContainerMinWidthLmlAttribute(), "minWidth");
        addAttributeProcessor(new ContainerPrefHeightLmlAttribute(), "prefHeight");
        addAttributeProcessor(new ContainerPrefSizeLmlAttribute(), "prefSize");
        addAttributeProcessor(new ContainerPrefWidthLmlAttribute(), "prefWidth");
        addAttributeProcessor(new ContainerRoundLmlAttribute(), "round");
        addAttributeProcessor(new ContainerSizeLmlAttribute(), "size");
        addAttributeProcessor(new ContainerWidthLmlAttribute(), "width");
    }

    /** Dialog-related attributes. */
    protected void registerDialogAttributes() {
        // Dialog children attributes:
        addAttributeProcessor(new OnResultLmlAttribute(), "result", "onResult", "onDialogResult");
    }

    /** HorizontalGroup widget attributes. */
    protected void registerHorizontalGroupAttributes() {
        addAttributeProcessor(new HorizontalGroupAlignmentLmlAttribute(), "groupAlign");
        addAttributeProcessor(new HorizontalGroupFillLmlAttribute(), "groupFill");
        addAttributeProcessor(new HorizontalGroupPaddingBottomLmlAttribute(), "groupPadBottom");
        addAttributeProcessor(new HorizontalGroupPaddingLeftLmlAttribute(), "groupPadLeft");
        addAttributeProcessor(new HorizontalGroupPaddingLmlAttribute(), "groupPad", "padding");
        addAttributeProcessor(new HorizontalGroupPaddingRightLmlAttribute(), "groupPadRight");
        addAttributeProcessor(new HorizontalGroupPaddingTopLmlAttribute(), "groupPadTop");
        addAttributeProcessor(new HorizontalGroupReverseLmlAttribute(), "reverse");
        addAttributeProcessor(new HorizontalGroupSpacingLmlAttribute(), "groupSpace", "spacing");
    }

    /** Image widget attributes. */
    protected void registerImageAttributes() {
        addAttributeProcessor(new ImageAlignmentLmlAttribute(), "imageAlign", "imgAlign", "iconAlign");
        addAttributeProcessor(new ScalingLmlAttribute(), "scaling", "imageScaling", "iconScaling", "imgScaling");
    }

    /** Label widget attributes. */
    protected void registerLabelAttributes() {
        addAttributeProcessor(new EllipsisLmlAttribute(), "ellipsis");
        addAttributeProcessor(new LabelAlignmentLmlAttribute(), "labelAlign", "labelAlignment");
        addAttributeProcessor(new LineAlignmentLmlAttribute(), "lineAlign", "lineAlignment");
        addAttributeProcessor(new TextAlignmentLmlAttribute(), "textAlign", "textAlignment");
        addAttributeProcessor(new WrapLmlAttribute(), "wrap");
    }

    /** List widget attributes. */
    protected void registerListAttributes() {
        addAttributeProcessor(new MultipleLmlAttribute(), "multiple");
        addAttributeProcessor(new RangeSelectLmlAttribute(), "rangeSelect");
        addAttributeProcessor(new RequiredLmlAttribute(), "required");
        addAttributeProcessor(new SelectedLmlAttribute(), "selected", "select", "value");
        addAttributeProcessor(new SelectionDisabledLmlAttribute(), "disabled", "disable", "isDisabled");
        addAttributeProcessor(new ToggleLmlAttribute(), "toggle");
    }

    /** ProgressBar widget attributes. */
    protected void registerProgressBarAttributes() {
        addAttributeProcessor(new AnimateDurationLmlAttribute(), "animate", "animateDuration", "animation");
        addAttributeProcessor(new OnCompleteLmlAtrribute(), "onComplete", "complete");
    }

    /** ScrollBar widget attributes. */
    protected void registerScrollBarAttributes() {
        addAttributeProcessor(new ScrollBarsOnTopLmlAttribute(), "barsOnTop", "scrollbarsOnTop");
        addAttributeProcessor(new ScrollBarsPositionsLmlAttribute(), "barsPositions", "scrollBarsPositions");
        addAttributeProcessor(new ScrollCancelTouchFocusLmlAttribute(), "cancelTouchFocus");
        addAttributeProcessor(new ScrollClampLmlAttribute(), "clamp");
        addAttributeProcessor(new ScrollDisabledLmlAttribute(), "disable", "disabled", "scrollingDisabled");
        addAttributeProcessor(new ScrollDisabledXLmlAttribute(), "disableX", "disabledX", "scrollingDisabledX");
        addAttributeProcessor(new ScrollDisabledYLmlAttribute(), "disableY", "disabledY", "scrollingDisabledY");
        addAttributeProcessor(new ScrollFadeBarsLmlAttribute(), "fadeBars", "fadeScrollbars");
        addAttributeProcessor(new ScrollFadeBarsSetupLmlAttribute(), "setupFadeScrollBars");
        addAttributeProcessor(new ScrollFlickLmlAttribute(), "flick", "flickScroll");
        addAttributeProcessor(new ScrollFlickTapSquareSizeLmlAttribute(), "flickScrollTapSquareSize", "tapSquareSize");
        addAttributeProcessor(new ScrollFlingTimeLmlAttribute(), "flingTime");
        addAttributeProcessor(new ScrollForceLmlAttribute(), "force", "forceScroll");
        addAttributeProcessor(new ScrollForceXLmlAttribute(), "forceX", "forceScrollX");
        addAttributeProcessor(new ScrollForceYLmlAttribute(), "forceY", "forceScrollY");
        addAttributeProcessor(new ScrollOverscrollLmlAttribute(), "overscroll");
        addAttributeProcessor(new ScrollOverscrollSetupLmlAttribute(), "setupOverscroll");
        addAttributeProcessor(new ScrollOverscrollXLmlAttribute(), "overscrollX");
        addAttributeProcessor(new ScrollOverscrollYLmlAttribute(), "overscrollY");
        addAttributeProcessor(new ScrollPercentLmlAttribute(), "scrollPercent", "percent");
        addAttributeProcessor(new ScrollPercentXLmlAttribute(), "scrollPercentX", "percentX");
        addAttributeProcessor(new ScrollPercentYLmlAttribute(), "scrollPercentY", "percentY");
        addAttributeProcessor(new ScrollVariableSizeKnobsLmlAttribute(), "variableSizeKnobs");
        addAttributeProcessor(new ScrollSmoothLmlAttribute(), "smooth", "smoothScrolling");
        addAttributeProcessor(new ScrollVelocityLmlAttribute(), "velocity");
        addAttributeProcessor(new ScrollVelocityXLmlAttribute(), "velocityX");
        addAttributeProcessor(new ScrollVelocityYLmlAttribute(), "velocityY");
    }

    /** SelectBox widget attributes. */
    protected void registerSelectBoxAttributes() {
        addAttributeProcessor(new SelectBoxSelectedLmlAttribute(), "selected", "select", "value");
    }

    /** SplitPane widget attributes. */
    protected void registerSplitPaneAttributes() {
        addAttributeProcessor(new MaxSplitLmlAttribute(), "max", "maxSplit", "maxSplitAmount");
        addAttributeProcessor(new MinSplitLmlAttribute(), "min", "minSplit", "minSplitAmount");
        addAttributeProcessor(new SplitAmountLmlAttribute(), "split", "splitAmount", "value");
    }

    /** Table widget attributes. */
    protected void registerTableAttributes() {
        addAttributeProcessor(new OneColumnLmlAttribute(), "oneColumn");
        addAttributeProcessor(new TableAlignLmlAttribute(), "tableAlign");
        addAttributeProcessor(new TableBackgroundLmlAttribute(), "bg", "background");
        addAttributeProcessor(new TablePadBottomLmlAttribute(), "tablePadBottom");
        addAttributeProcessor(new TablePadLeftLmlAttribute(), "tablePadLeft");
        addAttributeProcessor(new TablePadLmlAttribute(), "tablePad");
        addAttributeProcessor(new TablePadRightLmlAttribute(), "tablePadRight");
        addAttributeProcessor(new TablePadTopLmlAttribute(), "tablePadTop");
        addAttributeProcessor(new TableRoundLmlAttribute(), "round");
        registerCellAttributes();
    }

    /** Attributes available to children tags of a Table parent. */
    protected void registerCellAttributes() {
        addCellAttributeProcessor(new CellAlignLmlAttribute(), "align");
        addCellAttributeProcessor(new CellColspanLmlAttribute(), "colspan");
        addCellAttributeProcessor(new CellExpandLmlAttribute(), "expand");
        addCellAttributeProcessor(new CellExpandXLmlAttribute(), "expandX");
        addCellAttributeProcessor(new CellExpandYLmlAttribute(), "expandY");
        addCellAttributeProcessor(new CellFillLmlAttribute(), "fill");
        addCellAttributeProcessor(new CellFillXLmlAttribute(), "fillX");
        addCellAttributeProcessor(new CellFillYLmlAttribute(), "fillY");
        addCellAttributeProcessor(new CellGrowLmlAttribute(), "grow");
        addCellAttributeProcessor(new CellGrowXLmlAttribute(), "growX");
        addCellAttributeProcessor(new CellGrowYLmlAttribute(), "growY");
        addCellAttributeProcessor(new CellHeightLmlAttribute(), "height");
        addCellAttributeProcessor(new CellMaxHeightLmlAttribute(), "maxHeight");
        addCellAttributeProcessor(new CellMaxSizeLmlAttribute(), "maxSize");
        addCellAttributeProcessor(new CellMaxWidthLmlAttribute(), "maxWidth");
        addCellAttributeProcessor(new CellMinHeightLmlAttribute(), "minHeight");
        addCellAttributeProcessor(new CellMinSizeLmlAttribute(), "minSize");
        addCellAttributeProcessor(new CellMinWidthLmlAttribute(), "minWidth");
        addCellAttributeProcessor(new CellPadBottomLmlAttribute(), "padBottom");
        addCellAttributeProcessor(new CellPadLeftLmlAttribute(), "padLeft");
        addCellAttributeProcessor(new CellPadLmlAttribute(), "pad");
        addCellAttributeProcessor(new CellPadRightLmlAttribute(), "padRight");
        addCellAttributeProcessor(new CellPadTopLmlAttribute(), "padTop");
        addCellAttributeProcessor(new CellPrefHeightLmlAttribute(), "prefHeight");
        addCellAttributeProcessor(new CellPrefSizeLmlAttribute(), "prefSize");
        addCellAttributeProcessor(new CellPrefWidthLmlAttribute(), "prefWidth");
        addCellAttributeProcessor(new CellSizeLmlAttribute(), "size");
        addCellAttributeProcessor(new CellSpaceBottomLmlAttribute(), "spaceBottom");
        addCellAttributeProcessor(new CellSpaceLeftLmlAttribute(), "spaceLeft");
        addCellAttributeProcessor(new CellSpaceLmlAttribute(), "space");
        addCellAttributeProcessor(new CellSpaceRightLmlAttribute(), "spaceRight");
        addCellAttributeProcessor(new CellSpaceTopLmlAttribute(), "spaceTop");
        addCellAttributeProcessor(new CellUniformLmlAttribute(), "uniform");
        addCellAttributeProcessor(new CellUniformXLmlAttribute(), "uniformX");
        addCellAttributeProcessor(new CellUniformYLmlAttribute(), "uniformY");
        addCellAttributeProcessor(new CellWidthLmlAttribute(), "width");
        addAttributeProcessor(new RowLmlAttribute(), "row"); // Row attribute is not available for default cell.
    }

    /** @param cellAttribute will be registered using {@link #addAttributeProcessor(LmlAttribute, String...)}.
     * @param names will be used to register cell attribute. Same names with "default" prefix will be used for Table's
     *            default cell. */
    protected void addCellAttributeProcessor(final AbstractCellLmlAttribute cellAttribute, final String... names) {
        addAttributeProcessor(cellAttribute, names);
        // Registering table's default cell attribute - using same names with "default" prefix:
        for (int index = 0, length = names.length; index < length; index++) {
            names[index] = "default" + names[index];
        }
        addAttributeProcessor(new TableCellDefaultsLmlAttribute(cellAttribute), names);
    }

    /** TextField widget attributes. */
    protected void registerTextFieldAttributes() {
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
        addAttributeProcessor(new PrefRowsLmlAttribute(), "prefRows", "prefRowsAmount"); // TextArea.
    }

    /** Tooltip listener attributes. */
    protected void registerTooltipAttributes() {
        addAttributeProcessor(new AlwaysLmlAttribute(), "always");
        addAttributeProcessor(new InstantLmlAttribute(), "instant");
    }

    /** Touchpad widget attributes. */
    protected void registerTouchpadAttributes() {
        addAttributeProcessor(new DeadZoneLmlAttribute(), "deadzone", "deadzoneRadius");
        addAttributeProcessor(new ResetOnTouchUpLmlAttribute(), "resetOnTouchUp");
    }

    /** Tree widget attributes. */
    protected void registerTreeAttributes() {
        addAttributeProcessor(new IconSpacingLmlAttribute(), "iconSpacing", "iconSpace");
        addAttributeProcessor(new TreePaddingLmlAttribute(), "padding", "treePad");
        addAttributeProcessor(new YSpacingLmlAttribute(), "ySpacing", "ySpace");
    }

    /** VerticalGroup widget attributes. */
    protected void registerVerticalGroupAttributes() {
        addAttributeProcessor(new VerticalGroupAlignmentLmlAttribute(), "groupAlign");
        addAttributeProcessor(new VerticalGroupFillLmlAttribute(), "groupFill");
        addAttributeProcessor(new VerticalGroupPaddingBottomLmlAttribute(), "groupPadBottom");
        addAttributeProcessor(new VerticalGroupPaddingLeftLmlAttribute(), "groupPadLeft");
        addAttributeProcessor(new VerticalGroupPaddingLmlAttribute(), "groupPad", "padding");
        addAttributeProcessor(new VerticalGroupPaddingRightLmlAttribute(), "groupPadRight");
        addAttributeProcessor(new VerticalGroupPaddingTopLmlAttribute(), "groupPadTop");
        addAttributeProcessor(new VerticalGroupReverseLmlAttribute(), "reverse");
        addAttributeProcessor(new VerticalGroupSpacingLmlAttribute(), "groupSpace", "spacing");
    }

    /** Window widget attributes. */
    protected void registerWindowAttributes() {
        addAttributeProcessor(new KeepWithinStageLmlAttribute(), "keepWithinStage", "keepWithin");
        addAttributeProcessor(new ModalLmlAttribute(), "modal");
        addAttributeProcessor(new MovableLmlAttribute(), "movable");
        addAttributeProcessor(new ResizeableLmlAttribute(), "resizeable", "resizable");
        addAttributeProcessor(new ResizeBorderLmlAttribute(), "resizeBorder", "border");
        addAttributeProcessor(new TitleAlignmentLmlAttribute(), "titleAlign", "titleAlignment");
        addAttributeProcessor(new TitleLmlAttribute(), "title");
    }

    @Override
    public char getTagOpening() {
        return '<';
    }

    @Override
    public char getTagClosing() {
        return '>';
    }

    @Override
    public char getClosedTagMarker() {
        return '/';
    }

    @Override
    public char getCommentOpening() {
        return '!';
    }

    @Override
    public char getSchemaCommentMarker() {
        return '?';
    }

    @Override
    public char getCommentClosing() {
        return '-';
    }

    @Override
    public char getArgumentOpening() {
        return '{';
    }

    @Override
    public char getArgumentClosing() {
        return '}';
    }

    @Override
    public char getMacroMarker() {
        return '@';
    }

    @Override
    public char getIdSeparatorMarker() {
        return '.';
    }

    @Override
    public char getPreferenceMarker() {
        return '#';
    }

    @Override
    public char getBundleLineMarker() {
        return '@';
    }

    @Override
    public char getBundleLineArgumentMarker() {
        return '|';
    }

    @Override
    public char getAttributeSeparator() {
        return '=';
    }

    @Override
    public char getMethodInvocationMarker() {
        return '$';
    }

    @Override
    public char getArrayElementSeparator() {
        return ';';
    }

    @Override
    public char getRangeArrayOpening() {
        return '[';
    }

    @Override
    public char getRangeArraySeparator() {
        return ',';
    }

    @Override
    public char getRangeArrayClosing() {
        return ']';
    }

    @Override
    public LmlTagProvider getTagProvider(final String tagName) {
        return tagProviders.get(tagName);
    }

    @Override
    public void addTagProvider(final LmlTagProvider provider, final String... supportedTagNames) {
        for (final String name : supportedTagNames) {
            tagProviders.put(name, provider);
        }
    }

    @Override
    public void removeTagProvider(final String tagName) {
        tagProviders.remove(tagName);
    }

    @Override
    public LmlTagProvider getMacroTagProvider(final String tagName) {
        return macroTagProviders.get(tagName);
    }

    @Override
    public void addMacroTagProvider(final LmlTagProvider provider, final String... supportedTagNames) {
        for (final String name : supportedTagNames) {
            macroTagProviders.put(name, provider);
        }
    }

    @Override
    public void removeMacroTagProvider(final String tagName) {
        macroTagProviders.remove(tagName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Actor> LmlAttribute<Actor> getAttributeProcessor(final Actor forActor, final String attributeName) {
        return (LmlAttribute<Actor>) getAttributeProcessor(forActor.getClass(), attributeName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Actor> LmlAttribute<Actor> getAttributeProcessor(final Class<Actor> forActorType,
            final String attributeName) {
        Class<?> actorClass = forActorType;
        while (actorClass != null) {
            if (attributeProcessors.containsKey(actorClass)) {
                final ObjectMap<String, LmlAttribute<?>> processors = attributeProcessors.get(actorClass);
                if (processors.containsKey(attributeName)) {
                    return (LmlAttribute<Actor>) processors.get(attributeName);
                }
            }
            actorClass = actorClass.getSuperclass();
        }
        return null;
    }

    @Override
    public <Actor> void addAttributeProcessor(final LmlAttribute<Actor> attributeProcessor, final String... names) {
        final ObjectMap<String, LmlAttribute<?>> processors = attributeProcessors
                .get(attributeProcessor.getHandledType());
        for (final String name : names) {
            processors.put(name, attributeProcessor);
        }
    }

    @Override
    public void removeAttributeProcessor(final String name, final Class<?> handledActorType) {
        attributeProcessors.get(handledActorType).remove(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Builder extends LmlActorBuilder> LmlBuildingAttribute<Builder> getBuildingAttributeProcessor(
            final Builder builder, final String attributeName) {
        Class<?> builderClass = builder.getClass();
        while (builderClass != null) {
            if (buildingAttributeProcessors.containsKey(builderClass)) {
                final ObjectMap<String, LmlBuildingAttribute<?>> processors = buildingAttributeProcessors
                        .get(builderClass);
                if (processors.containsKey(attributeName)) {
                    return (LmlBuildingAttribute<Builder>) processors.get(attributeName);
                }
            }
            builderClass = builderClass.getSuperclass();
        }
        return null;
    }

    @Override
    public <Builder extends LmlActorBuilder> void addBuildingAttributeProcessor(
            final LmlBuildingAttribute<Builder> buildingAttributeProcessor, final String... names) {
        final ObjectMap<String, LmlBuildingAttribute<?>> processors = buildingAttributeProcessors
                .get(buildingAttributeProcessor.getBuilderType());
        for (final String name : names) {
            processors.put(name, buildingAttributeProcessor);
        }
    }

    @Override
    public void removeBuildingAttributeProcessor(final String name, final Class<?> handledActorType) {
        buildingAttributeProcessors.get(handledActorType).remove(name);
    }
}
