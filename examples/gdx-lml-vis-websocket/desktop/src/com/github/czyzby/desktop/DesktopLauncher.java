package com.github.czyzby.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.czyzby.Core;
import com.github.czyzby.websocket.CommonWebSockets;

public class DesktopLauncher {
    public static void main(final String[] arg) {
        CommonWebSockets.initiate();
        createApplication();
    }

    private static Application createApplication() {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "gdx-lml-vis-websocket";
        config.width = Core.WIDTH;
        config.height = Core.HEIGHT;
        return new LwjglApplication(new Core(), config);
    }
}
