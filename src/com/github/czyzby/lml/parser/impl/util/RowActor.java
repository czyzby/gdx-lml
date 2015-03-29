package com.github.czyzby.lml.parser.impl.util;

import com.badlogic.gdx.scenes.scene2d.Actor;

/** Mock-up actor. Allows to pass a row command as a tag. Use ROW reference, do not initiate.
 *
 * @author MJ */
public class RowActor extends Actor {
	public static final RowActor ROW = new RowActor();

	private RowActor() {
	}
}
