package com.github.czyzby.tests.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.lml.uedi.LmlApplication;
import com.github.czyzby.uedi.scanner.impl.GwtClassScanner;
import com.github.czyzby.tests.Root;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        GwtApplicationConfiguration configuration = new GwtApplicationConfiguration(Root.WIDTH, Root.HEIGHT);
        return configuration;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new LmlApplication(Root.class, new GwtClassScanner(), "gdx-lml-uedi-tests");
    }
}