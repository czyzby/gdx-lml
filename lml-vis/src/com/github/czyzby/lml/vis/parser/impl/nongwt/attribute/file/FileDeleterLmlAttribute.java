package com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file;

import com.github.czyzby.kiwi.util.common.Exceptions;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.DefaultFileDeleter;

/** See {@link FileChooser#setFileDeleter(com.kotcrab.vis.ui.widget.file.FileChooser.FileDeleter)}. Expects an ID of an
 * action that returns a FileDeleter instance. Mapped to "fileDeleter".
 *
 * @author MJ */
public class FileDeleterLmlAttribute implements LmlAttribute<FileChooser> {
    @Override
    public Class<FileChooser> getHandledType() {
        return FileChooser.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final FileChooser actor,
            final String rawAttributeData) {
        final ActorConsumer<?, FileChooser> action = parser.parseAction(rawAttributeData, actor);
        if (action == null) {
            parser.throwErrorIfStrict(
                    "File deleter attribute expects an action that returns a FileDeleter instance. Action not found for: "
                            + rawAttributeData);
        }
        final Object result = action.consume(actor);
        if (result instanceof DefaultFileDeleter) {
            actor.setFileDeleter((DefaultFileDeleter) result);
            return;
        }
        try {
            // We cannot reference the JNA classes directly. FileDeleter interface is not public, so we cannot access it
            // either. Using reflection to check if FileUtils class is available and to invoke setter method, avoiding
            // casting to unavailable FileDeleter or potentially exception-throwing JNAFileDeleter.
            if (Class.forName("com.kotcrab.vis.ui.widget.file.JNAFileDeleter").isInstance(result)) {
                FileChooser.class
                        .getMethod("setFileDeleter",
                                Class.forName("com.kotcrab.vis.ui.widget.file.FileChooser$FileDeleter"))
                        .invoke(actor, new Object[] { result });
                return;
            }
        } catch (final Exception exception) {
            Exceptions.ignore(exception); // Expected. JNA not available.
        }
        parser.throwErrorIfStrict(
                "Unable to set file deleter. A method returning FileDeleter instance is required; got result: "
                        + result);
    }
}
