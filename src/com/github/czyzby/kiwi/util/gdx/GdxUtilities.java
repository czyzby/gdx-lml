package com.github.czyzby.kiwi.util.gdx;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

/** Provides generic utilities for pretty much any LibGDX application.
 *
 * @author MJ */
public class GdxUtilities {
	private GdxUtilities() {
	}

	/** Clears the screen with black color. */
	public static void clearScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	/** Application's input processor will be set to null. */
	public static void clearInputProcessor() {
		Gdx.input.setInputProcessor(null);
	}

	/** Application's input processor will be set to a multiplexer with multiple passed processors. */
	public static void setMultipleInputProcessors(final InputProcessor... processors) {
		Gdx.input.setInputProcessor(new InputMultiplexer(processors));
	}

	/** @return a new vector2 storing windows' width and height as x and y. */
	public static Vector2 getScreenSize() {
		return new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	/** @return the passed vector2 storing windows' width and height as x and y. */
	public static Vector2 getScreenSize(final Vector2 result) {
		return result.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public static boolean isRunningOnApplet() {
		return Gdx.app.getType() == ApplicationType.Applet;
	}

	public static boolean isRunningOnAndroid() {
		return Gdx.app.getType() == ApplicationType.Android;
	}

	public static boolean isRunningOnDesktop() {
		return Gdx.app.getType() == ApplicationType.Desktop;
	}

	public static boolean isRunningOnIOS() {
		return Gdx.app.getType() == ApplicationType.iOS;
	}

	public static boolean isRunningOnGwt() {
		return Gdx.app.getType() == ApplicationType.WebGL;
	}

	public static boolean isHeadless() {
		return Gdx.app.getType() == ApplicationType.HeadlessDesktop;
	}
}
