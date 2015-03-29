package com.konfigurats.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.konfigurats.lml.gdx.widget.reflected.ReflectedLmlDialog;
import com.konfigurats.lml.parser.LmlParser;
import com.konfigurats.lml.parser.impl.dto.LmlParent;
import com.konfigurats.lml.parser.impl.dto.LmlTagData;
import com.konfigurats.lml.parser.impl.tag.parent.DialogLmlParent;

public class DialogLmlTagDataParser extends WindowLmlTagDataParser<Dialog> {
	public static final String ON_SHOW_ATTRIBUTE = "ONSHOW";

	@Override
	protected Dialog prepareNewTable(final LmlTagData lmlTagData, final LmlParser parser) {
		final Dialog dialog =
				new ReflectedLmlDialog(EMPTY_STRING, parser.getSkin(), getStyleName(lmlTagData, parser));
		return dialog;
	}

	@Override
	protected WindowStageAttacher getWindowStageAttacher(final Dialog dialog, final LmlParser parser,
			final LmlTagData lmlTagData, final float x, final float y, final PositionConverter xConverter,
			final PositionConverter yConverter) {
		final Action onShow;
		if (lmlTagData.containsAttribute(ON_SHOW_ATTRIBUTE)) {
			String onShowAttribute = lmlTagData.getAttribute(ON_SHOW_ATTRIBUTE);
			if (onShowAttribute.charAt(0) == ACTION_OPERATOR) {
				onShowAttribute = onShowAttribute.substring(1);
			}
			onShow = (Action) parser.findAction(onShowAttribute, dialog).consume(dialog);
		} else {
			onShow = null;
		}
		return new DialogStageAttacher(dialog, x, y, xConverter, yConverter, onShow);
	}

	@Override
	protected LmlParent<Dialog> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
			final LmlParent<?> parent) {
		return new DialogLmlParent(lmlTagData, parseChildWithValidTag(lmlTagData, parser), parent, parser);
	}

	public static class DialogStageAttacher extends WindowStageAttacher {
		private final Action onShow;

		public DialogStageAttacher(final Dialog dialog, final float x, final float y,
				final PositionConverter xConverter, final PositionConverter yConverter, final Action onShow) {
			super(dialog, x, y, xConverter, yConverter);
			this.onShow = onShow;
		}

		@Override
		public void attachToStage(final Stage stage) {
			if (onShow == null) {
				((Dialog) window).show(stage);
			} else {
				((Dialog) window).show(stage, onShow);
			}
			super.attachToStage(stage);
		}
	}
}
