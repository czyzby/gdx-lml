package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.parent.ImageLmlParent;

public class ImageLmlTagDataParser extends AbstractLmlTagDataParser<Image> {
	public static final String SOURCE_ATTRIBUTE = "SRC";
	public static final String ALIGNMENT_ATTRIBUTE = "IMAGEALIGN";

	@Override
	protected Image parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final Image image = new Image(parser.getSkin(), getStyleName(lmlTagData, parser));
		if (lmlTagData.containsAttribute(FILL_PARENT_ATTRIBUTE)) {
			image.setFillParent(parseBoolean(lmlTagData, FILL_PARENT_ATTRIBUTE, parser, image));
		}
		if (lmlTagData.containsAttribute(ALIGNMENT_ATTRIBUTE)) {
			image.setAlign(parseAlignment(lmlTagData, ALIGNMENT_ATTRIBUTE, parser, image));
		}
		return image;
	}

	@Override
	protected boolean containsStyleAttribute(final LmlTagData lmlTagData) {
		return lmlTagData.containsAttribute(SOURCE_ATTRIBUTE) || super.containsStyleAttribute(lmlTagData);
	}

	@Override
	protected String getStyleName(final LmlTagData lmlTagData, final LmlParser parser) {
		if (lmlTagData.containsAttribute(SOURCE_ATTRIBUTE)) {
			return parser.parseStringData(lmlTagData.getAttribute(SOURCE_ATTRIBUTE), null);
		}
		return super.getStyleName(lmlTagData, parser);
	}

	@Override
	protected LmlParent<Image> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
			final LmlParent<?> parent) {
		return new ImageLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent, parser);
	}

}
