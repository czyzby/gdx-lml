package com.github.czyzby.lml.parser.impl.macro;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlMacroParser;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.macro.parent.LmlMetaMacroParent;

public class LmlMetaMacroParser implements LmlMacroParser {
	@Override
	public void parseMacro(final LmlParser parser, final LmlMacroData lmlMacroData) {
		throw new LmlParsingException("Marco has to have content.", parser);
	}

	@Override
	public LmlParent<Actor> parseMacroParent(final LmlParser parser, final LmlMacroData lmlMacroData,
			final LmlParent<?> parent) {
		return new LmlMetaMacroParent(lmlMacroData.getMacroName(), parent, lmlMacroData.getArguments());
	}
}
