package com.konfigurats.lml.parser.impl.tag.parent;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;

public class ListLmlParent extends AbstractLmlParent<List<String>> {
	public ListLmlParent(final LmlTagData tagData, final List<String> actor, final LmlParent<?> parent,
			final LmlParser parser) {
		super(tagData, actor, parent, parser);
	}

	@Override
	public void handleValidChild(final Actor child, final LmlTagData childTagData, final LmlParser parser) {
		if (child instanceof Label) {
			final Label label = (Label) child;
			if (label.getText().length > 0) {
				actor.getItems().add(label.getText().toString());
			}
		} else {
			throwErrorIfStrict(parser, "List parent cannot have other children than labels. Received child: "
					+ child + ".");
		}
	}

	@Override
	public void doOnTagClose(final LmlParser parser) {
		if (actor.getUserObject() != null) {
			actor.setSelectedIndex((Integer) actor.getUserObject());
			actor.setUserObject(null);
		}
		actor.invalidateHierarchy();
	}

	@Override
	protected void handleValidDataBetweenTags(final String data, final LmlParser parser) {
		actor.getItems().add(parser.parseStringData(data, actor));
	}
}
