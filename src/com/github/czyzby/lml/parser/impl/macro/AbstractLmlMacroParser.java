package com.github.czyzby.lml.parser.impl.macro;

import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlMacroParser;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.tag.AbstractLmlAttributeParser;
import com.github.czyzby.lml.util.LmlSyntax;

public abstract class AbstractLmlMacroParser extends AbstractLmlAttributeParser implements LmlMacroParser,
		LmlSyntax {
	@Override
	public void parseMacro(final LmlParser parser, final LmlMacroData lmlMacroData) {
		final CharSequence textToAppend = parseTextToAppend(parser, lmlMacroData);
		if (textToAppend != null) {
			parser.appendToBuffer(textToAppend);
		} else {
			if (parser.isStrict()) {
				throw new LmlParsingException(
						"Action not supported. Macro tag should not be closed upon creation: "
								+ lmlMacroData.getMacroName() + ".", parser);
			}
		}
	}

	/** @param parser currently parser a LML document.
	 * @param lmlMacroData contains data of a single macro.
	 * @return text in LML format that will be parsed by passed parser. */
	protected abstract CharSequence parseTextToAppend(LmlParser parser, LmlMacroData lmlMacroData);

}
