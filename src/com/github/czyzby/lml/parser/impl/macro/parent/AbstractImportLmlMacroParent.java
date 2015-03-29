package com.github.czyzby.lml.parser.impl.macro.parent;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;

public abstract class AbstractImportLmlMacroParent extends AbstractLmlMacroParent {
	public static final String DEFAULT_CONTENT_ARGUMENT = "CONTENT";

	public AbstractImportLmlMacroParent(final String tagName, final LmlParent<?> parent,
			final Array<String> arguments) {
		super(tagName, parent, arguments);
	}

	protected String getContentArgumentKey() {
		if (arguments.size < 2) {
			return DEFAULT_CONTENT_ARGUMENT;
		}
		return arguments.get(1);
	}

	/** @param argument import path argument.
	 * @return file handle with the LML document. */
	protected abstract FileHandle getFileHandle(String argument);

	@Override
	public void closeTag(final LmlParser parser) {
		parser.appendToBuffer(getTextToAppend(parser));
	}

	public String getTextToAppend(final LmlParser parser) {
		if (arguments.size == 0) {
			throwErrorIfStrict(parser, "Missing arguments for import macro.");
			return getAppenedTextWithStrippedEndTag();
		}
		return getFileHandle(arguments.first()).readString().replace(toArgument(getContentArgumentKey()),
				getAppenedTextWithStrippedEndTag());
	}
}
