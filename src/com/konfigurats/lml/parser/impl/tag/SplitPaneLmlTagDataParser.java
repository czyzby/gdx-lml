package com.konfigurats.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.tag.parent.SplitPaneLmlParent;

public class SplitPaneLmlTagDataParser extends AbstractGroupLmlTagDataParser<SplitPane> {
	public static final String VERTICAL_ATTRIBUTE = "VERTICAL";
	public static final String SPLIT_AMOUNT_ATTRIBUTE = "SPLITAMOUNT";
	public static final String MIN_SPLIT_AMOUNT_ATTRIBUTE = "MINSPLITAMOUNT";
	public static final String MAX_SPLIT_AMOUNT_ATTRIBUTE = "MAXSPLITAMOUNT";

	@Override
	protected SplitPane parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final SplitPane splitPane = prepareWidget(lmlTagData, parser);
		setFillParent(splitPane, lmlTagData, parser);
		if (lmlTagData.containsAttribute(SPLIT_AMOUNT_ATTRIBUTE)) {
			splitPane.setSplitAmount(parseFloat(lmlTagData, SPLIT_AMOUNT_ATTRIBUTE, parser, splitPane));
		}
		if (lmlTagData.containsAttribute(MIN_SPLIT_AMOUNT_ATTRIBUTE)) {
			splitPane
					.setMinSplitAmount(parseFloat(lmlTagData, MIN_SPLIT_AMOUNT_ATTRIBUTE, parser, splitPane));
		}
		if (lmlTagData.containsAttribute(MAX_SPLIT_AMOUNT_ATTRIBUTE)) {
			splitPane
					.setMaxSplitAmount(parseFloat(lmlTagData, MAX_SPLIT_AMOUNT_ATTRIBUTE, parser, splitPane));
		}
		return splitPane;
	}

	private SplitPane prepareWidget(final LmlTagData lmlTagData, final LmlParser parser) {
		if (containsStyleAttribute(lmlTagData)) {
			return new SplitPane(null, null, parseBoolean(lmlTagData, VERTICAL_ATTRIBUTE, parser, null),
					parser.getSkin(), getStyleName(lmlTagData, parser));
		}
		// Default style name is not "default", using no style name arg constructor.
		return new SplitPane(null, null, parseBoolean(lmlTagData, VERTICAL_ATTRIBUTE, parser, null),
				parser.getSkin());
	}

	@Override
	protected LmlParent<SplitPane> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return new SplitPaneLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent, parser);
	}

}
