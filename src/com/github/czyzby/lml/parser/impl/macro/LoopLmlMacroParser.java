package com.github.czyzby.lml.parser.impl.macro;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.macro.parent.LoopLmlMacroParent;

public class LoopLmlMacroParser extends AbstractLmlMacroParser {
	@Override
	public LmlParent<Actor> parseMacroParent(final LmlParser parser, final LmlMacroData lmlMacroData,
			final LmlParent<?> parent) {
		return new LoopLmlMacroParent(lmlMacroData, parent, parser);
	}

	@Override
	protected CharSequence parseTextToAppend(final LmlParser parser, final LmlMacroData lmlMacroData) {
		return null;
	}
}
