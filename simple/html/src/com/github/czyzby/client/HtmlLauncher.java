package com.github.czyzby.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.Root;
import com.github.czyzby.autumn.gwt.scanner.GwtClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;
import com.github.czyzby.config.Configuration;

/** Launches GWT application. */
public class HtmlLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(Configuration.WIDTH, Configuration.HEIGHT);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        // Note that our ApplicationListener is implemented by AutumnApplication - we just say which classes should be
        // scanned (Configuration.class is the root) and with which scanner (GwtClassScanner in this case).
        return new AutumnApplication(new GwtClassScanner(), Root.class);
    }
}