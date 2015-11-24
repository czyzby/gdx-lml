package com.github.czyzby.controller.dialog;

import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;

/** This is a credits dialog, which can be shown in any view by using "show:credits" LML action or - in Java code -
 * through {@link InterfaceService#showDialog(Class)} method. */
@ViewDialog(id = "credits", value = "templates/dialogs/credits.lml")
public class CreditsController {
}
