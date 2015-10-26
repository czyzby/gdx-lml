package com.github.czyzby.lml.vis.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.vis.ui.reflected.VisFormTable;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;

/** Maintains a {@link SimpleFormValidator} through a specialized table widget: {@link VisFormTable}. As a parent, works
 * similarly to a table, although it adds all {@link com.kotcrab.vis.ui.widget.VisValidatableTextField} children to the
 * internally managed form. Mapped to "form", "formValidator", "formTable".
 *
 * @author MJ */
public class FormValidatorLmlTag extends VisTableLmlTag {
    public FormValidatorLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return new VisFormTable();
    }
}
