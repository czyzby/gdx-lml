package com.konfigurats.lml.util.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/** Provides basic utilities for pretty much any LibGDX application.
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

	/** @return current cursor X (or last touch position), having bottom left corner as origin point. */
	public static int getCurrentCursorX() {
		return Gdx.input.getX();
	}

	/** @return current cursor Y (or last touch position), having bottom left corner as origin point. */
	public static int getCurrentCursorY() {
		return Gdx.graphics.getHeight() - Gdx.input.getY();
	}
}
