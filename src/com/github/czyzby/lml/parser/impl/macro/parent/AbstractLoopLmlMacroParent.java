package com.github.czyzby.lml.parser.impl.macro.parent;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;

public abstract class AbstractLoopLmlMacroParent extends AbstractLmlMacroParent {
	public static final String INDEX_ARGUMENT_OPENING = "\\$\\{";
	public static final String INDEX_ARGUMENT_CLOSING = ":[iI][nN][dD][eE][xX]\\}";

	public AbstractLoopLmlMacroParent(final LmlMacroData lmlMacroData, final LmlParent<?> parent,
			final LmlParser parser) {
		super(lmlMacroData, parent, parser);
	}

	protected String getIndexArgumentKey() {
		return INDEX_ARGUMENT_OPENING + getTagNameIgnoringCaseRegex() + INDEX_ARGUMENT_CLOSING;
	}
}
