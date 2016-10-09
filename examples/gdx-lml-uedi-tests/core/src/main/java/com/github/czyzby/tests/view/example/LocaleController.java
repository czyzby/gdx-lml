package com.github.czyzby.tests.view.example;

import com.github.czyzby.lml.uedi.views.View;
import com.github.czyzby.uedi.stereotype.Singleton;

/** Allows to change application's locale.
 *
 * @author MJ */
public class LocaleController extends View implements Singleton {
    @Override
    public String getViewId() {
        return "locale";
    }
}
