package com.github.czyzby.kiwi.util.gdx.scene2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

/** Contains common methods for Scene2D actors.
 *
 * @author MJ */
public class Actors {
	private Actors() {
	}

	/** Centers passed actor's position on its assigned stage according to their sizes. */
	public static void centerActor(final Actor actor) {
		centerActor(actor, actor.getStage());
	}

	/** Centers passed actor's position on the given stage according to their sizes. */
	public static void centerActor(final Actor actor, final Stage stage) {
		actor.setPosition((int) (stage.getWidth() / 2f - actor.getWidth() / 2f),
				(int) (stage.getHeight() / 2f - actor.getHeight() / 2f));
	}

	/** When called BEFORE resizing the stage, moves the actor to match the same aspect ratio as before. Useful
	 * for windows and dialogs in screen viewports.
	 *
	 * @param actor will be repositioned.
	 * @param newScreenSizeInStageCoords screen coords processed by stage. */
	public static void updateActorPosition(final Actor actor, final Vector2 newScreenSizeInStageCoords) {
		updateActorPosition(actor, actor.getStage(), newScreenSizeInStageCoords);
	}

	/** When called BEFORE resizing the stage, moves the actor to match the same aspect ratio as before. Useful
	 * for windows and dialogs in screen viewports.
	 *
	 * @param actor will be repositioned.
	 * @param stage has to be before resizing.
	 * @param newScreenSizeInStageCoords screen coords processed by stage. */
	public static void updateActorPosition(final Actor actor, final Stage stage,
			final Vector2 newScreenSizeInStageCoords) {
		actor.setPosition((int) ((actor.getX() + actor.getWidth() / 2f) / stage.getWidth()
				* newScreenSizeInStageCoords.x - actor.getWidth() / 2f),
				(int) ((actor.getY() + actor.getHeight() / 2f) / stage.getHeight()
						* newScreenSizeInStageCoords.y - actor.getHeight() / 2f));
	}

	/** Null-safe check if the actor has a stage. Especially useful for dialogs.
	 *
	 * @return true if actor is not null and has a stage. */
	public static boolean isShown(final Actor actor) {
		return actor != null && actor.getStage() != null;
	}

	/** Null-safe method to hide dialogs.
	 *
	 * @param dialog will be hidden if exists. */
	public static void hide(final Dialog dialog) {
		if (dialog != null) {
			dialog.hide();
		}
	}

	/** Null-safe method for clearing keyboard focus.
	 *
	 * @param actor if is not null, will extract its stage. If stage is not null, will clear its keyboard
	 *            focused actor, if one is present. */
	public static void clearKeyboardFocus(final Actor actor) {
		if (actor != null) {
			clearKeyboardFocus(actor.getStage());
		}
	}

	/** Null-safe method for clearing keyboard focus.
	 *
	 * @param stage will have its keyboard focused actor cleared, if one is present. */
	public static void clearKeyboardFocus(final Stage stage) {
		if (stage != null) {
			stage.setKeyboardFocus(null);
		}
	}

	/** Null-safe method that clears a Cell of a Table.
	 *
	 * @param tableCell can be null. Will have its actor removed and will be reset. */
	public static void clearCell(final Cell<?> tableCell) {
		if (tableCell != null) {
			tableCell.clearActor();
			tableCell.reset();
		}
	}
}
