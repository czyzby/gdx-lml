package com.github.czyzby.gdx.idle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.czyzby.autumn.fcs.scanner.DesktopClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;
import com.github.czyzby.gdx.idle.Config;

public class DesktopLauncher {
    public static void main(final String[] arg) {
        createNewApplication();
    }

    private static LwjglApplication createNewApplication() {
        return new LwjglApplication(new AutumnApplication(new DesktopClassScanner(), Config.class),
                getDesktopConfiguration());
    }

    private static LwjglApplicationConfiguration getDesktopConfiguration() {
        final LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "GdxIdle";
        configuration.width = 512;
        configuration.height = 512;
        return configuration;
    }
}
