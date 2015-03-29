package com.konfigurats.lml.parser.impl.macro.parent;

import com.badlogic.gdx.utils.Array;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.util.gdx.collection.GdxArrays;
import com.konfigurats.lml.util.tuple.immutable.Pair;

public class NestedForEachLoopLmlMacroParent extends ForEachLoopLmlMacroParent {
	private int totalIterations;
	private String indexArgument;

	public NestedForEachLoopLmlMacroParent(final String tagName, final LmlParent<?> parent,
			final Array<String> arguments) {
		super(tagName, parent, arguments);
	}

	@Override
	protected StringBuilder parseArguments(final String textInTag,
			final Array<Pair<String, Array<String>>> argumentElements) {
		final StringBuilder textToAppend = new StringBuilder();
		totalIterations = 0;
		indexArgument = getIndexArgumentKey();
		appendArguments(textInTag, textToAppend, argumentElements, 0);
		return textToAppend;
	}

	private void appendArguments(final String textInTag, final StringBuilder textToAppend,
			final Array<Pair<String, Array<String>>> argumentElements, final int argumentIndex) {
		final Pair<String, Array<String>> argumentData = argumentElements.get(argumentIndex);
		for (final String argument : argumentData.getSecond()) {
			String currentLine = textInTag.replace(toArgument(argumentData.getFirst()), argument);
			if (GdxArrays.isIndexLast(argumentElements, argumentIndex)) {
				currentLine = currentLine.replaceAll(indexArgument, String.valueOf(totalIterations++));
				textToAppend.append(currentLine);
			} else {
				appendArguments(currentLine, textToAppend, argumentElements, argumentIndex + 1);
			}
		}
	}
}
