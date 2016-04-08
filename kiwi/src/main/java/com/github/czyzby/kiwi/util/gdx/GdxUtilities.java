package com.github.czyzby.kiwi.util.gdx;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.czyzby.kiwi.util.common.UtilitiesClass;

/** Provides generic utilities for pretty much any LibGDX application.
 *
 * @author MJ */
public class GdxUtilities extends UtilitiesClass {
    private GdxUtilities() {
    }

    /** Clears the screen with black color. */
    public static void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /** Clears the screen with the selected color.
     *
     * @param r red color value.
     * @param g green color value.
     * @param b blue color value. */
    public static void clearScreen(final float r, final float g, final float b) {
        Gdx.gl.glClearColor(r, g, b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /** Application's input processor will be set to null. */
    public static void clearInputProcessor() {
        Gdx.input.setInputProcessor(null);
    }

    /** @param processors application's input processor will be set to a multiplexer with multiple passed processors. */
    public static void setMultipleInputProcessors(final InputProcessor... processors) {
        Gdx.input.setInputProcessor(new InputMultiplexer(processors));
    }

    /** @return a new vector2 storing windows' width and height as x and y. */
    public static Vector2 getScreenSize() {
        return new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /** @param result will be modified and returned.
     * @return the passed vector2 storing windows' width and height as x and y. */
    public static Vector2 getScreenSize(final Vector2 result) {
        return result.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /** @return a new vector2 storing current cursor position. */
    public static Vector2 getCursorPosition() {
        return new Vector2(Gdx.input.getX(), Gdx.input.getY());
    }

    /** @param result will be modified and returned.
     * @return the passed vector2 storing current cursor position. */
    public static Vector2 getCursorPosition(final Vector2 result) {
        return result.set(Gdx.input.getX(), Gdx.input.getY());
    }

    /** @return a new vector3 storing current accelerometer data. */
    public static Vector3 getAccelerometerData() {
        return new Vector3(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY(), Gdx.input.getAccelerometerZ());
    }

    /** @param result will be modified and returned.
     * @return the passed vector3 storing current accelerometer data. */
    public static Vector3 getAccelerometerData(final Vector3 result) {
        return result.set(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY(), Gdx.input.getAccelerometerZ());
    }

    /** @return true if application type equals {@link ApplicationType#Applet}. */
    public static boolean isRunningOnApplet() {
        return Gdx.app.getType() == ApplicationType.Applet;
    }

    /** @return true if application type equals {@link ApplicationType#Android}. */
    public static boolean isRunningOnAndroid() {
        return Gdx.app.getType() == ApplicationType.Android;
    }

    /** @return true if application type equals {@link ApplicationType#Android} or {@link ApplicationType#iOS}. */
    public static boolean isMobile() {
        return isRunningOnAndroid() || isRunningOnIOS();
    }

    /** @return true if application type equals {@link ApplicationType#Desktop}. */
    public static boolean isRunningOnDesktop() {
        return Gdx.app.getType() == ApplicationType.Desktop;
    }

    /** @return true if application type equals {@link ApplicationType#iOS}. */
    public static boolean isRunningOnIOS() {
        return Gdx.app.getType() == ApplicationType.iOS;
    }

    /** @return true if application type equals {@link ApplicationType#WebGL}. */
    public static boolean isRunningOnGwt() {
        return Gdx.app.getType() == ApplicationType.WebGL;
    }

    /** @return true if application type equals {@link ApplicationType#HeadlessDesktop}. */
    public static boolean isHeadless() {
        return Gdx.app.getType() == ApplicationType.HeadlessDesktop;
    }
}