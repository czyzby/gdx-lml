package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.ActorConsumer;
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
	DISABLED("disabled") {
		@Override
		public void apply(final ProgressBar progressBar, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			progressBar.setDisabled(LmlAttributes.parseBoolean(progressBar, parser, attributeValue));
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
	},
	ON_COMPLETE("onComplete") {
		@Override
		protected void apply(final ProgressBar progressBar, final LmlParser parser,
				final String attributeValue, final LmlTagData lmlTagData) {
			final ActorConsumer<Object, Object> action =
					LmlAttributes.parseAction(progressBar, parser, attributeValue);
			progressBar.addListener(new ChangeListener() {
				@Override
				public void changed(final ChangeEvent event, final Actor actor) {
					if (progressBar.getValue() >= progressBar.getMaxValue()) {
						action.consume(actor);
					}
				}
			});
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