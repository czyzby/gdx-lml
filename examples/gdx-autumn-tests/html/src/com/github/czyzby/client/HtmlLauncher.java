package com.github.czyzby.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.Core;
import com.github.czyzby.autumn.gwt.scanner.GwtClassScanner;

public class HtmlLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(Core.WIDTH, Core.HEIGHT);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        // GwtClassScanner (from gdx-autumn-gwt lib) is the default ClassScanner implementation for GWT applications.
        return new Core(new GwtClassScanner());
    }

    // Note: GWT applications are not really prepared to be closed. GwtApplication#exit() is not even implemented
    // properly. When you click on the GWT application to close it, expect some NPE errors at the end - this is because
    // GwtApplication still tries to render itself even after it was disposed. Unfortunate, but expected.
}