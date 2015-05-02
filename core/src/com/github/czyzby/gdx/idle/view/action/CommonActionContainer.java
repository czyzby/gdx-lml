package com.github.czyzby.gdx.idle.view.action;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.mvc.stereotype.ViewActionContainer;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;
import com.github.czyzby.kiwi.util.gdx.collection.immutable.ImmutableArray;
import com.github.czyzby.lml.parser.impl.annotation.ViewAction;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;

@ViewActionContainer("common")
public class CommonActionContainer implements ActionContainer {
	/** Colors of common drawables stored in skin. */
	private static final Array<String> BACKGROUND_COLORS = ImmutableArray.of("brown", "grey");

	// Note that @ViewAction is optional, methods can be referenced by names. By using this annotation, you
	// can refactor or obfuscate your code without breaking the templates, as methods are assigned to a
	// constant key.
	@ViewAction("getRandomBackground")
	public String getRandomBackgroundColor() {
		return BACKGROUND_COLORS.random();
	}

	@ViewAction("isNotGwt")
	public boolean isNotGwt() {
		return !GdxUtilities.isRunningOnGwt();
	}

	// Utility action for dialogs - can be passed with onResult to simply close the dialog.
	@ViewAction("close")
	public void doNothing() {
	}
}