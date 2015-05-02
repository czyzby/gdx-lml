package com.github.czyzby.gdx.idle.view;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.impl.StandardViewShower;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.autumn.mvc.stereotype.ViewActor;
import com.github.czyzby.autumn.mvc.stereotype.ViewStage;
import com.github.czyzby.gdx.idle.logic.GameManager;
import com.github.czyzby.lml.parser.impl.annotation.ViewAction;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;

@View(value = "templates/game.lml", id = "game", themes = { "sfx/jupiter.ogg", "sfx/mars.ogg",
		"sfx/uranus.ogg" })
// TODO themes
// View shower could be implemented instead of extending StandardViewShower, but we don't want to change the
// functionality - just expand it.
public class GameView extends StandardViewShower implements ActionContainer {
	private static final float ANIMATION_TIME = 0.15f;
	private static final float SPRITE_OFFSET = 4f;

	// Using publics to avoid boilerplate code - don't do this at home, kids!
	@ViewActor("location")
	public Image locationImage;
	@ViewActor("monster")
	public Image monsterImage;
	@ViewActor("player")
	public Image playerImage;
	@ViewActor("locationId")
	public Label locationLabel;
	@ViewActor("monsterId")
	public Label monsterLabel;
	@ViewActor("monsterHp")
	public ProgressBar monsterHealthBar;
	@ViewActor("playerHp")
	public ProgressBar playerHealthBar;
	@ViewActor("monsterHpAmount")
	public Label monsterHealthLabel;
	@ViewActor("playerHpAmount")
	public Label playerHealthLabel;
	@ViewActor("monstersKilled")
	public Label monstersKilledLabel;

	@ViewStage
	public Stage stage;

	@Inject
	private GameManager gameManager;

	@ViewAction("tapPlayer")
	private void handlePlayerClick() {
		gameManager.handlePlayerClick();
	}

	@ViewAction("tapMonster")
	private void handleMonsterClick() {
		gameManager.handleMonsterClick();
	}

	@Override
	public void show(final Stage stage, final Action action) {
		gameManager.initiateGame();
		stage.addAction(Actions.forever(Actions.sequence(
				Actions.delay(GameManager.ATTACK_DELAY - ANIMATION_TIME),
				Actions.run(new ImageUpdateAction(-SPRITE_OFFSET, playerImage)),
				Actions.run(new ImageUpdateAction(SPRITE_OFFSET, monsterImage)), Actions.run(new Runnable() {
					@Override
					public void run() {
						gameManager.update();
					}
				}), Actions.delay(ANIMATION_TIME),
				Actions.run(new ImageUpdateAction(SPRITE_OFFSET, playerImage)),
				Actions.run(new ImageUpdateAction(-SPRITE_OFFSET, monsterImage)))));
		super.show(stage, action);
	}

	private static class ImageUpdateAction implements Runnable {
		private final float offset;
		private final Image image;

		public ImageUpdateAction(final float offset, final Image image) {
			this.offset = offset;
			this.image = image;
		}

		@Override
		public void run() {
			image.setX(image.getX() + offset);
		}
	}
}