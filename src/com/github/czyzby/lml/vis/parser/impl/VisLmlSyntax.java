package com.github.czyzby.lml.vis.parser.impl;

import com.github.czyzby.lml.parser.impl.DefaultLmlSyntax;
import com.github.czyzby.lml.vis.parser.impl.attribute.textbutton.TextButtonFocusBorderEnabledLmlAttribute;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisLabelLmlTagProvider;
import com.github.czyzby.lml.vis.parser.impl.tag.provider.VisTextButtonLmlTagProvider;

/** Replaces regular Scene2D actor tags with Vis UI widgets. Supports the same core syntax (operators).
 *
 * @author MJ */
public class VisLmlSyntax extends DefaultLmlSyntax {
    @Override
    protected void registerTags() {
        registerVisActorTags();
        registerMacroTags();
    }

    /** Registers all Vis UI actors' tags. */
    protected void registerVisActorTags() {
        addTagProvider(new VisLabelLmlTagProvider(), "label", "visLabel", "text", "txt", "li");
        addTagProvider(new VisTextButtonLmlTagProvider(), "textButton", "visTextButton", "a");
        // TODO register other Vis tags
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        registerVisAttributes();
    }

    /** Registers attributes of Vis UI-specific actors. */
    protected void registerVisAttributes() {
        // TODO register attributes
    }

    @Override
    protected void registerButtonAttributes() {
        super.registerButtonAttributes();
        // VisTextButton:
        addAttributeProcessor(new TextButtonFocusBorderEnabledLmlAttribute(), "focusBorder", "focusBorderEnabled");
    }
}
