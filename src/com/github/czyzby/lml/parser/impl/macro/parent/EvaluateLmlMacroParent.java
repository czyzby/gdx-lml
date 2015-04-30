package com.github.czyzby.lml.parser.impl.macro.parent;

import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;

public class EvaluateLmlMacroParent extends AbstractLmlMacroParent {
	public EvaluateLmlMacroParent(final LmlMacroData lmlMacroData, final LmlParent<?> parent,
			final LmlParser parser) {
		super(lmlMacroData, parent, parser);
		throw new LmlParsingException("Evaluate macro tag cannot be parental.");
	}

	@Override
	public void closeTag(final LmlParser parser) {
		throw new LmlParsingException("Evaluate macro tag cannot be parental.");
	}
}
