package com.konfigurats.lml.parser.impl.macro;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlMacroData;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.macro.parent.AssignLmlMacroParent;

public class AssignLmlMacroParser extends AbstractLmlMacroParser {
	@Override
	public LmlParent<Actor> parseMacroParent(final LmlParser parser, final LmlMacroData lmlMacroData,
			final LmlParent<?> parent) {
		return new AssignLmlMacroParent(lmlMacroData.getMacroName(), parent, lmlMacroData.getArguments());
	}

	@Override
	protected CharSequence parseTextToAppend(final LmlParser parser, final LmlMacroData lmlMacroData) {
		final Array<String> arguments = lmlMacroData.getArguments();
		if (arguments.size < 2) {
			throwErrorIfStrict(parser,
					"Closed assign macro needs two arguments to assign an argument. Received: " + arguments
							+ ".");
		} else {
			parser.addArgument(arguments.first(), arguments.get(1));
		}
		return EMPTY_STRING;
	}
}
