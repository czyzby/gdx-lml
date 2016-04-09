package com.github.czyzby.lml.parser.impl.tag.macro;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractMacroLmlTag;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.collection.IgnoreCaseStringMap;

/** Import macros are used to read other templates and append their content into the currently passed template. There
 * are two ways to invoke the macros:
 *
 * <blockquote>
 *
 * <pre>
 * &lt;:import file.lml /&gt;
 * &lt;:import file.lml contentArg&gt; Content &lt;/:import&gt;
 * </pre>
 *
 * </blockquote>In the first example, file.lml will be read and appended to the current template without any changes. In
 * the second, every argument named contentArg (by default, arguments are represented like this: {contentArg}) will be
 * replaced with the text between import tags: " Content ".
 *
 * @author MJ */
public abstract class AbstractImportLmlMacroTag extends AbstractMacroLmlTag {
    private String content;

    public AbstractImportLmlMacroTag(final LmlParser parser, final LmlTag parentTag, final String rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    public void handleDataBetweenTags(final String rawMacroContent) {
        content = rawMacroContent;
    }

    @Override
    public void closeTag() {
        if (GdxArrays.isEmpty(getAttributes())) {
            getParser().throwErrorIfStrict("Import macros need at least one argument: template name to import.");
            return;
        }
        final FileHandle template = getFileHandle(getTemplateFileName());
        final String textToAppend;
        if (isReplacingArguments()) {
            if (content == null) {
                getParser().throwErrorIfStrict(
                        "Import macros with content name attribute (second attribute) have to be parental and contain some content that can be replaced in the imported template. Remove second import attribute or add data between macro tags.");
            }
            textToAppend = replaceArguments(template.readString(), getArguments());
        } else {
            textToAppend = template.readString();
        }
        appendTextToParse(textToAppend);
    }

    /** @return arguments to replace in the imported template. */
    protected ObjectMap<String, String> getArguments() {
        final ObjectMap<String, String> arguments = new IgnoreCaseStringMap<String>();
        arguments.put(getArgumentName(), content);
        return arguments;
    }

    /** @param templateFileName path to the template to import.
     * @return an instance of FileHandle used to construct with the passed template path. */
    protected abstract FileHandle getFileHandle(String templateFileName);

    /** @return attribute with the name of the template file. Validate attributes list before usage. */
    protected String getTemplateFileName() {
        return getAttributes().get(0);
    }

    /** @return attribute with the name of the argument that should replace template's content. Validate attributes list
     *         before usage. */
    protected String getArgumentName() {
        return getAttributes().get(1);
    }

    /** @return true if contains at least 2 arguments: template file and content argument. */
    protected boolean isReplacingArguments() {
        return GdxArrays.sizeOf(getAttributes()) > 1;
    }
}
