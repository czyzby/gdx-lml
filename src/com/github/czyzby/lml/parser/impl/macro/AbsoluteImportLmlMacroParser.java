package com.github.czyzby.lml.parser.impl.macro;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.macro.parent.AbsoluteImportLmlMacroParent;
import com.github.czyzby.lml.parser.impl.macro.parent.AbstractImportLmlMacroParent;

public class AbsoluteImportLmlMacroParser extends AbstractImportLmlMacroParser {
	@Override
	protected AbstractImportLmlMacroParent getImportMacro(final String tagName, final LmlParent<?> parent,
			final Array<String> arguments) {
		return new AbsoluteImportLmlMacroParent(tagName, parent, arguments);
	}
}
