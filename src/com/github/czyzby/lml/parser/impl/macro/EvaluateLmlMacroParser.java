package com.github.czyzby.lml.parser.impl.macro;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.macro.parent.EvaluateLmlMacroParent;

public class EvaluateLmlMacroParser extends AbstractLmlMacroParser {
	@Override
	public LmlParent<Actor> parseMacroParent(final LmlParser parser, final LmlMacroData lmlMacroData,
			final LmlParent<?> parent) {
		return new EvaluateLmlMacroParent(lmlMacroData, parent, parser);
	}

	@Override
	protected CharSequence parseTextToAppend(final LmlParser parser, final LmlMacroData lmlMacroData) {
		final Array<String> arguments = lmlMacroData.getArguments();
		if (arguments.size == 0) {
			throwErrorIfStrict(parser,
					"Evaluate macro needs at least one argument - method name. Received none.");
		} else {
			final Object methodResult =
					parser.findAction(getMethodName(arguments.first()), (Actor) null).consume(parser);
			if (arguments.size > 1) {
				parser.addArgument(arguments.get(1), Strings.toString(methodResult, NULL_ARGUMENT));
			}
		}
		return Strings.EMPTY_STRING;
	}

	private static String getMethodName(final String argument) {
		return Strings.startsWith(argument, ACTION_OPERATOR) ? argument.substring(1) : argument;
	}
}
