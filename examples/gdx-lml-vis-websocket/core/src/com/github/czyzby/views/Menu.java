package com.github.czyzby.views;

import com.github.czyzby.Core;
import com.github.czyzby.actions.GlobalActions;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;

/** A very basic first and main view of the application. Allows to switch to other views. Since all required screen
 * changing actions are in {@link GlobalActions}, this class is pretty much empty.
 *
 * @author MJ */
public class Menu extends AbstractLmlView {
    public Menu() {
        super(Core.newStage());
    }

    @Override
    public String getViewId() {
        return "menu";
    }
}
