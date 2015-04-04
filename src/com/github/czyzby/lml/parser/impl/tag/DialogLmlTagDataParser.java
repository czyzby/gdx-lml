package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.gdx.widget.reflected.ReflectedLmlDialog;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.tag.parent.DialogLmlParent;

public class DialogLmlTagDataParser extends WindowLmlTagDataParser {
	public static final String ON_SHOW_ATTRIBUTE = "ONSHOW";
	private static final ObjectMap<String, LmlTagAttributeParser> ATTRIBUTE_PARSERS;

	private final ObjectMap<String, LmlTagAttributeParser> attributeParsers =
			new ObjectMap<String, LmlTagAttributeParser>(ATTRIBUTE_PARSERS);

	static {
		ATTRIBUTE_PARSERS = GdxMaps.newObjectMap();
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
		if (GdxMaps.isNotEmpty(attributeParsers)) {
			for (final Entry<String, String> attribute : lmlTagData.getAttributes()) {
				if (attributeParsers.containsKey(attribute.key)) {
					attributeParsers.get(attribute.key).apply(actor, parser, attribute.value, lmlTagData);
				}
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
	protected Dialog getNewInstanceOfWindow(final LmlTagData lmlTagData, final LmlParser parser) {
		return new ReflectedLmlDialog(Strings.EMPTY_STRING, parser.getSkin(),
				getStyleName(lmlTagData, parser));
	}

	@Override
	protected WindowStageAttacher getWindowStageAttacher(final Window dialog, final LmlParser parser,
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
		return new DialogStageAttacher((Dialog) dialog, x, y, xConverter, yConverter, onShow);
	}

	@Override
	protected LmlParent<Table> parseParentWithValidTag(final LmlTagData lmlTagData, final LmlParser parser,
			final LmlParent<?> parent) {
		return new DialogLmlParent(lmlTagData, (Dialog) parseChild(lmlTagData, parser), parent, parser);
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
