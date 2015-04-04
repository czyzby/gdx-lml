package com.github.czyzby.lml.parser;

import com.github.czyzby.lml.parser.impl.dto.LmlTagData;

/** Allows to parse tag attributes, modifying created actors.
 *
 * @author MJ */
public interface LmlTagAttributeParser {
	/** @return names of the attribute aliases as they should appear in the LML documents. */
	public String[] getAttributeNames();

	/** @param actor will apply attribute effect to this actor.
	 * @param parser is parsing the LML document containing the actor.
	 * @param attributeValue unparsed value of attribute as it appears in actor's tag.
	 * @param lmlTagData raw tag data with all attributes. Might be necessary to parse if multiple attributes
	 *            are somehow connected. */
	public void apply(Object actor, LmlParser parser, String attributeValue, LmlTagData lmlTagData);
}
