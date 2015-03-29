package com.konfigurats.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.tag.parent.LabelLmlParent;

/** Allows to specify: <ul> <li> text: label's text.</li> <li> textAlign: text's alignment. Does not collide
 * with table cell alignment.</li> <li> wrap: if true, label will be wrapped.</li> <li> ellipsis: if true,
 * ellipsis will will be applied.</li> </ul>
 *
 * @author MJ */
public class LabelLmlTagDataParser extends AbstractLmlTagDataParser<Label> {
	public static final String TEXT_ATTRIBUTE = "TEXT";
	public static final String TEXT_ALIGN_ATTRIBUTE = "TEXTALIGN";
	public static final String WRAP_ATTRIBUTE = "WRAP";
	public static final String ELLIPSIS_ATTRIBUTE = "ELLIPSIS";
	public static final String MULTILINE_ATTRIBUTE = "MULTILINE";

	@Override
	protected Label parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final Label label = new Label(EMPTY_STRING, parser.getSkin(), getStyleName(lmlTagData, parser));
		label.setText(parseString(lmlTagData, TEXT_ATTRIBUTE, parser, label));
		if (lmlTagData.containsAttribute(TEXT_ALIGN_ATTRIBUTE)) {
			label.setAlignment(parseAlignment(lmlTagData, TEXT_ALIGN_ATTRIBUTE, parser, label));
		}
		if (parseBoolean(lmlTagData, MULTILINE_ATTRIBUTE, parser, label)) {
			label.setUserObject(MULTILINE_ATTRIBUTE);
		}
		label.setWrap(parseBoolean(lmlTagData, WRAP_ATTRIBUTE, parser, label));
		label.setEllipsis(parseBoolean(lmlTagData, ELLIPSIS_ATTRIBUTE, parser, label));
		return label;
	}

	@Override
	protected LmlParent<Label> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
			final LmlParent<?> parent) {
		return new LabelLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent, parser);
	}

}
