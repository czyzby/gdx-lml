package com.github.czyzby.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.github.czyzby.Core;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;

public class YetAnotherView extends AbstractLmlView {
    public static final String ID = "another";

    public YetAnotherView() {
        super(Core.newStage());
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/another.lml");
    }

    @Override
    public String getViewId() {
        return ID;
    }
}
