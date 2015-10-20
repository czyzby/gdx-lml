package com.github.czyzby.gdx.idle.view.dialog;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;

@SuppressWarnings("static-method")
@ViewDialog(value = "templates/dialogs/credits.lml", id = "credits", cacheInstance = true)
public class CreditsDialog implements ActionContainer {
    @LmlAction("author0")
    private void openMjUrl() {
        Gdx.net.openURI("https://github.com/czyzby/gdx-autumn-mvc");
    }

    @LmlAction("author1")
    private void openOpenGameArtUrl() {
        Gdx.net.openURI("http://opengameart.org/");
    }

    @LmlAction("author2")
    private void openKenneyUrl() {
        Gdx.net.openURI("http://opengameart.org/content/pixel-ui-pack-750-assets");
    }

    @LmlAction("author3")
    private void openJeromUrl() {
        Gdx.net.openURI("http://opengameart.org/content/16x16-studded-metal-fonts");
    }

    @LmlAction("author4")
    private void openWmkUrl() {
        Gdx.net.openURI("http://www.fontsquirrel.com/fonts/modern-antiqua");
    }

    @LmlAction("author5")
    private void openClintUrl() {
        Gdx.net.openURI("http://opengameart.org/content/boxy-bold-font");
    }

    @LmlAction("author6")
    private void openDenziUrl() {
        Gdx.net.openURI("http://opengameart.org/content/denzis-assets");
    }

    @LmlAction("author7")
    private void openUsAirForceUrl() {
        Gdx.net.openURI("http://opengameart.org/content/holst-the-planets-suite");
    }

    @LmlAction("author8")
    private void openCynicmusicUrl() {
        Gdx.net.openURI("http://opengameart.org/content/crystal-cave-mysterious-ambience-seamless-loop");
    }

    @LmlAction("author9")
    private void openQubodupUrl() {
        Gdx.net.openURI("http://opengameart.org/content/punch");
    }

    @LmlAction("author10")
    private void openDoKashiteruUrl() {
        Gdx.net.openURI("http://opengameart.org/content/3-heal-spells");
    }
}