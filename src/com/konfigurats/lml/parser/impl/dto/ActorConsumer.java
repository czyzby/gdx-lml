package com.konfigurats.lml.parser.impl.dto;

/** A simple interface that allows to assign actions to actors.
 *
 * @author MJ */
public interface ActorConsumer<ReturnType, Widget> {
	/** @param actor triggered when a specified action concerning this actor was fired. */
	ReturnType consume(Widget actor);
}
