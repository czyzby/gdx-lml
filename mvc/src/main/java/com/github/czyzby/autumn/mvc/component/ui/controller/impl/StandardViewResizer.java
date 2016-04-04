package com.github.czyzby.autumn.mvc.component.ui.controller.impl;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewResizer;

/** Resizes the screen by updating its viewport.
 *
 * @author MJ */
public class StandardViewResizer implements ViewResizer {
    @Override
    public void resize(final Stage stage, final int width, final int height) {
        stage.getViewport().update(width, height, false);
    }
}
