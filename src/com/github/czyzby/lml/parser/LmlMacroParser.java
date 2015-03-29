package com.github.czyzby.lml.parser;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;

/** Handles a macro tag, proceeded with @.
 *
 * @author MJ */
public interface LmlMacroParser {
	/** @param parser is currently parsing LML file.
	 * @param lmlMacroData data of a single closed macro. */
	public void parseMacro(LmlParser parser, LmlMacroData lmlMacroData);

	/** @param parser is currently parsing LML file.
	 * @param lmlMacroData data of a single parental macro. */
	public LmlParent<Actor> parseMacroParent(LmlParser parser, LmlMacroData lmlMacroData, LmlParent<?> parent);
}
