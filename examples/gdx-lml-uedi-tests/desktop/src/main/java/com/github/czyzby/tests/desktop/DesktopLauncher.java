package com.github.czyzby.tests.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.czyzby.lml.uedi.LmlApplication;
import com.github.czyzby.uedi.scanner.impl.DefaultClassScanner;
import com.github.czyzby.tests.Root;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new LmlApplication(Root.class, new DefaultClassScanner(), "gdx-lml-uedi-tests"),
                getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "gdx-lml-uedi-tests";
        configuration.width = Root.WIDTH;
        configuration.height = Root.HEIGHT;
        for (int size : new int[] { 128, 64, 32, 16 }) {
            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
        }
        return configuration;
    }
}