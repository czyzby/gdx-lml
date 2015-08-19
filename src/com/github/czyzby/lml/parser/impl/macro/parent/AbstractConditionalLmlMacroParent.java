package com.github.czyzby.lml.parser.impl.macro.parent;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.common.Nullables;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlMacroData;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;

public abstract class AbstractConditionalLmlMacroParent extends AbstractLmlMacroParent {
	protected static final String ELSE_OPENING = "<@";
	protected static final String ELSE_CLOSING = ":[eE][lL][sS][eE]/>";

	public AbstractConditionalLmlMacroParent(final LmlMacroData lmlMacroData, final LmlParent<?> parent,
			final LmlParser parser) {
		super(lmlMacroData, parent, parser);
	}

	@Override
	public void closeTag(final LmlParser parser) {
		final String textInTag = getAppenedTextWithStrippedEndTag();
		if (textInTag.length() == 0) {
			throwErrorIfStrict(parser, "Conditional macro has to have a body.");
			return;
		}
		if (needsArgument() && arguments.size == 0) {
			throwErrorIfStrict(parser, "This conditional macro has to have an argument to check.");
			return;
		}
		final String[] textToAppend =
				textInTag.split(ELSE_OPENING + getTagNameIgnoringCaseRegex() + ELSE_CLOSING);

		if (checkCondition(parser)) {
			parser.appendToBuffer(textToAppend[0]);
		} else if (textToAppend.length > 1) {
			parser.appendToBuffer(textToAppend[1]);
		}
	}

	/** @return true if the macro needs at least one argument to be properly evaluated. */
	protected abstract boolean needsArgument();

	protected abstract boolean checkCondition(LmlParser parser);

	public boolean doSingleArgumentCheck(final String conditionArgument, final LmlParser parser) {
		if (conditionArgument != null && conditionArgument.length() > 0) {
			if (conditionArgument.charAt(0) == ACTION_OPERATOR) {
				// Method invocation, consuming parent:
				return invokeConditionalAction(conditionArgument, parser);
			}
			// Null/false check:
			return !conditionArgument.equalsIgnoreCase(NULL_ARGUMENT)
					&& !conditionArgument.equalsIgnoreCase(BOOLEAN_FALSE_ATTRIBUTE_VALUE);
		}
		return false;
	}

	private boolean invokeConditionalAction(final String conditionArgument, final LmlParser parser) {
		final Object actionResult =
				parser.findAction(conditionArgument.substring(1), Array.class).consume(getParentActor());
		if (actionResult instanceof Boolean) {
			return (Boolean) actionResult;
		}
		return doSingleArgumentCheck(Nullables.toString(actionResult, NULL_ARGUMENT), parser);
	}
}
