package com.github.czyzby.lml.vis.parser.impl.nongwt.attribute.file;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;

/** Constructs a {@link FileChooser} that will be shown after the widget is clicked (unless it's disabled). Attribute
 * expects a reference to a method that consumes a single {@link FileHandle} or an {@link Array} of files. File chooser
 * allows to select files (not directories). If the file chooser is cancelled, file handle-consuming method will receive
 * null; array-consuming method will receive empty array. Mapped to "fileChooser".
 *
 * @author MJ */
public class FileChooserLmlAttribute implements LmlAttribute<Actor> {
    @Override
    public Class<Actor> getHandledType() {
        return Actor.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final Actor actor, final String rawAttributeData) {
        final FileChooser fileChooser = new FileChooser(Mode.OPEN);
        fileChooser.setSelectionMode(getSelectionMode());
        addFileChooserListener(parser, rawAttributeData, fileChooser);
        actor.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (actor instanceof Disableable && ((Disableable) actor).isDisabled()) {
                    return;
                }
                actor.getStage().addActor(fileChooser.fadeIn());
            }
        });
    }

    /** @param parser parses the attribute.
     * @param rawAttributeData attribute value.
     * @param fileChooser will have a listener set. */
    protected void addFileChooserListener(final LmlParser parser, final String rawAttributeData,
            final FileChooser fileChooser) {
        final ActorConsumer<?, FileHandle> action = parser.parseAction(rawAttributeData, MockUpFileHandle.INSTANCE);
        if (action != null) {
            // Handling single file:
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(final FileHandle file) {
                    action.consume(file);
                }

                @Override
                public void canceled() {
                    action.consume(null);
                }
            });
        } else {
            // Handling multiple files:
            final ActorConsumer<?, Array<FileHandle>> directoryAction = parser.parseAction(rawAttributeData,
                    MockUpFileHandle.EMPTY_ARRAY);
            if (directoryAction == null) {
                parser.throwError(
                        "File chooser attribute needs a reference to an action consuming a FileHandle or Array<FileHandle>. Action not found for: "
                                + rawAttributeData);
            }
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setListener(new FileChooserListener() {
                @Override
                public void selected(final Array<FileHandle> files) {
                    directoryAction.consume(files);
                }

                @Override
                public void selected(final FileHandle file) {
                    directoryAction.consume(GdxArrays.newArray(file));
                }

                @Override
                public void canceled() {
                    directoryAction.consume(GdxArrays.newArray(FileHandle.class));
                }
            });
        }
    }

    /** @return type of file chooser selection mode. */
    protected SelectionMode getSelectionMode() {
        return SelectionMode.FILES;
    }
}
