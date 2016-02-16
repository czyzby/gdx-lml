package com.github.czyzby.lml.vis.parser.impl.attribute.listview;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.vis.parser.impl.tag.ListViewLmlTag;
import com.kotcrab.vis.ui.widget.ListView;

/** Abstract base for attributes of {@link ListView} children.
 *
 * @author MJ */
public abstract class ListViewChildLmlAttribute implements LmlAttribute<Actor> {
    @Override
    public Class<Actor> getHandledType() {
        return Actor.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Actor actor, final String rawAttributeData) {
        if (tag.getParent() == null || !(tag.getParent().getActor() instanceof Table)) {
            throwInvalidParentException(parser);
        } else {
            final Actor parent = tag.getParent().getActor();
            final ListView<?> listView = ListViewLmlTag.getListView((Table) parent);
            if (listView == null) {
                throwInvalidParentException(parser);
            } else {
                process(parser, tag, actor, listView, rawAttributeData);
            }
        }
    }

    /** @param parser handles LML template parsing.
     * @param tag contains raw tag data. Allows to access actor's parent.
     * @param actor handled actor instance.
     * @param listView direct parent of the actor.
     * @param rawAttributeData unparsed LML attribute data that should be handled by this attribute processor. Common
     *            data types (string, int, float, boolean, action) are already handled by LML parser implementation, so
     *            make sure to invoke its methods. */
    protected abstract void process(LmlParser parser, LmlTag tag, Actor actor, ListView<?> listView,
            String rawAttributeData);

    protected void throwInvalidParentException(final LmlParser parser) {
        parser.throwErrorIfStrict("ListView child attribute can be processed only inside a list view tag.");
    }
}
