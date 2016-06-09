package com.github.czyzby.tests.view.example;

import com.github.czyzby.lml.uedi.views.View;
import com.github.czyzby.uedi.stereotype.Singleton;

/** Allows to dynamically rebuild the view.
 *
 * @author MJ */
public class RefreshController extends View implements Singleton {
    @Override
    public String getViewId() {
        return "refresh";
    }
}
