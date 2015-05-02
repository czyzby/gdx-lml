package com.github.czyzby.gdx.idle.view.dialog;

import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.parser.impl.annotation.ViewAction;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;

@ViewDialog(value = "templates/dialogs/exit.lml", id = "exit", cacheInstance = true)
public class ExitPromptDialog implements ActionContainer {
	@Inject
	private InterfaceService interfaceService;

	@ViewAction("exit")
	public void exitApplication() {
		interfaceService.exitApplication();
	}
}