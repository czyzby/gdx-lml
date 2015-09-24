package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public class WindowLmlParent extends TableLmlParent {
    public static final String TO_TITLE_TABLE_ATTRIBUTE = "TOTITLETABLE";

    public WindowLmlParent(final LmlTagData tagData, final Window actor, final LmlParent<?> parent,
            final LmlParser parser) {
        super(tagData, actor, parent, parser);
    }

    protected Window getActorAsWindow() {
        return (Window) actor;
    }

    @Override
    public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
        if (LmlAttributes.parseBoolean(child, parser, childTagData.getAttribute(TO_TITLE_TABLE_ATTRIBUTE))) {
            appendCellToTable(getActorAsWindow().getTitleTable(), child, childTagData, parser);
        } else {
            appendCellToTable(actor, child, childTagData, parser);
        }
    }
}
