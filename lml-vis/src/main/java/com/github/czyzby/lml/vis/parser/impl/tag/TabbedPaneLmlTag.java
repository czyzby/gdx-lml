package com.github.czyzby.lml.vis.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.impl.attribute.table.OneColumnLmlAttribute;
import com.github.czyzby.lml.parser.impl.tag.AbstractActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUserObject;
import com.github.czyzby.lml.util.LmlUtilities;
import com.github.czyzby.lml.vis.ui.VisTabTable;
import com.github.czyzby.lml.vis.ui.reflected.action.TabShowingAction;
import com.kotcrab.vis.ui.layout.HorizontalFlowGroup;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

/** Handles {@link TabbedPane}. Allows to use table attributes: settings will be applied to tabbed pane's main table.
 * Its children, though, should not have any cell attributes; in fact, it is prepared only to handle tab children - see
 * {@link TabLmlTag}. Cannot parse plain text between tags. Note that tabbed pane tag cannot handle
 * {@link OneColumnLmlAttribute} properly. Tabbed pane is not actually an actor - if you want to inject the pane by its
 * ID, use {@link Table} instead and extract {@link TabbedPane} instance with {@link #getTabbedPane(Table)}. Mapped to
 * "tabbedPane", "tabs".
 *
 * @author MJ */
public class TabbedPaneLmlTag extends AbstractActorLmlTag {
    private TabbedPane tabbedPane;
    private boolean attachDefaultListener = true;
    private ActorConsumer<Action, Tab> showActionProvider;
    private ActorConsumer<Action, Tab> hideActionProvider;

    public TabbedPaneLmlTag(final LmlParser parser, final LmlTag parentTag, final StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        tabbedPane = new TabbedPane(builder.getStyleName());
        final Table mainTable = tabbedPane.getTable();
        // TabbedPane will be accessible through LmlUserObject#getData(). This disables oneColumn attribute, though.
        LmlUtilities.getLmlUserObject(mainTable).setData(tabbedPane);
        if (tabbedPane.getTabsPane().isHorizontal()
                || tabbedPane.getTabsPane().getActor() instanceof HorizontalFlowGroup) {
            mainTable.row();
        }
        // This will be the content table:
        mainTable.add(new VisTable()).grow().row();
        // There might be an expand+fill image in the second cell. We need to correct that:
        normalizeSecondCell(mainTable);
        return mainTable;
    }

    /** @param table main table of the {@link TabbedPane}. If LML meta-data was not cleared, it will contain a reference
     *            of its {@link TabbedPane} parent.
     * @return {@link TabbedPane} which uses passed table as its main table. {@code null} if table is not used by a
     *         tabbed pane, LML meta-data was cleared or reference was lost due to prohibited settings (like using of
     *         {@link OneColumnLmlAttribute}). */
    public static TabbedPane getTabbedPane(final Table table) {
        final LmlUserObject userObject = LmlUtilities.getOptionalLmlUserObject(table);
        if (userObject != null && userObject.getData() instanceof TabbedPane) {
            return (TabbedPane) userObject.getData();
        }
        return null;
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
            if (child.isDisabled()) {
                tabbedPane.disableTab(child.getTab(), true);
            }
        } else {
            getParser().throwErrorIfStrict(
                    "TabbedPane cannot handle all actors. It can contain only tab children. Found child: "
                            + childTag.getActor() + " with tag name: " + childTag.getTagName());
        }
    }

    @Override
    protected void doOnTagClose() {
        if (attachDefaultListener) {
            LmlUtilities.getLmlUserObject(tabbedPane.getTable()).addOnCloseAction(getListenerAttachmentAction());
        }
    }

    /** @return an on-close action that attached the default listener and fills content table with the active tab's
     *         content. */
    protected ActorConsumer<Object, Object> getListenerAttachmentAction() {
        return new ActorConsumer<Object, Object>() {
            @Override
            public Object consume(final Object actor) {
                // Invoked in a separate action to insure that the widget is truly fully built:
                getContentTable(tabbedPane).add(tabbedPane.getActiveTab().getContentTable()).grow();
                tabbedPane.addListener(new LmlTabbedPaneListener(tabbedPane, showActionProvider, hideActionProvider,
                        tabbedPane.getActiveTab()));
                return null;
            }
        };
    }

    /** @param attachDefaultListener if true (default), will attach a default
     *            {@link com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneListener} that adds pane children to an internal
     *            table. This might be an undesired behavior if you want to take control over how tabs are processed, so
     *            set this setting to false if you need a custom listener. */
    public void setAttachDefaultListener(final boolean attachDefaultListener) {
        this.attachDefaultListener = attachDefaultListener;
    }

    /** @param showActionProvider will be invoked to provide an action for a tab each time a tab is shown. */
    public void setShowActionProvider(final ActorConsumer<Action, Tab> showActionProvider) {
        this.showActionProvider = showActionProvider;
    }

    /** @param hideActionProvider will be invoked to provide an action for a tab each time a tab is hidden. */
    public void setHideActionProvider(final ActorConsumer<Action, Tab> hideActionProvider) {
        this.hideActionProvider = hideActionProvider;
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

    /** Default listener of {@link TabbedPane} constructed with LML. Appends tab children to an internal
     * {@link TabbedPane}'s table.
     *
     * @author MJ */
    public static final class LmlTabbedPaneListener extends TabbedPaneAdapter {
        private final TabbedPane tabbedPane;
        private final ActorConsumer<Action, Tab> showActionProvider;
        private final ActorConsumer<Action, Tab> hideActionProvider;
        /** Cached tab for action utility. */
        private Tab currentTab;

        /** @param tabbedPane has to be created with a LML tag (or contain a separate content table as its last
         *            widget. */
        public LmlTabbedPaneListener(final TabbedPane tabbedPane) {
            this(tabbedPane, null, null, null);
        }

        /** @param tabbedPane has to be created with a LML tag (or contain a separate content table as its last widget.
         * @param showActionProvider optional provider of showing actions. Will be used to show tabs.
         * @param hideActionProvider optional provider of hiding actions. Will be used to hide tabs.
         * @param initialTab current selected tab. Can be null. */
        public LmlTabbedPaneListener(final TabbedPane tabbedPane, final ActorConsumer<Action, Tab> showActionProvider,
                final ActorConsumer<Action, Tab> hideActionProvider, final Tab initialTab) {
            this.tabbedPane = tabbedPane;
            this.showActionProvider = showActionProvider;
            this.hideActionProvider = hideActionProvider;
            currentTab = initialTab;
        }

        @Override
        public void switchedTab(final Tab tab) {
            if (currentTab == null) {
                // Immediate transition:
                setNewTab(tab);
            } else if (hideActionProvider == null) {
                // Immediate transition:
                setNewTab(tab);
            } else {
                // Showing after the hiding action is done:
                final Table contentTable = getContentTable(tabbedPane);
                contentTable.clearActions();
                contentTable.addAction(Actions.sequence(hideActionProvider.consume(currentTab),
                        Actions.action(TabShowingAction.class).show(tab, this)));
            }
        }

        /** @param tab will become currently set action. */
        public void setNewTab(final Tab tab) {
            final Table contentTable = getContentTable(tabbedPane);
            contentTable.clear();
            currentTab = tab;
            if (tab != null) {
                contentTable.add(tab.getContentTable()).grow();
                if (showActionProvider != null) {
                    contentTable.addAction(showActionProvider.consume(tab));
                }
            } else {
                contentTable.clear();
            }
        }

        @Override
        public void removedAllTabs() {
            switchedTab(null);
        }
    }
}
