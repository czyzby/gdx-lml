package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public enum WindowLmlTagAttributeParser implements LmlTagAttributeParser {
	TITLE("title") {
		@Override
		protected void apply(final Window window, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			window.setTitle(LmlAttributes.parseString(window, parser, attributeValue));
		}
	},
	TITLE_ALIGNMENT("titleAlign", "titleAlignment") {
		@Override
		protected void apply(final Window window, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			window.setTitleAlignment(LmlAttributes.parseAlignment(window, parser, attributeValue));
		}
	},
	KEEP_WITHIN_STAGE("keepWithin", "keepWithinStage") {
		@Override
		protected void apply(final Window window, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			window.setKeepWithinStage(LmlAttributes.parseBoolean(window, parser, attributeValue));
		}
	},
	MODAL("modal") {
		@Override
		protected void apply(final Window window, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			window.setModal(LmlAttributes.parseBoolean(window, parser, attributeValue));
		}
	},
	MOVABLE("movable") {
		@Override
		protected void apply(final Window window, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			window.setMovable(LmlAttributes.parseBoolean(window, parser, attributeValue));
		}
	},
	RESIZABLE("resizable") {
		@Override
		protected void apply(final Window window, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			window.setResizable(LmlAttributes.parseBoolean(window, parser, attributeValue));
		}
	},
	RESIZE_BORDER("resizeBorder") {
		@Override
		protected void apply(final Window window, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			window.setResizeBorder(LmlAttributes.parseInt(window, parser, attributeValue));
		}
	};
	private final String[] aliases;

	private WindowLmlTagAttributeParser(final String... aliases) {
		this.aliases = aliases;
	}

	@Override
	public void apply(final Object actor, final LmlParser parser, final String attributeValue,
			final LmlTagData lmlTagData) {
		apply((Window) actor, parser, attributeValue, lmlTagData);
	}

	protected abstract void apply(Window window, LmlParser parser, String attributeValue,
			LmlTagData lmlTagData);

	@Override
	public String[] getAttributeNames() {
		return aliases;
	}
}
