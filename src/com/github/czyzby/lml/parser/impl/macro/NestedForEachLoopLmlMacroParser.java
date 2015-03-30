package com.github.czyzby.lml.parser.impl.macro;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.macro.parent.NestedForEachLoopLmlMacroParent;

public class NestedForEachLoopLmlMacroParser extends ForEachLoopLmlMacroParser {
	@Override
	public LmlParent<Actor> parseMacroParent(final LmlParser parser, final LmlMacroData lmlMacroData,
			final LmlParent<?> parent) {
		return new NestedForEachLoopLmlMacroParent(lmlMacroData, parent, parser);
	}
}
