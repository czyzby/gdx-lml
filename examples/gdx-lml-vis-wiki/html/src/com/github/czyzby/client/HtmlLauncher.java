package com.github.czyzby.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.Core;

public class HtmlLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(Core.WIDTH, Core.HEIGHT);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        // GWT applications don't like to be closed, so we're turning off the logging:
        setLogLevel(Application.LOG_NONE);
        // Normally you just wouldn't include an exit button on this platform.
        return new Core();
    }
}