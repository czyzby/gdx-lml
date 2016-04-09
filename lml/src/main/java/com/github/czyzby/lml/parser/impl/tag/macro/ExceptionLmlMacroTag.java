package com.github.czyzby.lml.parser.impl.tag.macro;

import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractMacroLmlTag;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Throws parsing exception. Has one optional attribute: a boolean. If the boolean matches "true", exception is always
 * thrown; otherwise only strict parser throws exception. If no attribute is given, exception is always thrown.
 *
 * <blockquote>
 *
 * <pre>
 * &lt;:notNull $myMethod&gt;
 *      &lt;label text=$myMethod&gt;
 * &lt;:notNull:else/&gt;
 *      &lt;:exception&gt;MyMethod should never return null!&lt;/:exception&gt;
 * &lt;/:notNull&gt;
 * </pre>
 *
 * </blockquote>If method mapped to "myMethod" key returns null or false, this will throw an exception with a custom
 * reason message: "MyMethod should never return null!".
 *
 * @author MJ */
public class ExceptionLmlMacroTag extends AbstractMacroLmlTag {
    private String content = "Exception thrown by invoking exception macro.";

    public ExceptionLmlMacroTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    public void handleDataBetweenTags(final String rawData) {
        content = rawData;
    }

    @Override
    public void closeTag() {
        boolean always;
        if (GdxArrays.isEmpty(getAttributes())) {
            always = true;
        } else {
            always = Boolean.valueOf(getParser().parseString(getAttributes().first(), getActor()));
        }
        if (always) {
            getParser().throwError(content);
        } else {
            getParser().throwErrorIfStrict(content);
        }
    }
}
