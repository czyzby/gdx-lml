package com.github.czyzby.lml.vis.parser.impl.tag.validator;

import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.InputValidator;

/** Allows to create custom {@link InputValidator}s. Expects one attribute: name of a method that consumes a string and
 * returns a boolean. Mapped to "validator", "validate", "customValidator".
 *
 * @author MJ */
public class CustomValidatorLmlTag extends AbstractValidatorLmlTag {
    public CustomValidatorLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    public InputValidator getInputValidator() {
        if (GdxArrays.isEmpty(getAttributes())) {
            getParser().throwError(
                    "Custom validator needs at least one attribute: ID of method that consumes a string and returns a boolean.");
        }
        @SuppressWarnings("unchecked") final ActorConsumer<Boolean, String> validator = (ActorConsumer<Boolean, String>) getParser()
                .parseAction(getAttributes().first(), Strings.EMPTY_STRING);
        return new InputValidator() {
            @Override
            public boolean validateInput(final String input) {
                return validator.consume(input);
            }
        };
    }
}
