package com.github.czyzby.lml.vis.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUtilities;
import com.github.czyzby.lml.vis.ui.reflected.VisTabTable;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

/** Handles {@link TabbedPane}. Allows to use table attributes: settings will be applied to tabbed pane's main table.
 * Its children, though, should not have any cell attributes; in fact, it is prepared only to handle tab children - see
 * {@link TabLmlTag}. Cannot parse plain text between tags. Mapped to "tabbedPane", "tabs".
 *
 * @author MJ */
public class TabbedPaneLmlTag extends AbstractActorLmlTag {
    private TabbedPane tabbedPane;
    private boolean attachDefaultListener = true;

    public TabbedPaneLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        tabbedPane = new TabbedPane(builder.getStyleName());
        final Table mainTable = tabbedPane.getTable();
        // TabbedPane will be accessible through LmlUserObject#getData().
        LmlUtilities.getLmlUserObject(mainTable).setData(tabbedPane);
        mainTable.row();
        // This will be the content table:
        mainTable.add(new VisTable()).grow().row();
        // There might be an expand+fill image in the second cell. We need to correct that:
        normalizeSecondCell(mainTable);
        return mainTable;
    }

    /** @param mainTable main table of {@link TabbedPane}. */
    protected void normalizeSecondCell(final Table mainTable) {
        final Cell<?> secondCell = mainTable.getCells().get(1);
        if (secondCell.getActor() instanceof Image) {
            secondCell.expand(true, false);
            secondCell.fill(true, false);
        }
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        getParser().throwErrorIfStrict(
                "TabbedPane cannot handle plain text. It can contain only tab children. Found plain text line: "
                        + plainTextLine);
    }

    @Override
    protected void handleValidChild(final LmlTag childTag) {
        if (childTag.getActor() instanceof VisTabTable) {
            final VisTabTable child = (VisTabTable) childTag.getActor();
            tabbedPane.add(child.getTab());
        } else {
            getParser().throwErrorIfStrict(
                    "TabbedPane cannot handle all actors. It can contain only tab children. Found child: "
                            + childTag.getActor() + " with tag name: " + childTag.getTagName());
        }
    }

    @Override
    protected void doOnTagClose() {
        if (attachDefaultListener) {
            getContentTable(tabbedPane).add(tabbedPane.getActiveTab().getContentTable()).grow();
            tabbedPane.addListener(new LmlTabbedPaneListener(tabbedPane));
        }
    }

    /** @param attachDefaultListener if true (default), will attach a default
     *            {@link com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneListener} that adds pane children to an internal
     *            table. This might be an undesired behavior if you want to take control over how tabs are processed, so
     *            set this setting to false if you need a custom listener. */
    public void setAttachDefaultListener(final boolean attachDefaultListener) {
        this.attachDefaultListener = attachDefaultListener;
    }

    /** @param tabbedPane will have its content table extracted. Must have been created with a LML tag.
     * @return content table of the tabbed pane. Might have to be cleared. */
    public static Table getContentTable(final TabbedPane tabbedPane) {
        final Array<Actor> children = tabbedPane.getTable().getChildren();
        final Actor actor = children.get(children.size - 1);
        if (!(actor instanceof Table)) {
            throw new GdxRuntimeException(
                    "Tabbed pane not constructed with LML. Unable to find content table in: " + tabbedPane);
        }
        return (Table) actor;
    }

    /** Default listener of {@link TabbedPane} constructed with LML. Adds tab children to an internal table.
     *
     * @author MJ */
    public static final class LmlTabbedPaneListener extends TabbedPaneAdapter {
        private final TabbedPane tabbedPane;

        /** @param tabbedPane has to be created with a LML tag (or contain a separate content table as its last
         *            widget. */
        public LmlTabbedPaneListener(final TabbedPane tabbedPane) {
            this.tabbedPane = tabbedPane;
        }

        @Override
        public void switchedTab(final Tab tab) {
            if (tab == null) {
                removedAllTabs();
            } else {
                final Table contentTable = getContentTable(tabbedPane);
                contentTable.clear();
                contentTable.add(tab.getContentTable()).grow();
            }
        }

        @Override
        public void removedAllTabs() {
            getContentTable(tabbedPane).clear();
        }
    }
}
