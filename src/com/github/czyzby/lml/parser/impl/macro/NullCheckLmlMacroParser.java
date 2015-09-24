package com.github.czyzby.lml.parser.impl.macro;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.macro.parent.ConditionLmlMacroParent;
import com.github.czyzby.lml.parser.impl.macro.parent.NullCheckLmlMacroParent;

public class NullCheckLmlMacroParser extends AbstractLmlMacroParser {

    @Override
    public LmlParent<Actor> parseMacroParent(final LmlParser parser, final LmlMacroData lmlMacroData,
            final LmlParent<?> parent) {
        return new ConditionLmlMacroParent(lmlMacroData, parent, parser);
    }

    @Override
    protected CharSequence parseTextToAppend(final LmlParser parser, final LmlMacroData lmlMacroData) {
        if (lmlMacroData.getArguments().size == 0
                || lmlMacroData.getArguments().size == 1 && !new NullCheckLmlMacroParent(lmlMacroData, null, parser)
                        .doSingleArgumentCheck(lmlMacroData.getArguments().first(), parser)) {
            return Strings.EMPTY_STRING;
        }
        return lmlMacroData.getArguments().toString(SPACE_SEPARATOR);
    }
}
