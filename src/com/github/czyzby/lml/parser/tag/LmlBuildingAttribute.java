package com.github.czyzby.lml.parser.tag;

import com.github.czyzby.lml.parser.LmlParser;

/** Represents a single attribute that must be parsed before the widget is created. An example of this might be the
 * style attribute, which is needed to select widget's style in skin in the constructor. Note that since the attribute
 * parsing takes place before the actor is even created, referencing actions that require the actor as argument is
 * undesired - even if the action can be properly found by its ID, it will still receive a null argument, possibly
 * causing problems. If necessary, use no-arg methods in building attributes.
 *
 * @author MJ
 *
 * @param <Builder> contains widget building data. */
public interface LmlBuildingAttribute<Builder extends LmlActorBuilder> {
    /** Utility for {@link #process(LmlParser, LmlTag, LmlActorBuilder, String)} method. Return for code clarity. */
    boolean FULLY_PARSED = true, NOT_FULLY_PARSED = false;

    /** @return base actor classes that can be handled by this attribute processor. Since building attributes work on
     *         generic builders rather than widgets themselves, then can handle multiple children types. An example
     *         might be a text builder with additional "initialText" string property that can be used to build both
     *         labels and text buttons. */
    Class<?>[] getHandledTypes();

    /** @param parser handles LML template parsing.
     * @param tag contains raw tag data. Allows to access actor's parent.
     * @param builder used to construct the widget.
     * @param rawAttributeData unparsed LML attribute data that should be handled by this attribute processor. Common
     *            data types (string, int, float, boolean, action) are already handled by LML parser implementation, so
     *            make sure to invoke its methods.
     * @return true if attribute was fully processed and should not be evaluated again by an after-creation attribute
     *         parser. False otherwise. */
    boolean process(LmlParser parser, LmlTag tag, Builder builder, String rawAttributeData);
}
