package com.konfigurats.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.tag.parent.TextButtonLmlParent;

/** Additionally to button and table tags, text button supports: <ul> <li>text - button's label text.</li>
 * <li>textAlign - button's label alignment.</li> <li>textWrap - button's label wrapping setting. Use with
 * caution. true/false.</li> <li>textEllipsis - button's label ellipsis setting. true/false.</li> </ul>
 *
 * @author MJ */
public class TextButtonLmlTagDataParser<TextButtonWidget extends TextButton> extends
		ButtonLmlTagDataParser<TextButtonWidget> {
	public static final String LINK_ATTRIBUTE = "HREF";
	public static final String TEXT_ATTRIBUTE = "TEXT";
	public static final String TEXT_ALIGN_ATTRIBUTE = "TEXTALIGN";
	public static final String TEXT_WRAP_ATTRIBUTE = "TEXTWRAP";
	public static final String TEXT_ELLIPSIS_ATTRIBUTE = "TEXTELLIPSIS";

	@Override
	protected TextButtonWidget parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final TextButtonWidget textButton = super.parseChildWithValidTag(lmlTagData, parser);
		final Label buttonLabel = textButton.getLabel();
		if (lmlTagData.containsAttribute(LINK_ATTRIBUTE)) {
			addOnClickAction(parser, textButton, lmlTagData.getAttribute(LINK_ATTRIBUTE));
		}
		if (lmlTagData.containsAttribute(TEXT_ALIGN_ATTRIBUTE)) {
			buttonLabel.setAlignment(parseAlignment(lmlTagData, TEXT_ALIGN_ATTRIBUTE, parser, textButton));
		}
		buttonLabel.setWrap(parseBoolean(lmlTagData, TEXT_WRAP_ATTRIBUTE, parser, textButton));
		buttonLabel.setEllipsis(parseBoolean(lmlTagData, TEXT_ELLIPSIS_ATTRIBUTE, parser, textButton));

		return textButton;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected TextButtonWidget prepareNewTable(final LmlTagData lmlTagData, final LmlParser parser) {
		final TextButtonWidget textButton =
				(TextButtonWidget) new TextButton(EMPTY_STRING, parser.getSkin(), getStyleName(lmlTagData,
						parser));
		textButton.setText(parseString(lmlTagData, TEXT_ATTRIBUTE, parser, textButton));
		return textButton;
	};

	@Override
	@SuppressWarnings("unchecked")
	protected LmlParent<TextButtonWidget> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return (LmlParent<TextButtonWidget>) new TextButtonLmlParent<TextButton>(lmlTagData,
				parseChildWithValidTag(lmlTagData, parser), parent, parser);
	}
}
