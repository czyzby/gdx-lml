package com.konfigurats.lml.parser.impl.macro.parent;

import com.badlogic.gdx.utils.Array;
import com.konfigurats.lml.parser.impl.dto.LmlParent;

public abstract class AbstractLoopLmlMacroParent extends AbstractLmlMacroParent {
	public static final String INDEX_ARGUMENT_OPENING = "\\$\\{";
	public static final String INDEX_ARGUMENT_CLOSING = ":[iI][nN][dD][eE][xX]\\}";

	public AbstractLoopLmlMacroParent(final String tagName, final LmlParent<?> parent,
			final Array<String> arguments) {
		super(tagName, parent, arguments);
	}

	protected String getIndexArgumentKey() {
		return INDEX_ARGUMENT_OPENING + getTagNameIgnoringCaseRegex() + INDEX_ARGUMENT_CLOSING;
	}
}
