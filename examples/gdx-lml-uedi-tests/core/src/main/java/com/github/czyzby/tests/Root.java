package com.github.czyzby.tests;

import java.lang.reflect.Member;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.uedi.views.View;
import com.github.czyzby.lml.vis.util.VisLml;
import com.github.czyzby.uedi.stereotype.Provider;

/** The first application's view. See first.lml file for widgets layout. */
public class Root extends View implements Provider<LmlParser> {
    /** Default application size. */
    public static final int WIDTH = 640, HEIGHT = 480;

    @Override
    public String getViewId() {
        return "first";
    }
    @Override
    public boolean isFirst() {
        return true;
    }

    // This class implements Provider interface to customize LmlParser used to create the view.
    @Override
    public Class<? extends LmlParser> getType() {
        return LmlParser.class;
    }
    @Override
    public LmlParser provide(final Object target, final Member member) {
        return VisLml.parser().build();
    }

    /** Since this method is annotated with LmlAction, it will be available in the LML template: first.lml.
     * @param button its text will be changed. */
    @LmlAction("setClicked")
    public void changeButtonText(final TextButton button) {
        button.setText("Clicked.");
    }
}