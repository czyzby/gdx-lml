package com.konfigurats.lml.parser.impl.tag;

import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.tag.parent.RowLmlParent;
import com.konfigurats.lml.parser.impl.util.RowActor;

public class RowLmlTagDataParser extends AbstractLmlTagDataParser<RowActor> {
	@Override
	protected RowActor parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		return RowActor.ROW;
	}

	@Override
	protected LmlParent<RowActor> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return new RowLmlParent(lmlTagData, parent, parser);
	}
}
