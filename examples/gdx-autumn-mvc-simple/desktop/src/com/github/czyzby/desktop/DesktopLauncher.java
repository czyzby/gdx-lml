package com.github.czyzby.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.czyzby.Root;
import com.github.czyzby.autumn.fcs.scanner.DesktopClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;
import com.github.czyzby.config.Configuration;

/** Main entry point for desktop application. */
public class DesktopLauncher {
    public static void main(final String[] arg) {
        final LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "Simple Autumn MVC app";
        configuration.width = Configuration.WIDTH;
        configuration.height = Configuration.HEIGHT;
        createApplication(configuration);
    }

    private static Application createApplication(final LwjglApplicationConfiguration configuration) {
        // Note that our ApplicationListener is implemented by AutumnApplication - we just say which classes should be
        // scanned (Configuration.class is the root) and with which scanner (DesktopClassScanner in this case).
        return new LwjglApplication(new AutumnApplication(new DesktopClassScanner(), Root.class), configuration);
    }
}
