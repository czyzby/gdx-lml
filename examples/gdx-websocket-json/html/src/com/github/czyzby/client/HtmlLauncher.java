package com.github.czyzby.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.Core;
import com.github.czyzby.websocket.GwtWebSockets;

public class HtmlLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(128, 128);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        // Initiating web sockets module:
        GwtWebSockets.initiate();
        return new Core();
    }
}