package com.github.czyzby.gdx.idle.view.dialog;

import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.gdx.idle.logic.GameManager;
import com.github.czyzby.lml.parser.impl.annotation.ViewAction;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;

@ViewDialog("templates/dialogs/loss.lml")
public class LossDialog implements ActionContainer {
	@Inject
	private GameManager gameManager;

	@ViewAction("getKilledMonstersAmount")
	private int getKilledMonstersAmount() {
		return gameManager.getKilledMonstersAmount();
	}
}