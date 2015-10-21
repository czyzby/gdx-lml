package com.github.czyzby.lml.parser.impl.attribute.table;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUserObject.StandardTableTarget;
import com.github.czyzby.lml.util.LmlUtilities;

public class OneColumnLmlAttribute implements LmlAttribute<Table> {
    @Override
    public Class<Table> getHandledType() {
        return Table.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Table actor, final String rawAttributeData) {
        if (parser.parseBoolean(rawAttributeData, actor)) {
            // Extracting main table. It is usually the same actor, but some widgets (dialog) manage internal table:
            final Table target = StandardTableTarget.MAIN.extract(actor);
            // Setting as one column using unique data mechanism:
            LmlUtilities.getLmlUserObject(target).setData(Boolean.TRUE); // See LmlUtilities#isOneColumn(Table)
        }
    }
}
