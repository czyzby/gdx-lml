package com.github.czyzby.lml.parser;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

public interface LmlTagDataParser<Widget extends Actor> {
	/** Should be used when the tag is a child widget, closing in the same tag as it was created.
	 *
	 * @param lmlTagData contains tag data.
	 * @return widget created with tag data. */
	Widget parseChild(LmlTagData lmlTagData, LmlParser parser);

	/** Should be used when the tag is a parent widget, closing in another (not yet parsed) tag.
	 *
	 * @param lmlTagData contains tag data.
	 * @param parent of currently added parent. Can be null.
	 * @return widget created with tag data, storing additional information about behavior as a parent. */
	LmlParent<Widget> parseParent(LmlTagData lmlTagData, LmlParser parser, LmlParent<?> parent);

	/** @param parser will be added to the parsed attributes list. Default attributes can be also set globally
	 *            by accessing static methods of tag parsers. */
	void registerAttributeParser(LmlTagAttributeParser parser);

	/** @param attributeName parser connected with the attribute name will be removed, if exists. Default
	 *            attributes can be also unset globally by accessing static methods of tag parsers. */
	void unregisterAttributeParser(String attributeName);
}
