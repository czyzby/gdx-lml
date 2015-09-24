package com.github.czyzby.lml.parser.impl.macro.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.AbstractLmlDto;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.macro.AbstractLmlMacroParser;

public class LmlMetaMacroParent extends AbstractLmlMacroParent {
    public static final String DEFAULT_CONTENT_ATTRIBUTE = "${MACRO}";

    public LmlMetaMacroParent(final LmlMacroData lmlMacroData, final LmlParent<?> parent, final LmlParser parser) {
        super(lmlMacroData, parent, parser);
    }

    @Override
    public void closeTag(final LmlParser parser) {
        if (arguments.size == 0) {
            throw new LmlParsingException("Macro has to have at least one argument (name).", parser);
        }
        parser.registerMacroParser(new AbstractLmlMacroParser() {
            private final Array<String> macroArguments = GdxArrays.newArray();
            private final ObjectMap<String, String> defaultValues = GdxMaps.newObjectMap();
            private final ObjectSet<String> parsedArguments = GdxSets.newSet();
            private final String macroContent;
            private final String macroContentAttribute;

            {
                final Array<String> macroArguments = LmlMetaMacroParent.this.arguments;
                macroContentAttribute = macroArguments.size > 1 ? toArgument(macroArguments.get(1))
                        : DEFAULT_CONTENT_ATTRIBUTE;
                for (int index = 2; index < macroArguments.size; index++) {
                    registerArgument(macroArguments.get(index));
                }
                macroContent = LmlMetaMacroParent.this.getAppenedTextWithStrippedEndTag();
            }

            private void registerArgument(final String macroArgument) {
                final int separatorIndex = extractSeparatorIndex(macroArgument);
                if (separatorIndex < 0) {
                    macroArguments.add(macroArgument);
                    defaultValues.put(macroArgument, NULL_ARGUMENT);
                } else {
                    final String argumentName = extractArgumentName(macroArgument, separatorIndex);
                    macroArguments.add(argumentName);
                    defaultValues.put(argumentName, extractArgumentValue(macroArgument, separatorIndex));
                }
            }

            @Override
            public LmlParent<Actor> parseMacroParent(final LmlParser parser, final LmlMacroData lmlMacroData,
                    final LmlParent<?> parent) {
                return new AbstractLmlMacroParent(lmlMacroData, parent, parser) {
                    @Override
                    public void closeTag(final LmlParser parser) {
                        String content = extractContent(arguments, parser);
                        final String textInTag = getAppenedTextWithStrippedEndTag();
                        content = content.replace(macroContentAttribute, textInTag);
                        parser.appendToBuffer(content);
                    }
                };
            }

            private String extractContent(final Array<String> arguments, final LmlParser parser) {
                if (containsNamedParameters(arguments)) {
                    return processNamedArguments(arguments, parser, macroContent);
                } else {
                    return processUnnamedParameters(arguments, macroContent);
                }
            }

            private boolean containsNamedParameters(final Array<String> arguments) {
                return GdxArrays.isNotEmpty(arguments) && Strings.contains(arguments.first(), MACRO_ARGUMENT_SEPARATOR);
            }

            private String processNamedArguments(final Array<String> arguments, final LmlParser parser,
                    String content) {
                for (final String macroArgument : arguments) {
                    final int separatorIndex = extractSeparatorIndex(macroArgument);
                    final String argumentName = extractArgumentName(macroArgument, separatorIndex);
                    if (!defaultValues.containsKey(argumentName)) {
                        throw new LmlParsingException("Invalid macro argument name. Received: " + argumentName
                                + ", expected one of: " + macroArguments, parser);
                    }
                    if (parsedArguments.contains(argumentName)) {
                        throwErrorIfStrict(parser, "Macro argument referenced multiple times: " + argumentName + ".");
                        continue;
                    }
                    final String argumentValue = extractArgumentValue(macroArgument, separatorIndex);
                    content = content.replace(toArgument(argumentName), argumentValue);
                    parsedArguments.add(argumentName);
                }
                if (GdxSets.sizeOf(parsedArguments) != GdxArrays.sizeOf(macroArguments)) {
                    // Appending unreferenced arguments:
                    for (final String macroArgument : macroArguments) {
                        content = content.replace(toArgument(macroArgument), defaultValues.get(macroArgument));
                    }
                }
                parsedArguments.clear();
                return content;
            }

            private int extractSeparatorIndex(final String macroArgument) {
                return macroArgument.indexOf(MACRO_ARGUMENT_SEPARATOR);
            }

            private String extractArgumentName(final String macroArgument, final int separatorIndex) {
                return macroArgument.substring(0, separatorIndex);
            }

            private String extractArgumentValue(final String macroArgument, final int separatorIndex) {
                return AbstractLmlDto
                        .escapeQuotation(macroArgument.substring(separatorIndex + 1, macroArgument.length()));
            }

            private String processUnnamedParameters(final Array<String> arguments, String content) {
                for (int argumentIndex = 0; argumentIndex < GdxArrays.sizeOf(macroArguments); argumentIndex++) {
                    final String argumentKey = macroArguments.get(argumentIndex);
                    final String argumentValue = Strings.toString(arguments.size > argumentIndex
                            ? arguments.get(argumentIndex) : defaultValues.get(argumentKey), NULL_ARGUMENT);
                    content = content.replace(toArgument(argumentKey), argumentValue);
                }
                return content;
            }

            @Override
            protected CharSequence parseTextToAppend(final LmlParser parser, final LmlMacroData lmlMacroData) {
                return extractContent(lmlMacroData.getArguments(), parser).replace(macroContentAttribute,
                        Strings.EMPTY_STRING);
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
