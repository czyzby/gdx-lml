package com.github.czyzby.lml.parser.impl.dto;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.github.czyzby.lml.parser.LmlParser;

/** Contains data of an open widget.
 *
 * @author MJ */
public interface LmlParent<Widget extends Actor> {
	/** @return tag used to create the parental widget. */
	String getTagName();

	/** @return value appended with ID attribute in tag. Optional, can be null. */
	String getId();

	/** @return line number on which the tag was opened. */
	int getLineNumber();

	/** When the parent is current processed tag, it can receive and handle all read characters before
	 * pre-parsing. While most parent ignore this input by default, it can be used to build complex macros.
	 *
	 * @return if false, parser will ignore this character. */
	boolean acceptCharacter(char character);

	/** @return enclosing tag, if any. */
	LmlParent<?> getParent();

	/** @return actor created by the tag. */
	Widget getActor();

	/** @param child child of the widget stored in this object. Generally appended to the widget one way or
	 *            another.
	 * @param childTagData tag data used to create child widget. May contain attributes relevant to the
	 *            parent. */
	void handleChild(Actor child, LmlTagData childTagData, LmlParser parser);

	/** @param data is between tag opening and ending. May or may not be appended to the actor. */
	void handleDataBetweenTags(String data, LmlParser parser);

	/** Has to be triggered when this widget's tag is closed. */
	void closeTag(LmlParser parser);

	/** @return node of the actor, if in a tree. */
	Tree.Node getNode();
}
