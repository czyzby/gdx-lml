package com.github.czyzby.tests.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.tests.Main;

/** Launches GWT application.
 * 
 * @author MJ */
public class HtmlLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(1024, 512);
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return new Main();
    }
}