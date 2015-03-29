package com.konfigurats.lml.util.gdx;

import java.util.Map;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/** Provides some simple utility methods for disposable objects.
 *
 * @author MJ */
public class Disposables {
	private Disposables() {
	}

	/** Performs null check and disposes of an asset.
	 *
	 * @param disposable will be disposed of (if it exists). */
	public static void disposeOf(final Disposable disposable) {
		if (disposable != null) {
			disposable.dispose();
		}
	}

	/** Performs null checks and disposes of assets.
	 *
	 * @param disposables will be disposed of (if they exist). */
	public static void disposeOf(final Disposable... disposables) {
		for (final Disposable disposable : disposables) {
			disposeOf(disposable);
		}
	}

	/** Performs null checks and disposes of assets.
	 *
	 * @param disposables will be disposed of (if they exist). */
	public static void disposeOf(final Iterable<? extends Disposable> disposables) {
		if (disposables != null) {
			for (final Disposable disposable : disposables) {
				disposeOf(disposable);
			}
		}
	}

	/** Performs null checks and disposes of assets.
	 *
	 * @param disposables its values will be disposed of (if they exist). Can be null. */
	public static void disposeOf(final ObjectMap<?, ? extends Disposable> disposables) {
		if (disposables != null) {
			for (final Disposable disposable : disposables.values()) {
				disposeOf(disposable);
			}
		}
	}

	/** Performs null checks and disposes of assets.
	 *
	 * @param disposables its values will be disposed of (if they exist). Can be null. */
	public static void disposeOf(final Map<?, ? extends Disposable> disposables) {
		if (disposables != null) {
			for (final Disposable disposable : disposables.values()) {
				disposeOf(disposable);
			}
		}
	}
}
