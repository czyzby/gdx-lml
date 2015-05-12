package com.github.czyzby.tests.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.czyzby.tests.Main;

public class DesktopLauncher {
	public static void main(final String[] arg) {
		new LwjglApplication(new Main(), getDefaultConfiguration());
	}

	private static LwjglApplicationConfiguration getDefaultConfiguration() {
		final LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "LML 0.6.1.6.0 tests";
		configuration.width = 1024;
		configuration.height = 512;
		return configuration;
	}
}
