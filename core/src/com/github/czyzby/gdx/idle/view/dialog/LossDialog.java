package com.github.czyzby.gdx.idle.view.dialog;

import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.gdx.idle.logic.GameManager;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;

@ViewDialog("templates/dialogs/loss.lml")
public class LossDialog implements ActionContainer {
    @Inject private GameManager gameManager;

    @LmlAction("getKilledMonstersAmount")
    private int getKilledMonstersAmount() {
        return gameManager.getKilledMonstersAmount();
    }
}