package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum ProgressBarLmlTagAttributeParser implements LmlTagAttributeParser {
	FILL_PARENT("fillParent") {
		@Override
		public void apply(final ProgressBar progressBar, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			progressBar.setFillParent(LmlAttributes.parseBoolean(progressBar, parser, attributeValue));
		}
	},
	VALUE("value") {
		@Override
		protected void apply(final ProgressBar progressBar, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			progressBar.setValue(LmlAttributes.parseFloat(progressBar, parser, attributeValue));
		}
	},
	ANIMATE_DURATION("animateDuration") {
		@Override
		protected void apply(final ProgressBar progressBar, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			progressBar.setAnimateDuration(LmlAttributes.parseFloat(progressBar, parser, attributeValue));
		}
	};
	private final String[] aliases;

	private ProgressBarLmlTagAttributeParser(final String... aliases) {
		this.aliases = aliases;
	}

	@Override
	public void apply(final Object actor, final LmlParser parser, final String attributeValue,
			final LmlTagData lmlTagData) {
		apply((ProgressBar) actor, parser, attributeValue, lmlTagData);
	}

	protected abstract void apply(ProgressBar progressBar, LmlParser parser, String attributeValue,
			LmlTagData lmlTagData);

	@Override
	public String[] getAttributeNames() {
		return aliases;
	}
}
