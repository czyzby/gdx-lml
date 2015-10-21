package com.github.czyzby.lml.vis.parser.impl;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.impl.DefaultLmlSyntax;
import com.github.czyzby.lml.util.LmlUserObject.StandardTableTarget;
import com.github.czyzby.lml.util.LmlUserObject.TableExtractor;
import com.github.czyzby.lml.vis.parser.impl.attribute.building.ShowWindowBorderLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.button.ImageButtonGenerateDisabledLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.button.TextButtonFocusBorderEnabledLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.window.AddCloseButtonLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.window.CloseOnEscapeLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.attribute.window.OnResultLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisDialogLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisImageButtonLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisImageTextButtonLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisLabelLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisTextButtonLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisWindowLmlTagProvider;
import com.kotcrab.vis.ui.widget.VisDialog;

/** Replaces regular Scene2D actor tags with Vis UI widgets. Supports the same core syntax (operators).
 *
 * @author MJ */
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
        addTagProvider(new VisDialogLmlTagProvider(), "dialog", "visDialog", "popup");
        addTagProvider(new VisImageButtonLmlTagProvider(), "imageButton", "visImageButton");
        addTagProvider(new VisImageTextButtonLmlTagProvider(), "imageTextButton", "visImageTextButton");
        addTagProvider(new VisLabelLmlTagProvider(), "label", "visLabel", "text", "txt", "li");
        addTagProvider(new VisTextButtonLmlTagProvider(), "textButton", "visTextButton", "a");
        addTagProvider(new VisWindowLmlTagProvider(), "window", "visWindow");
        // TODO register other Vis tags
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        registerVisAttributes();
    }

    /** Registers attributes of Vis UI-specific actors. */
    protected void registerVisAttributes() {
        // TODO register attributes of Vis unique actors
    }

    @Override
    protected void registerButtonAttributes() {
        super.registerButtonAttributes();
        // VisTextButton:
        addAttributeProcessor(new TextButtonFocusBorderEnabledLmlAttribute(), "focusBorder", "focusBorderEnabled");
        // VisImageButton:
        addAttributeProcessor(new ImageButtonGenerateDisabledLmlAttribute(), "generateDisabled",
                "generateDisabledImage");
    }

    @Override
    protected void registerWindowAttributes() {
        super.registerWindowAttributes();
        // VisWindow:
        addAttributeProcessor(new AddCloseButtonLmlAttribute(), "closeButton", "addCloseButton");
        addAttributeProcessor(new CloseOnEscapeLmlAttribute(), "closeOnEscape");
    }

    @Override
    protected void registerDialogAttributes() {
        super.registerDialogAttributes();
        // VisDialog children:
        addAttributeProcessor(new OnResultLmlAttribute(), "result", "onResult", "onDialogResult");
    }

    @Override
    protected void registerBuildingAttributes() {
        super.registerBuildingAttributes();
        // VisWindow:
        addBuildingAttributeProcessor(new ShowWindowBorderLmlAttribute(), "showBorder", "showWindowBorder");
    }
}
