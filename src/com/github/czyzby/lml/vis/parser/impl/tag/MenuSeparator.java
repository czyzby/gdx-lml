package com.github.czyzby.lml.vis.parser.impl.tag;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Handles {@link com.kotcrab.vis.ui.widget.Separator} widgets inside menu tags. Overrides default style name with a
 * custom one, designed for menus. Basically, this is a menu utility. Mapped to "menuSeparator".
 *
 * @author MJ */
public abstract class MenuSeparator extends AbstractActorLmlTag {// TODO extend separator tag instead
    public MenuSeparator(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected LmlActorBuilder getNewInstanceOfBuilder() {
        final LmlActorBuilder builder = new LmlActorBuilder();
        builder.setStyleName("menu"); // Default separator style name for menus.
        return builder;
    }
}
