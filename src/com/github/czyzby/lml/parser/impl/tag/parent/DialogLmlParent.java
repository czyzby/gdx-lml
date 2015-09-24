package com.github.czyzby.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public class DialogLmlParent extends TableLmlParent {
    public static final String TO_TITLE_TABLE_ATTRIBUTE = "TOTITLETABLE";
    public static final String TO_BUTTON_TABLE_ATTRIBUTE = "TOBUTTONTABLE";
    public static final String TO_DIALOG_TABLE_ATTRIBUTE = "TODIALOGTABLE";
    public static final String ON_RESULT_ATTRIBUTE = "ONRESULT";

    public DialogLmlParent(final LmlTagData tagData, final Dialog actor, final LmlParent<?> parent,
            final LmlParser parser) {
        super(tagData, actor, parent, parser);
        final Cell<?> contentCell = actor.getCell(actor.getContentTable());
        contentCell.fill();
        contentCell.expand();
        final Cell<?> buttonCell = actor.getCell(actor.getButtonTable());
        buttonCell.fillX();
        buttonCell.expandX();
    }

    protected Dialog getActorAsDialog() {
        return (Dialog) actor;
    }

    @Override
    public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
        final boolean containsResult = childTagData.containsAttribute(ON_RESULT_ATTRIBUTE);

        if (LmlAttributes.parseBoolean(child, parser, childTagData.getAttribute(TO_BUTTON_TABLE_ATTRIBUTE))
                || containsResult) {
            appendCellToTable(getActorAsDialog().getButtonTable(), child, childTagData, parser);
        } else if (LmlAttributes.parseBoolean(child, parser, childTagData.getAttribute(TO_DIALOG_TABLE_ATTRIBUTE))) {
            appendCellToTable(actor, child, childTagData, parser);
        } else if (LmlAttributes.parseBoolean(child, parser, childTagData.getAttribute(TO_TITLE_TABLE_ATTRIBUTE))) {
            appendCellToTable(getActorAsDialog().getTitleTable(), child, childTagData, parser);
        } else {
            appendCellToTable(getActorAsDialog().getContentTable(), child, childTagData, parser);
        }
        if (containsResult) {
            String onResultAttribute = childTagData.getAttribute(ON_RESULT_ATTRIBUTE);
            if (onResultAttribute.charAt(0) == ACTION_OPERATOR) {
                onResultAttribute = onResultAttribute.substring(1);
            }
            getActorAsDialog().setObject(child, parser.findAction(onResultAttribute, actor));
        }
    }

    @Override
    protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
        if (Strings.isNotWhitespace(data)) {
            getActorAsDialog().text(parser.parseStringData(data, actor));
            getActorAsDialog().getContentTable().row();
        }
    }
}
