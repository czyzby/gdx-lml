package com.github.czyzby.lml.vis.parser.impl.tag.builder;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.kotcrab.vis.ui.layout.ColumnGroup;
import com.kotcrab.vis.ui.layout.GridGroup;

/** Allows to build drag pane widgets.
 *
 * @author MJ */
public class DragPaneLmlActorBuilder extends LmlActorBuilder {
    private GroupType groupType = GroupType.HORIZONTAL;

    /** @return type of group managed by the drag pane. */
    public GroupType getGroupType() {
        return groupType;
    }

    /** @param groupType type of group managed by the drag pane. */
    public void setGroupType(final GroupType groupType) {
        this.groupType = groupType;
    }

    /** Contains all default group types managed by the drag pane.
     *
     * @author MJ */
    public static enum GroupType {
        /** Constructs {@link HorizontalGroup}. */
        HORIZONTAL {
            @Override
            public WidgetGroup getGroup() {
                return new HorizontalGroup();
            }
        },
        /** Constructs {@link VerticalGroup}. */
        VERTICAL {
            @Override
            public WidgetGroup getGroup() {
                return new VerticalGroup();
            }
        },
        /** Constructs {@link GridGroup}. */
        GRID {
            @Override
            public WidgetGroup getGroup() {
                return new GridGroup();
            }
        },
        /** Constructs {@link ColumnGroup}. */
        COLUMN {
            @Override
            public WidgetGroup getGroup() {
                return new ColumnGroup();
            }
        };

        /** @return a new instance of selected group type. */
        public abstract WidgetGroup getGroup();
    }
}
