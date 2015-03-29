package com.github.czyzby.lml.parser.impl.macro.parent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;

public class InternalImportLmlMacroParent extends AbstractImportLmlMacroParent {
	public InternalImportLmlMacroParent(final String tagName, final LmlParent<?> parent,
			final Array<String> arguments) {
		super(tagName, parent, arguments);
	}

	@Override
	protected FileHandle getFileHandle(final String argument) {
		return Gdx.files.internal(argument);
	}
}
