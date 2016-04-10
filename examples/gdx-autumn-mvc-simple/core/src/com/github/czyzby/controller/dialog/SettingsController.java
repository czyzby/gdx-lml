package com.github.czyzby.controller.dialog;

import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.kiwi.util.gdx.scene2d.Actors;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.kotcrab.vis.ui.widget.VisDialog;

/** This is a settings dialog, which can be shown in any view by using "show:settings" LML action or - in Java code -
 * through {@link InterfaceService#showDialog(Class)} method. */
@ViewDialog(id = "settings", value = "templates/dialogs/settings.lml")
public class SettingsController implements ActionContainer {
    /** {@link LmlActor} annotation will make the LML parser look for an actor with "id=dialog" attribute and inject it
     * into this field. This allows us to get a reference of the dialog we created in settings.lml. */
    @LmlActor("dialog") VisDialog dialog;

    /** This method will hide the current dialog instance if it is currently shown on the stage. Will be available
     * through "quit" (annotation argument) and "hideDialog" (method name) IDs. */
    @LmlAction("quit")
    public void hideDialog() {
        if (Actors.isShown(dialog)) {
            dialog.hide();
        }
    }
}
