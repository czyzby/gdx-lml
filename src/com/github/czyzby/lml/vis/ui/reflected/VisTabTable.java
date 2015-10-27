package com.github.czyzby.lml.vis.ui.reflected;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

/** {@link VisTable} extension that manages an internal {@link Tab} implementation. Meant to be used as
 * {@link com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane} tab child.
 *
 * @author MJ */
public class VisTabTable extends VisTable {
    private final TableTab tab = new TableTab();
    private String title;
    private ActorConsumer<?, VisTabTable> onDispose;

    /** @param title tab's title. */
    public VisTabTable(final String title) {
        this.title = title;
    }

    /** @return implementation of {@link Tab} managed by this table. */
    public TableTab getTab() {
        return tab;
    }

    /** @return title used by the tab. */
    public String getTitle() {
        return title;
    }

    /** @param onDispose will be invoked (consuming this table) when the tab is being removed from the pane. */
    public void setOnDispose(final ActorConsumer<?, VisTabTable> onDispose) {
        this.onDispose = onDispose;
    }

    /** @param title will become title of the tab. Note that this value should be changed only if the tab is currently
     *            not shown. */
    public void setTitle(final String title) {
        this.title = title;
    }

    /** Internal {@link Tab} implementation used by {@link VisTabTable}.
     *
     * @author MJ */
    public class TableTab extends Tab {
        private boolean closeableByUser = true;
        private boolean savable;

        @Override
        public boolean isCloseableByUser() {
            return closeableByUser;
        }

        /** @param closeableByUser will be returned by {@link #isCloseableByUser()} when adding the tab. Note that this
         *            method should be used only before the tab is added. */
        public void setCloseableByUser(final boolean closeableByUser) {
            this.closeableByUser = closeableByUser;
        }

        @Override
        public boolean isSavable() {
            return savable;
        }

        /** @param savable will be returned by {@link #isSavable()} when adding the tab to the pane. Note that this
         *            method should be used only before the tab is added. */
        public void setSavable(final boolean savable) {
            this.savable = savable;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public Table getContentTable() {
            return VisTabTable.this;
        }

        @Override
        public void dispose() {
            if (onDispose != null) {
                onDispose.consume(VisTabTable.this);
            }
        }
    }
}
