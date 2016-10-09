package com.github.czyzby.tests.view;

import com.github.czyzby.lml.uedi.views.View;
import com.github.czyzby.uedi.stereotype.Singleton;

/** Allows to go through application's example screens.
 *
 * @author MJ */
public class MenuController extends View implements Singleton {
    @Override
    public String getViewId() {
        return "menu";
    }
}
