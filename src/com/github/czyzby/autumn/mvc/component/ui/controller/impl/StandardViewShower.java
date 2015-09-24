package com.github.czyzby.autumn.mvc.component.ui.controller.impl;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewShower;
import com.github.czyzby.lml.gdx.widget.reflected.Tooltip;

/** Shows and hides the screen by passing received action to the stage without any modifications. Removes tooltips and
 * dialogs before showing.
 *
 * @author MJ */
public class StandardViewShower implements ViewShower {
    @Override
    public void show(final Stage stage, final Action action) {
        for (final Actor actor : stage.getActors()) {
            if (actor instanceof Dialog || actor instanceof Tooltip) {
                actor.remove();
            }
        }
        stage.addAction(action);
    }

    @Override
    public void hide(final Stage stage, final Action action) {
        stage.addAction(action);
    }
}
