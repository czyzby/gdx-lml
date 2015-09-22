package com.github.czyzby.tests.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.czyzby.tests.Main;

public class DesktopLauncher {
	public static void main(final String[] arg) {
		createNewApplication();
	}

	private static LwjglApplication createNewApplication() {
		return new LwjglApplication(new Main(), getDefaultConfiguration());
	}

	private static LwjglApplicationConfiguration getDefaultConfiguration() {
		final LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "LML 0.8.1.7.0 tests";
		configuration.width = 1024;
		configuration.height = 512;
		return configuration;
	}
}