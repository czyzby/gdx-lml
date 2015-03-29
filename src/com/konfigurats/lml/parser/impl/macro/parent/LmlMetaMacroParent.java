package com.konfigurats.lml.parser.impl.macro.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.konfigurats.lml.error.LmlParsingException;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlMacroData;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.macro.AbstractLmlMacroParser;
import com.konfigurats.lml.util.gdx.collection.GdxArrays;

public class LmlMetaMacroParent extends AbstractLmlMacroParent {
	public static final String DEFAULT_CONTENT_ATTRIBUTE = "${MACRO}";

	public LmlMetaMacroParent(final String tagName, final LmlParent<?> parent, final Array<String> arguments) {
		super(tagName, parent, arguments);
	}

	@Override
	public void closeTag(final LmlParser parser) {
		if (arguments.size == 0) {
			throw new LmlParsingException("Macro has to have at least one argument (name).", parser);
		}
		parser.registerMacroParser(new AbstractLmlMacroParser() {
			private final Array<String> macroArguments = GdxArrays.newArray();
			private final String macroContent;
			private final String macroContentAttribute;
			{
				final Array<String> macroArguments = LmlMetaMacroParent.this.arguments;
				macroContentAttribute =
						macroArguments.size > 1 ? toArgument(macroArguments.get(1))
								: DEFAULT_CONTENT_ATTRIBUTE;
				for (int index = 2; index < macroArguments.size; index++) {
					this.macroArguments.add(macroArguments.get(index));
				}
				macroContent = LmlMetaMacroParent.this.getAppenedTextWithStrippedEndTag();
			}

			@Override
			public LmlParent<Actor> parseMacroParent(final LmlParser parser, final LmlMacroData lmlMacroData,
					final LmlParent<?> parent) {
				return new AbstractLmlMacroParent(lmlMacroData.getMacroName(), parent, lmlMacroData
						.getArguments()) {
					@Override
					public void closeTag(final LmlParser parser) {
						String content = extractContent(arguments);
						final String textInTag = getAppenedTextWithStrippedEndTag();
						content = content.replace(macroContentAttribute, textInTag);
						parser.appendToBuffer(content);
					}
				};
			}

			private String extractContent(final Array<String> arguments) {
				String content = macroContent;
				for (int argumentIndex = 0; argumentIndex < macroArguments.size; argumentIndex++) {
					final String argumentKey = macroArguments.get(argumentIndex);
					final String argumentValue =
							arguments.size > argumentIndex ? arguments.get(argumentIndex) : NULL_ARGUMENT;
					content = content.replace(toArgument(argumentKey), argumentValue);
				}
				return content;
			}

			@Override
			protected CharSequence parseTextToAppend(final LmlParser parser, final LmlMacroData lmlMacroData) {
				return extractContent(lmlMacroData.getArguments()).replace(DEFAULT_CONTENT_ATTRIBUTE, "");
			}
		}, getMacroNames(parser));
	}

	private String[] getMacroNames(final LmlParser parser) {
		final Array<String> names = toArgumentArray(arguments.get(0), parser, getParentActor());
		for (int index = 0; index < names.size; index++) {
			names.set(index, names.get(index).toUpperCase());
		}
		return names.toArray(String.class);
	}

}
