package com.konfigurats.lml.parser.impl.macro;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlMacroData;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.macro.parent.ConditionLmlMacroParent;

public class ConditionLmlMacroParser extends AbstractLmlMacroParser {

	@Override
	public LmlParent<Actor> parseMacroParent(final LmlParser parser, final LmlMacroData lmlMacroData,
			final LmlParent<?> parent) {
		return new ConditionLmlMacroParent(lmlMacroData.getMacroName(), parent, lmlMacroData.getArguments());
	}

	@Override
	protected CharSequence parseTextToAppend(final LmlParser parser, final LmlMacroData lmlMacroData) {
		return null;
	}

}
