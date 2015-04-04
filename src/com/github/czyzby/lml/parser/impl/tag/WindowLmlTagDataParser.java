package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.dto.StageAttacher;
import com.github.czyzby.lml.parser.impl.tag.attribute.WindowLmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.tag.parent.WindowLmlParent;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;

public class WindowLmlTagDataParser extends TableLmlTagDataParser {
	// Unique attributes, have to be parsed in a specific way.
	public static final String POSITION_X_ATTRIBUTE = "POSITIONX";
	public static final String POSITION_Y_ATTRIBUTE = "POSITIONY";

	private static final ObjectMap<String, LmlTagAttributeParser> ATTRIBUTE_PARSERS;

	private final ObjectMap<String, LmlTagAttributeParser> attributeParsers =
			new ObjectMap<String, LmlTagAttributeParser>(ATTRIBUTE_PARSERS);

	static {
		ATTRIBUTE_PARSERS = GdxMaps.newObjectMap();
		for (final LmlTagAttributeParser parser : WindowLmlTagAttributeParser.values()) {
			registerParser(parser);
		}
	}

	public static void registerParser(final LmlTagAttributeParser parser) {
		for (final String alias : parser.getAttributeNames()) {
			ATTRIBUTE_PARSERS.put(alias.toUpperCase(), parser);
		}
	}

	public static void unregisterParser(final String withAlias) {
		ATTRIBUTE_PARSERS.remove(withAlias);
	}

	@Override
	protected void parseAttributes(final LmlTagData lmlTagData, final LmlParser parser, final Actor actor) {
		super.parseAttributes(lmlTagData, parser, actor);
		for (final Entry<String, String> attribute : lmlTagData.getAttributes()) {
			if (attributeParsers.containsKey(attribute.key)) {
				attributeParsers.get(attribute.key).apply(actor, parser, attribute.value, lmlTagData);
			}
		}
	}

	@Override
	public void registerAttributeParser(final LmlTagAttributeParser parser) {
		for (final String alias : parser.getAttributeNames()) {
			attributeParsers.put(alias.toUpperCase(), parser);
		}
	}

	@Override
	public void unregisterAttributeParser(final String attributeName) {
		attributeParsers.remove(attributeName);
	}

	@Override
	protected Window parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final Window window = getNewInstanceOfWindow(lmlTagData, parser);
		addStageAttacher(lmlTagData, parser, window);
		return window;
	}

	protected Window getNewInstanceOfWindow(final LmlTagData lmlTagData, final LmlParser parser) {
		return new Window(Strings.EMPTY_STRING, parser.getSkin(), getStyleName(lmlTagData, parser));
	}

	private void addStageAttacher(final LmlTagData lmlTagData, final LmlParser parser, final Window window) {
		final float x;
		final float y;
		final PositionConverter xConverter;
		final PositionConverter yConverter;
		if (lmlTagData.containsAttribute(POSITION_X_ATTRIBUTE)) {
			final String xAttribute =
					LmlAttributes.parseString(window, parser, lmlTagData.getAttribute(POSITION_X_ATTRIBUTE));
			if (xAttribute.charAt(xAttribute.length() - 1) == PERCENT_OPERATOR) {
				x = Float.parseFloat(xAttribute.substring(0, xAttribute.length() - 1));
				xConverter = PositionConverter.PERCENT;
			} else {
				x = Float.parseFloat(xAttribute);
				xConverter = PositionConverter.ABSOLUTE;
			}
		} else {
			x = 0f;
			xConverter = PositionConverter.CENTER;
		}

		if (lmlTagData.containsAttribute(POSITION_Y_ATTRIBUTE)) {
			final String yAttribute =
					LmlAttributes.parseString(window, parser, lmlTagData.getAttribute(POSITION_Y_ATTRIBUTE));
			if (yAttribute.charAt(yAttribute.length() - 1) == PERCENT_OPERATOR) {
				y = Float.parseFloat(yAttribute.substring(0, yAttribute.length() - 1));
				yConverter = PositionConverter.PERCENT;
			} else {
				y = Float.parseFloat(yAttribute);
				yConverter = PositionConverter.ABSOLUTE;
			}
		} else {
			y = 0f;
			yConverter = PositionConverter.CENTER;
		}

		window.setUserObject(getWindowStageAttacher(window, parser, lmlTagData, x, y, xConverter, yConverter));
	}

	protected WindowStageAttacher getWindowStageAttacher(final Window window, final LmlParser parser,
			final LmlTagData lmlTagData, final float x, final float y, final PositionConverter xConverter,
			final PositionConverter yConverter) {
		return new WindowStageAttacher(window, x, y, xConverter, yConverter);
	}

	@Override
	protected LmlParent<Table> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
			final LmlParent<?> parent) {
		return new WindowLmlParent(lmlTagData, (Window) parseChild(lmlTagData, parser), parent, parser);
	}

	public static enum PositionConverter {
		CENTER {
			@Override
			public float convertX(final float x, final Stage stage, final Window window) {
				return (int) (stage.getWidth() / 2f - window.getWidth() / 2f);
			}

			@Override
			public float convertY(final float y, final Stage stage, final Window window) {
				return (int) (stage.getHeight() / 2f - window.getHeight() / 2f);
			}
		},
		ABSOLUTE {
			@Override
			public float convertX(final float x, final Stage stage, final Window window) {
				return x;
			}

			@Override
			public float convertY(final float y, final Stage stage, final Window window) {
				return y;
			}
		},
		PERCENT {
			@Override
			public float convertX(final float x, final Stage stage, final Window window) {
				return (int) (stage.getWidth() * x);
			}

			@Override
			public float convertY(final float y, final Stage stage, final Window window) {
				return (int) (stage.getHeight() * y);
			}
		};

		public abstract float convertX(float x, Stage stage, Window window);

		public abstract float convertY(float y, Stage stage, Window window);
	}

	public static class WindowStageAttacher implements StageAttacher {
		protected final Window window;
		private final float x, y;
		private final PositionConverter xConverter, yConverter;

		public WindowStageAttacher(final Window window, final float x, final float y,
				final PositionConverter xConverter, final PositionConverter yConverter) {
			this.window = window;
			this.x = x;
			this.y = y;
			this.xConverter = xConverter;
			this.yConverter = yConverter;
		}

		@Override
		public void attachToStage(final Stage stage) {
			window.pack();
			window.setPosition(xConverter.convertX(x, stage, window), yConverter.convertY(y, stage, window));
		}
	}
}
