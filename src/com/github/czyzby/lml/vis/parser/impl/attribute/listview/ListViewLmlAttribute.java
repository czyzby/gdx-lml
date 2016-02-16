package com.github.czyzby.lml.vis.parser.impl.attribute.listview;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.vis.parser.impl.tag.ListViewLmlTag;
import com.kotcrab.vis.ui.widget.ListView;

/** Abstract base for {@link ListView} tag attributes.
 *
 * @author MJ */
public abstract class ListViewLmlAttribute implements LmlAttribute<Table> {
    @Override
    public Class<Table> getHandledType() {
        return Table.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Table actor, final String rawAttributeData) {
        final ListView<?> listView = ListViewLmlTag.getListView(actor);
        if (listView == null) {
            parser.throwErrorIfStrict("This attribute can be used only in ListView tags.");
        } else {
            process(parser, tag, actor, listView, rawAttributeData);
        }
    }

    /** @param parser handles LML template parsing.
     * @param tag contains raw tag data. Allows to access actor's parent.
     * @param mainTable list view's main table.
     * @param listView actual list view instance.
     * @param rawAttributeData unparsed LML attribute data that should be handled by this attribute processor. Common
     *            data types (string, int, float, boolean, action) are already handled by LML parser implementation, so
     *            make sure to invoke its methods. */
    protected abstract void process(LmlParser parser, LmlTag tag, Table mainTable, ListView<?> listView,
            String rawAttributeData);
}
