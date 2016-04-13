package com.github.czyzby.actions;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.Core;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.views.Menu;

/** This is a utility class with methods available in all LML views.
 *
 * @author MJ */
public class GlobalActions implements ActionContainer {
    // Note that @LmlAction annotation is optional. If annotation is not present, method name can referenced in LML view
    // instead. However, it is advised to use this annotation, because A) you can refactor method names without breaking
    // LML templates, B) it makes it clear that the method is reserved for LML views and not a part of public API, C) it
    // significantly speeds up method look-up during parsing phase.

    /** Begins view transition to {@link Menu}. */
    @LmlAction("toMenu")
    void setViewToMenu() {
        ((Core) Gdx.app.getApplicationListener()).setView(Menu.class);
    }
}
