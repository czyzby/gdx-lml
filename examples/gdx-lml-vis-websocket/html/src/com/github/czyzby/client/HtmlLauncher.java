package com.github.czyzby.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.Core;
import com.github.czyzby.websocket.GwtWebSockets;

public class HtmlLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(Core.WIDTH, Core.HEIGHT);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        GwtWebSockets.initiate();
        return new Core();
    }
}