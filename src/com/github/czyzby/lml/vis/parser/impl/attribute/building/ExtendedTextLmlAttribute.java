package com.github.czyzby.lml.vis.parser.impl.attribute.building;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.github.czyzby.lml.parser.impl.attribute.building.TextLmlAttribute;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

/** Same functionality as {@link TextLmlAttribute} - allows to build text-requiring actors. Includes Vis widgets. Mapped
 * to "text", "txt", "value".
 *
 * @author MJ */
public class ExtendedTextLmlAttribute extends TextLmlAttribute {
    @Override
    public Class<?>[] getHandledTypes() {
        // VisWindow, VisDialog, VisText (etc.) would be already handled, as widgets in their hierarchy are registered
        // (Window, TextButton, etc.) - this class was overridden to handle "new" Vis widgets, like VisImageTextButton.
        return new Class<?>[] { Window.class, Label.class, TextButton.class, TextField.class, TextArea.class,
                CheckBox.class, ImageTextButton.class, Dialog.class, VisTextButton.class, VisWindow.class,
                VisDialog.class, VisImageTextButton.class };
    }
}
