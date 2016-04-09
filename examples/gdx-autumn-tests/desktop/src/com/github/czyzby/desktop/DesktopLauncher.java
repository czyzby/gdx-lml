package com.github.czyzby.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.czyzby.Core;
import com.github.czyzby.autumn.fcs.scanner.DesktopClassScanner;

public class DesktopLauncher {
    public static void main(final String[] arg) {
        createApplication();
    }

    private static Application createApplication() {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "gdx-autumn-tests";
        config.width = Core.WIDTH;
        config.height = Core.HEIGHT;
        return new LwjglApplication(getApplicationListener(), config);
    }

    private static ApplicationListener getApplicationListener() {
        // DesktopClassScanner (from gdx-autumn-fcs lib) is the advised class scanner for desktop applications.
        return new Core(new DesktopClassScanner());
    }
}
