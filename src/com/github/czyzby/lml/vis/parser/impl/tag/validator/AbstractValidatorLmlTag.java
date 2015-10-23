package com.github.czyzby.lml.vis.parser.impl.tag.validator;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractLmlTag;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.InputValidator;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

/** Abstract base for {@link InputValidator} tags.
 *
 * @author MJ */
public abstract class AbstractValidatorLmlTag extends AbstractLmlTag {
    public AbstractValidatorLmlTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
        if (parentTag == null) {
            parser.throwError("Validators need to be attached to a tag. No parent found for tag: " + getTagName());
        }
    }

    @Override
    public void handleDataBetweenTags(final String rawData) {
        if (Strings.isNotBlank(rawData)) {
            getParser().throwErrorIfStrict("Validators cannot parse plain text between tags.");
        }
    }

    @Override
    public Actor getActor() {
        return null;
    }

    @Override
    public void closeTag() {
    }

    @Override
    public void handleChild(final LmlTag childTag) {
        getParser().throwErrorIfStrict("Validators cannot have children.");
    }

    /** @return {@link InputValidator} supplied by this tag. Invoked once, when validator is attached to the actor. */
    public abstract InputValidator getInputValidator();

    @Override
    protected boolean supportsNamedAttributes() {
        return false;
    }

    @Override
    public boolean isAttachable() {
        return true;
    }

    @Override
    public void attachTo(final LmlTag tag) {
        if (tag.getActor() instanceof VisValidatableTextField) {
            ((VisValidatableTextField) tag.getActor()).addValidator(getInputValidator());
        } else { // TODO are they any other usages?
            getParser().throwErrorIfStrict("Validators can be attached only to VisValidatableTextField actors.");
        }
    }
}
