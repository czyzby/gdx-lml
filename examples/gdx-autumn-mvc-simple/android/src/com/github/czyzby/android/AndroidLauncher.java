package com.github.czyzby.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.czyzby.Root;
import com.github.czyzby.autumn.android.scanner.AndroidClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;

/** Launches Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // Note that our ApplicationListener is implemented by AutumnApplication - we just say which classes should be
        // scanned (Configuration.class is the root) and with which scanner (AndroidClassScanner in this case).
        initialize(new AutumnApplication(new AndroidClassScanner(), Root.class), config);
    }
}
