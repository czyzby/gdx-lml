package com.github.czyzby.lml.vis.parser.impl.action;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.lml.parser.impl.action.DefaultStageAttacher;
import com.kotcrab.vis.ui.widget.VisDialog;

/** Expands default stage attacher functionality with the ability to attach {@link VisDialog}s.
 *
 * @author MJ */
public class VisStageAttacher extends DefaultStageAttacher {
    @Override
    public void attachToStage(final Actor actor, final Stage stage) {
        if (actor instanceof VisDialog) {
            ((VisDialog) actor).show(stage);
        }
        super.attachToStage(actor, stage);
    }
}
