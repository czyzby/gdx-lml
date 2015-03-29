package com.konfigurats.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.dto.StageAttacher;
import com.konfigurats.lml.parser.impl.tag.parent.WindowLmlParent;

public class WindowLmlTagDataParser<WindowWidget extends Window> extends TableLmlTagDataParser<WindowWidget> {
	private static final char POSITION_PERCENT = '%';
	public static final String TITLE_ATTRIBUTE = "TITLE";
	public static final String TITLE_ALIGNMENT = "TITLE_ALIGNMENT";
	public static final String KEEP_WITHIN_STAGE_ATTRIBUTE = "KEEPWITHINSTAGE";
	public static final String MODAL_ATTRIBUTE = "MODAL";
	public static final String MOVABLE_ATTRIBUTE = "MOVABLE";
	public static final String RESIZABLE_ATTRIBUTE = "RESIZABLE";
	public static final String RESIZE_BORDER_ATTRIBUTE = "RESIZEBORDER";
	public static final String POSITION_X_ATTRIBUTE = "POSITIONX";
	public static final String POSITION_Y_ATTRIBUTE = "POSITIONY";

	@Override
	protected WindowWidget parseChildWithValidTag(final LmlTagData lmlTagData, final LmlParser parser) {
		final WindowWidget window = super.parseChildWithValidTag(lmlTagData, parser);
		setTitle(lmlTagData, parser, window);
		setMoveable(lmlTagData, parser, window);
		setResizable(lmlTagData, parser, window);
		addStageAttacher(lmlTagData, parser, window);
		return window;
	}

	private void setTitle(final LmlTagData lmlTagData, final LmlParser parser, final WindowWidget window) {
		if (lmlTagData.containsAttribute(TITLE_ATTRIBUTE)) {
			window.setTitle(parseString(lmlTagData, TITLE_ATTRIBUTE, parser, window));
		}
		if (lmlTagData.containsAttribute(TITLE_ALIGNMENT)) {
			window.setTitleAlignment(parseAlignment(lmlTagData, TITLE_ALIGNMENT, parser, window));
		}
	}

	private void setMoveable(final LmlTagData lmlTagData, final LmlParser parser, final WindowWidget window) {
		if (lmlTagData.containsAttribute(KEEP_WITHIN_STAGE_ATTRIBUTE)) {
			window.setKeepWithinStage(parseBoolean(lmlTagData, KEEP_WITHIN_STAGE_ATTRIBUTE, parser, window));
		}
		if (lmlTagData.containsAttribute(MOVABLE_ATTRIBUTE)) {
			window.setMovable(parseBoolean(lmlTagData, MOVABLE_ATTRIBUTE, parser, window));
		}
		if (lmlTagData.containsAttribute(MODAL_ATTRIBUTE)) {
			window.setModal(parseBoolean(lmlTagData, MODAL_ATTRIBUTE, parser, window));
		}
	}

	private void setResizable(final LmlTagData lmlTagData, final LmlParser parser, final WindowWidget window) {
		if (lmlTagData.containsAttribute(RESIZABLE_ATTRIBUTE)) {
			window.setResizable(parseBoolean(lmlTagData, RESIZABLE_ATTRIBUTE, parser, window));
		}
		if (lmlTagData.containsAttribute(RESIZE_BORDER_ATTRIBUTE)) {
			window.setResizeBorder(parseInt(lmlTagData, RESIZE_BORDER_ATTRIBUTE, parser, window));
		}
	}

	private void addStageAttacher(final LmlTagData lmlTagData, final LmlParser parser,
			final WindowWidget window) {
		final float x;
		final float y;
		final PositionConverter xConverter;
		final PositionConverter yConverter;
		if (lmlTagData.containsAttribute(POSITION_X_ATTRIBUTE)) {
			final String xAttribute = parseString(lmlTagData, POSITION_X_ATTRIBUTE, parser, window);
			if (xAttribute.charAt(xAttribute.length() - 1) == '%') {
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
			final String yAttribute = parseString(lmlTagData, POSITION_Y_ATTRIBUTE, parser, window);
			if (yAttribute.charAt(yAttribute.length() - 1) == POSITION_PERCENT) {
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

	protected WindowStageAttacher getWindowStageAttacher(final WindowWidget window, final LmlParser parser,
			final LmlTagData lmlTagData, final float x, final float y, final PositionConverter xConverter,
			final PositionConverter yConverter) {
		return new WindowStageAttacher(window, x, y, xConverter, yConverter);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected WindowWidget prepareNewTable(final LmlTagData lmlTagData, final LmlParser parser) {
		final Window window = new Window(EMPTY_STRING, parser.getSkin(), getStyleName(lmlTagData, parser));
		return (WindowWidget) window;
	}

	@Override
	protected LmlParent<WindowWidget> parseParentWithValidTag(final LmlTagData lmlTagData,
			final LmlParser parser, final LmlParent<?> parent) {
		return new WindowLmlParent<WindowWidget>(lmlTagData, parseChildWithValidTag(lmlTagData, parser),
				parent, parser);
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
