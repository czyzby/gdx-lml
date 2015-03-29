package com.konfigurats.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.tag.LabelLmlTagDataParser;
import com.konfigurats.lml.util.gdx.collection.GdxArrays;

/** Allows to create a label with text passed between tags instead of an attribute.
 *
 * @author MJ */
public class LabelLmlParent extends AbstractLmlParent<Label> {
	private final StringBuilder text;
	private final boolean containedInitialText;

	public LabelLmlParent(final LmlTagData tagData, final Label actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
		text = new StringBuilder(actor.getText());
		containedInitialText = actor.getText().length > 0;
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		if (child != null) {
			throwErrorIfStrict(parser, "Labels cannot have children.");
		}
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
		actor.setText(text);
		if (LabelLmlTagDataParser.MULTILINE_ATTRIBUTE.equals(actor.getUserObject())) {
			actor.setUserObject(null);
		}
		if (parent != null && parent instanceof ListLmlParent) {
			final ListLmlParent listParent = (ListLmlParent) parent;
			if (containedInitialText) {
				// Making sure label is not added twice if it contained both text attribute and text between
				// tags.
				GdxArrays.removeLast(listParent.getActor().getItems());
			}
			((ListLmlParent) parent).handleChild(actor, null, parser);
		}
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		if (LabelLmlTagDataParser.MULTILINE_ATTRIBUTE.equals(actor.getUserObject()) && text.length() > 0) {
			text.append(NEW_LINE);
		}
		text.append(parser.parseStringData(data, actor));
	}
}
