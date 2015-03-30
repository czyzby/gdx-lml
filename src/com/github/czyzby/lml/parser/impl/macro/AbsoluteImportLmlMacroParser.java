package com.github.czyzby.lml.parser.impl.macro;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.macro.parent.AbsoluteImportLmlMacroParent;
import com.github.czyzby.lml.parser.impl.macro.parent.AbstractImportLmlMacroParent;

public class AbsoluteImportLmlMacroParser extends AbstractImportLmlMacroParser {
	@Override
	protected AbstractImportLmlMacroParent getImportMacro(final LmlMacroData lmlMacroData,
			final LmlParent<?> parent, final LmlParser parser) {
		return new AbsoluteImportLmlMacroParent(lmlMacroData, parent, parser);
	}
}
