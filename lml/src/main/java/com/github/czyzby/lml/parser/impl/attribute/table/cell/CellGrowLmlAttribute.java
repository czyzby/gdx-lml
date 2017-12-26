package com.github.czyzby.lml.parser.impl.attribute.table.cell;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** See {@link Cell#grow()}. Mapped to "grow".
 *
 * @author MJ */
public class CellGrowLmlAttribute extends AbstractCellLmlAttribute {
    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Actor actor, final Cell<?> cell,
            final String rawAttributeData) {
        if (parser.parseBoolean(rawAttributeData, actor)) {
            cell.grow();
        } else {
            cell.expand(false, false);
            cell.fill(false, false);
        }
    }
}
