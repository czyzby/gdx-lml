package com.github.czyzby.lml.parser.impl.macro;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.macro.parent.AbstractImportLmlMacroParent;
import com.github.czyzby.lml.parser.impl.macro.parent.ClasspathImportLmlMacroParent;

public class ClasspathImportLmlMacroParser extends AbstractImportLmlMacroParser {
	@Override
	protected AbstractImportLmlMacroParent getImportMacro(final String tagName, final LmlParent<?> parent,
			final Array<String> arguments) {
		return new ClasspathImportLmlMacroParent(tagName, parent, arguments);
	}
}
