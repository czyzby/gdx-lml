package com.github.czyzby.lml.parser.impl.macro;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.macro.parent.AbstractImportLmlMacroParent;

public abstract class AbstractImportLmlMacroParser extends AbstractLmlMacroParser {
	@Override
	protected CharSequence parseTextToAppend(final LmlParser parser, final LmlMacroData lmlMacroData) {
		return getImportMacro(lmlMacroData, null, parser).getTextToAppend(parser);
	}

	protected abstract AbstractImportLmlMacroParent getImportMacro(LmlMacroData lmlMacroData,
			LmlParent<?> parent, LmlParser parser);

	@Override
	public LmlParent<Actor> parseMacroParent(final LmlParser parser, final LmlMacroData lmlMacroData,
			final LmlParent<?> parent) {
		return getImportMacro(lmlMacroData, parent, parser);
	}
}