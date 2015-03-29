package com.konfigurats.lml.parser.impl.dto;

import com.badlogic.gdx.scenes.scene2d.Stage;

/** Allows to pass actions executed when widgets are attached to a stage.
 *
 * @author MJ */
public interface StageAttacher {
	void attachToStage(Stage stage);
}
