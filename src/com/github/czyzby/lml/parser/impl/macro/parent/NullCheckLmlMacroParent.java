package com.github.czyzby.lml.parser.impl.macro.parent;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;

public class NullCheckLmlMacroParent extends AbstractConditionalLmlMacroParent {
	public NullCheckLmlMacroParent(final LmlMacroData lmlMacroData, final LmlParent<?> parent,
			final LmlParser parser) {
		super(lmlMacroData, parent, parser);
	}

	@Override
	protected boolean checkCondition(final LmlParser parser) {
		if (arguments.size == 0) {
			return false;
		} else if (arguments.size == 1) {
			return doSingleArgumentCheck(arguments.get(0), parser);
		}
		// More arguments received - assuming a parameter was passed and it consists of multiple parts
		// (therefore is not null).
		return true;
	}

	@Override
	protected boolean needsArgument() {
		return false;
	}
}
