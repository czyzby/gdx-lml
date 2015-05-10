package com.github.czyzby.tests.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.lml.util.Lml;
import com.github.czyzby.tests.Main;

public class HtmlLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(1024, 512);
	}

	@Override
	public ApplicationListener getApplicationListener() {
		Lml.EXTRACT_FIELDS_AS_METHODS = false; // Problematic on GWT.
		return new Main();
	}
}