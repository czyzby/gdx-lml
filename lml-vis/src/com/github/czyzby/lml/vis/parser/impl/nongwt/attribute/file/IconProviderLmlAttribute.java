package com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.FileIconProvider;

/** See {@link FileChooser#setIconProvider(FileIconProvider)}. Expects ID of an action that references a method
 * consuming {@link FileHandle} and returning a {@link Drawable}. Method will be invoked each time an icon for a file is
 * requested. Mapped to "iconProvider".
 *
 * @author MJ */
public class IconProviderLmlAttribute implements LmlAttribute<FileChooser> {
    @Override
    public Class<FileChooser> getHandledType() {
        return FileChooser.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final FileChooser actor,
            final String rawAttributeData) {
        @SuppressWarnings("unchecked") final ActorConsumer<Drawable, FileHandle> provider = (ActorConsumer<Drawable, FileHandle>) parser
                .parseAction(rawAttributeData, MockUpFileHandle.INSTANCE);
        if (provider == null) {
            parser.throwErrorIfStrict(
                    "Icon provider attribute expects a method that consumes a FileHandle and returns Drawable. Method not found for ID: "
                            + rawAttributeData);
            return;
        }
        actor.setIconProvider(new FileIconProvider() {
            @Override
            public Drawable provideIcon(final FileHandle file) {
                return provider.consume(file);
            }
        });
    }
}
