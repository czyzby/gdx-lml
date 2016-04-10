package com.github.czyzby.lml.vis.parser.impl.attribute.tabbed;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUserObject;
import com.github.czyzby.lml.util.LmlUtilities;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;

/** Abstract base for {@link TabbedPane} attributes. Since tabbed pane is not an actor, its internal {@link Table} is
 * used to handle attributes; if the table belonds to a tabbed pane, it should have a reference to its instance in
 * {@link LmlUserObject}.
 *
 * @author MJ */
public abstract class AbstractTabbedPaneLmlAttribute implements LmlAttribute<Table> {
    /** Mock-up {@link Tab} instance. Can be used to retrieve actions that consume a tab. Do not use in actual
     * scenes. */
    public static final Tab MOCK_UP_TAB = new Tab() {
        @Override
        public String getTabTitle() {
            return null;
        }

        @Override
        public Table getContentTable() {
            return null;
        }
    };

    @Override
    public Class<Table> getHandledType() {
        return Table.class;
    }

    @Override
    public final void process(final LmlParser parser, final LmlTag tag, final Table actor,
            final String rawAttributeData) {
        // Checking if the table is a main table of a TabbedPane:
        final LmlUserObject userObject = LmlUtilities.getOptionalLmlUserObject(actor);
        if (userObject != null && userObject.getData() instanceof TabbedPane) {
            process(parser, tag, (TabbedPane) userObject.getData(), rawAttributeData);
        } else {
            parser.throwErrorIfStrict("Only tabbed panes can parser this attribute. Found a reference to this parser: "
                    + this + " in an invalid table tag: " + tag.getTagName());
        }
    }

    /** @param parser handles LML template parsing.
     * @param tag contains raw tag data. Allows to access actor's parent.
     * @param tabbedPane handled tabbed pane instance.
     * @param rawAttributeData unparsed LML attribute data that should be handled by this attribute processor. Common
     *            data types (string, int, float, boolean, action) are already handled by LML parser implementation, so
     *            make sure to invoke its methods. */
    protected abstract void process(LmlParser parser, LmlTag tag, TabbedPane tabbedPane, String rawAttributeData);
}
