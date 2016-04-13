package com.github.czyzby.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.github.czyzby.Core;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.github.czyzby.lml.util.LmlApplicationListener;

/** A very basic first and main view of the application. Allows to switch to other views. Since all required screen
 * changing actions are in {@link LmlApplicationListener}, this class is pretty much empty.
 *
 * @author MJ */
public class Menu extends AbstractLmlView {
    public Menu() {
        super(Core.newStage());
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/Menu.lml");
    }

    @Override
    public String getViewId() {
        return "menu";
    }
}
