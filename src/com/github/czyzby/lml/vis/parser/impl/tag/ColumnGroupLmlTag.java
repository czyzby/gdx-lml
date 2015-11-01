package com.github.czyzby.lml.vis.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractGroupLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.layout.ColumnGroup;

/** Handles {@link ColumnGroup} widgets. Simplified {@link com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup}. Can have
 * any tag children; converts plain text to labels. Mapped to "columnGroup".
 *
 * @author MJ */
public class ColumnGroupLmlTag extends AbstractGroupLmlTag {
    public ColumnGroupLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Group getNewInstanceOfGroup(final LmlActorBuilder builder) {
        return new ColumnGroup();
    }
}
