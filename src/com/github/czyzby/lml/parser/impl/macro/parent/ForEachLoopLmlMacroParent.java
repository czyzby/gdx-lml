package com.github.czyzby.lml.parser.impl.macro.parent;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.util.tuple.immutable.Pair;

public class ForEachLoopLmlMacroParent extends AbstractLoopLmlMacroParent {
	public static final String ARGUMENT_DECLARATION_SEPARATOR = ":";

	public ForEachLoopLmlMacroParent(final LmlMacroData lmlMacroData, final LmlParent<?> parent,
			final LmlParser parser) {
		super(lmlMacroData, parent, parser);
	}

	@Override
	public void closeTag(final LmlParser parser) {
		final String textInTag = getAppenedTextWithStrippedEndTag();
		if (textInTag.length() == 0) {
			throwErrorIfStrict(parser, "For-each loop has to have a body.");
			return;
		}
		if (arguments.size == 0) {
			throwErrorIfStrict(parser, "For-each loop has to have arguments to iterate over.");
			return;
		}
		final Array<Pair<String, Array<String>>> argumentElements =
				parseArgumentElementsPairedWithKeys(parser, arguments);
		final StringBuilder textToAppend = parseArguments(textInTag, argumentElements);
		parser.appendToBuffer(textToAppend);
	}

	protected StringBuilder parseArguments(final String textInTag,
			final Array<Pair<String, Array<String>>> argumentElements) {
		final StringBuilder textToAppend = new StringBuilder();
		final String indexArgumentKey = getIndexArgumentKey();
		final int iterationsAmount = getBiggestArgumentsListSize(argumentElements);
		for (int index = 0; index < iterationsAmount; index++) {
			String appendedText = textInTag.replaceAll(indexArgumentKey, String.valueOf(index));
			for (final Pair<String, Array<String>> argument : argumentElements) {
				appendedText =
						appendedText.replace(toArgument(argument.getFirst()),
								extractArgument(argument.getSecond(), index));
			}
			textToAppend.append(appendedText);
		}
		return textToAppend;
	}

	private int getBiggestArgumentsListSize(final Array<Pair<String, Array<String>>> argumentElements) {
		int max = 0;
		for (final Pair<String, Array<String>> argument : argumentElements) {
			max = Math.max(max, argument.getSecond().size);
		}
		return max;
	}

	protected CharSequence extractArgument(final Array<String> array, final int index) {
		if (array.size > index) {
			return array.get(index);
		}
		return NULL_ARGUMENT;
	}

	private Array<Pair<String, Array<String>>> parseArgumentElementsPairedWithKeys(final LmlParser parser,
			final Array<String> arguments) {
		final Array<Pair<String, Array<String>>> elements = GdxArrays.newArray();
		for (final String argument : arguments) {
			final String[] argumentData = argument.split(ARGUMENT_DECLARATION_SEPARATOR);
			if (argumentData.length != 2) {
				throw new LmlParsingException("Unexpected argument declaration in loop: " + argument + ".",
						parser);
			}
			elements.add(Pair.of(argumentData[0], toArgumentArray(argumentData[1], parser, getParentActor())));
		}
		return elements;
	}
}
