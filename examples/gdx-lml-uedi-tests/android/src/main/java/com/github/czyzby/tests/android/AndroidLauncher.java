package com.github.czyzby.tests.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.czyzby.lml.uedi.LmlApplication;
import com.github.czyzby.uedi.scanner.impl.AndroidClassScanner;
import com.github.czyzby.tests.Root;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        initialize(new LmlApplication(Root.class, new AndroidClassScanner(getApplicationInfo()), "gdx-lml-uedi-tests"), configuration);
    }
}