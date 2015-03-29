package com.konfigurats.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.util.RowActor;

public class RowLmlParent extends AbstractLmlParent<RowActor> {
	public RowLmlParent(final LmlTagData tagData, final LmlParent<?> parent, final LmlParser parser) {
		super(tagData, RowActor.ROW, parent, parser);
		if (parent == null || !(parent instanceof TableLmlParent<?>) && !(parent instanceof RowLmlParent)) {
			throwErrorIfStrict(parser, "Row cannot be used without a table parent.");
		}
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		parent.handleChild(child, childTagData, parser);
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
		LmlParent<?> parent = this.parent;
		while (!(parent instanceof TableLmlParent<?>)) {
			parent = parent.getParent();
		}
		((TableLmlParent<?>) parent).getActor().row();
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		parent.handleDataBetweenTags(data, parser);
	}

}
