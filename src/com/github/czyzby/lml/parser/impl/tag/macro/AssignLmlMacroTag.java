package com.github.czyzby.lml.parser.impl.tag.macro;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.common.Nullables;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractMacroLmlTag;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Allows to assign LML parser arguments from within LML templates. Arguments will be available after macro evaluation
 * with the given name, by default: {accessedLikeThis}. First macro attribute is the name of the argument to assign.
 * Other - optional - arguments will be joined with space and assigned as argument value. Alternatively, argument value
 * can be the data between macro tags. For example: <blockquote>
 *
 * <pre>
 * &lt;@assign arg0 Value/&gt;
 * &lt;@assign arg1 Complex value  with many   parts./&gt;
 * &lt;@assign arg2&gt;Data between macro tags.&lt;/@assign&gt;
 * </pre>
 *
 * </blockquote>Assigned values:
 * <ul>
 * <li>{arg0}: {@code "Value"}
 * <li>{arg1}: {@code "Complex value with many parts."}
 * <li>{arg2}: {@code "Data between macro tags."}
 * </ul>
 * Of course, data between assignment macro tags can contain any other tags (including nested assign macros) and can be
 * used to effectively assign template parts to convenient-to-use arguments.
 *
 * @author MJ */
public class AssignLmlMacroTag extends AbstractMacroLmlTag {
    private String argument;

    public AssignLmlMacroTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    public void handleDataBetweenTags(final String rawMacroContent) {
        if (Strings.isNotEmpty(rawMacroContent)) {
            argument = rawMacroContent;
        }
    }

    @Override
    public void closeTag() {
        if (hasArgumentName()) {
            getParser().getData().addArgument(getArgumentName(), processArgumentValue(getArgumentValue()));
        } else {
            getParser().throwErrorIfStrict(
                    "Assignment macro needs at least one attribute: name of the argument to assign.");
        }
    }

    /** @param argumentValue should be evaluated according to macro specification.
     * @return evaluated value. */
    protected String processArgumentValue(final String argumentValue) {
        // Assignment macro does parse the value, it just assigns it.
        return argumentValue;
    }

    /** @return argument value that should be assigned. */
    protected String getArgumentValue() {
        if (argument != null) {
            if (hasArgumentValue()) {
                getParser().throwErrorIfStrict(
                        "Assignment macro cannot have both argument value to assign (second macro attribute) and content between tags. Only 1 value can be assigned to 1 argument name.");
            }
            return argument;
        }
        if (hasArgumentValue()) {
            return getArgumentValueFromAttributes();
        }
        getParser().throwErrorIfStrict(
                "Assignment macro has to have a value to assign. Pass second attribute name or add content between macro tags.");
        return Nullables.DEFAULT_NULL_STRING;
    }

    /** @return true if has at least one attribute. */
    protected boolean hasArgumentName() {
        return GdxArrays.isNotEmpty(getAttributes());
    }

    /** @return true if has the argument value to assign. */
    protected boolean hasArgumentValue() {
        return GdxArrays.isNotEmpty(getAttributes()) && GdxArrays.sizeOf(getAttributes()) > 1;
    }

    /** @return attribute assigned to argument name. */
    protected String getArgumentName() {
        return getAttributes().get(0);
    }

    /** @return attribute assigned to argument value. */
    protected String getArgumentValueFromAttributes() {
        final Array<String> attributes = getAttributes();
        if (GdxArrays.sizeOf(attributes) == 2) {
            return attributes.get(1);
        }
        final StringBuilder builder = new StringBuilder();
        for (int index = 1, length = GdxArrays.sizeOf(attributes); index < length; index++) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(attributes.get(index));
        }
        return builder.toString();
    }
}
