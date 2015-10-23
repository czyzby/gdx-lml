package com.github.czyzby.lml.vis.parser.impl.tag.validator;

import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.InputValidator;
import com.kotcrab.vis.ui.util.Validators;

/** Provides {@link Validators.LesserThanValidator}s. Expects at least one attribute: value to check. For example:
 * <blockquote>
 *
 * <pre>
 * &lt;lesserThan 3.2/&gt;
 * &lt;lesserThan -1 true/&gt;
 * </pre>
 *
 * </blockquote>The first value will create a validator that returns true if the value is lesser than 3.2. The second
 * will return true if value is equal to or lesser than -1.
 *
 * <p>
 * Mapped to "lesserThan", "lesserThanValidator".
 *
 * @author MJ */
public class LesserThanValidatorLmlTag extends AbstractValidatorLmlTag {
    public LesserThanValidatorLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    public InputValidator getInputValidator() {
        if (GdxArrays.isEmpty(getAttributes())) {
            getParser().throwError("Greater than validator needs at least one attribute: value to check.");
        }
        final float value = getParser().parseFloat(getAttributes().first(), getParent().getActor());
        boolean canBeEqual;
        if (GdxArrays.sizeOf(getAttributes()) > 1) {
            canBeEqual = getParser().parseBoolean(getAttributes().get(1), getParent().getActor());
        } else {
            canBeEqual = false;
        }
        return new Validators.LesserThanValidator(value, canBeEqual);
    }
}
