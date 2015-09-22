package com.github.czyzby.gdx.idle.view.dialog;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.parser.impl.annotation.ViewAction;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;

@SuppressWarnings("static-method")
@ViewDialog(value = "templates/dialogs/credits.lml", id = "credits", cacheInstance = true)
public class CreditsDialog implements ActionContainer {
	@ViewAction("author0")
	private void openMjUrl() {
		Gdx.net.openURI("https://github.com/czyzby/gdx-autumn-mvc");
	}

	@ViewAction("author1")
	private void openOpenGameArtUrl() {
		Gdx.net.openURI("http://opengameart.org/");
	}

	@ViewAction("author2")
	private void openKenneyUrl() {
		Gdx.net.openURI("http://opengameart.org/content/pixel-ui-pack-750-assets");
	}

	@ViewAction("author3")
	private void openJeromUrl() {
		Gdx.net.openURI("http://opengameart.org/content/16x16-studded-metal-fonts");
	}

	@ViewAction("author4")
	private void openWmkUrl() {
		Gdx.net.openURI("http://www.fontsquirrel.com/fonts/modern-antiqua");
	}

	@ViewAction("author5")
	private void openClintUrl() {
		Gdx.net.openURI("http://opengameart.org/content/boxy-bold-font");
	}

	@ViewAction("author6")
	private void openDenziUrl() {
		Gdx.net.openURI("http://opengameart.org/content/denzis-assets");
	}

	@ViewAction("author7")
	private void openUsAirForceUrl() {
		Gdx.net.openURI("http://opengameart.org/content/holst-the-planets-suite");
	}

	@ViewAction("author8")
	private void openCynicmusicUrl() {
		Gdx.net.openURI("http://opengameart.org/content/crystal-cave-mysterious-ambience-seamless-loop");
	}

	@ViewAction("author9")
	private void openQubodupUrl() {
		Gdx.net.openURI("http://opengameart.org/content/punch");
	}

	@ViewAction("author10")
	private void openDoKashiteruUrl() {
		Gdx.net.openURI("http://opengameart.org/content/3-heal-spells");
	}
}