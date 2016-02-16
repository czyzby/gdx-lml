package com.github.czyzby.lml.vis.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.actor.TableLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUserObject;
import com.github.czyzby.lml.util.LmlUtilities;
import com.github.czyzby.lml.vis.parser.impl.tag.builder.ListViewLmlActorBuilder;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.util.adapter.ListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.ListView.UpdatePolicy;

/** Manages {@link ListView} widget. Note that list view is not actually an {@link Actor} - instead, its main table is
 * used as the main widget. When injecting the list view into fields or methods, expect {@link Table} instance and
 * extract {@link ListView} with {@link #getListView(Table)} method. Mapped to "listView".
 *
 * <p>
 * Normally, list views are used to display collections of values. However, they can also be used as regular groups of
 * actors. If no {@link ListAdapter} is set with "adapter" attribute, a default implementation is provided. The default
 * adapter is prepared to handle regular actors, allowing the tag to have children. If a custom adapter is provided with
 * "adapter" attribute, this tag cannot have regular children.
 *
 * <p>
 * To set footer and header, use "footer=true" and "header=true" attributes in list view tag's children.
 *
 * <p>
 * Since list view manages a table and a scroll pane, it can have its attributes in its tag. However, table cell
 * attributes CANNOT be used: table is automatically built by the list adapter and should not be modified manually. If
 * you want a custom way of building the table, use another adapter implementation.
 *
 * <p>
 * By default, {@link ListView} will be in {@link UpdatePolicy#ON_DRAW} policy to prevent the table from being rebuilt
 * during creation. If you want to change this setting, do it manually after constructing the view.
 *
 * @author MJ */
public class ListViewLmlTag extends TableLmlTag {
    private ListView<?> listView;
    private ListAdapter<?> listAdapter;

    public ListViewLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected ListViewLmlActorBuilder getNewInstanceOfBuilder() {
        return new ListViewLmlActorBuilder();
    }

    @Override
    protected Table getNewInstanceOfActor(final LmlActorBuilder builder) {
        listAdapter = extractListAdapter((ListViewLmlActorBuilder) builder);
        listView = createListView(listAdapter);
        LmlUtilities.getLmlUserObject(listView.getMainTable()).setData(listView);
        listView.setUpdatePolicy(UpdatePolicy.ON_DRAW); // Prevents the table from being rebuilt during creation.
        return listView.getMainTable();
    }

    /** @param mainTable main table of a {@link ListView} created with LML.
     * @return {@link ListView} instance managing the table. Might be null if the table is not managed by a list view or
     *         LML meta-data was cleared.
     * @see LmlUtilities#clearLmlUserObjects(Iterable)
     * @see LmlUtilities#getLmlUserObject(Actor) */
    public static ListView<?> getListView(final Table mainTable) {
        final LmlUserObject userObject = LmlUtilities.getOptionalLmlUserObject(mainTable);
        if (userObject != null && userObject.getData() instanceof ListView<?>) {
            return (ListView<?>) userObject.getData();
        }
        return null;
    }

    /** @param listAdapter converts data to views.
     * @return a new instance of ListView. */
    protected <Type> ListView<Type> createListView(final ListAdapter<Type> listAdapter) {
        return new ListView<Type>(listAdapter);
    }

    /** @param builder may contain a custom adapter.
     * @return customized list adapter or the default implementation prepared to handle regular actors. */
    protected ListAdapter<?> extractListAdapter(final ListViewLmlActorBuilder builder) {
        return builder.getListAdapter() != null ? builder.getListAdapter()
                : new ArrayAdapter<Actor, Actor>(new Array<Actor>()) {
                    @Override
                    protected Actor createView(final Actor item) {
                        return item;
                    }
                };
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addChild(final Actor actor) {
        if (listView.getFooter() == actor || listView.getHeader() == actor) {
            return;
        } else if (listAdapter instanceof ArrayAdapter<?, ?>) {
            ((ArrayAdapter<Actor, Actor>) listAdapter).add(actor);
        } else {
            getParser().throwErrorIfStrict(
                    "ListView can have tag children only with the default list adapter. Cannot append children to a custom list view.");
        }
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        addChild(toLabel(plainTextLine));
    }

    @Override
    protected boolean hasComponentActors() {
        return true;
    }

    @Override
    protected Actor[] getComponentActors(final Actor actor) {
        final ListView<?> listView = getListView((Table) actor);
        return new Actor[] { listView.getScrollPane() };
    }
}
