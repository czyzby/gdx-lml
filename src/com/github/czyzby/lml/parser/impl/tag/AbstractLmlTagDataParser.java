package com.github.czyzby.lml.parser.impl.tag;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.gdx.widget.reflected.Tooltip;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagDataParser;
import com.github.czyzby.lml.parser.impl.dto.ActorConsumer;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.util.common.Nullables;

/** Provides tag validation and common tags handling.
 *
 * @author MJ */
public abstract class AbstractLmlTagDataParser<Widget extends Actor> extends AbstractLmlAttributeParser
		implements LmlTagDataParser<Widget> {
	public static final String DEFAULT_STYLE_NAME = "default";

	@Override
	public Widget parseChild(final LmlTagData lmlTagData, final LmlParser parser) {
		if (!lmlTagData.isClosed()) {
			throw new LmlParsingException("Invalid parser implementation. Tag is not a child.", parser);
		}
		final Widget child = parseChildWithValidTag(lmlTagData, parser);
		handleCommonAttributes(lmlTagData, parser, child);
		return child;
	}

	private void handleCommonAttributes(final LmlTagData lmlTagData, final LmlParser parser,
			final Widget actor) {
		setVisible(lmlTagData, actor, parser);
		setTouchable(lmlTagData, actor, parser);
		appendOnClickAction(lmlTagData, parser, actor);
		appendOnChangeAction(lmlTagData, parser, actor);
		attachTooltip(lmlTagData, parser, actor);
		setDebug(lmlTagData, actor, parser);
		fireOnCreateAction(lmlTagData, parser, actor);
	}

	protected void setVisible(final LmlTagData lmlTagData, final Widget actor, final LmlParser parser) {
		if (lmlTagData.containsAttribute(VISIBLE_ATTRIBUTE)) {
			actor.setVisible(parseBoolean(lmlTagData, VISIBLE_ATTRIBUTE, parser, actor));
		}
	}

	protected void setTouchable(final LmlTagData lmlTagData, final Widget actor, final LmlParser parser) {
		if (lmlTagData.containsAttribute(TOUCHABLE_ATTRIBUTE)) {
			actor.setTouchable(Touchable.valueOf(parseString(lmlTagData, TOUCHABLE_ATTRIBUTE, parser, actor)));
		}
	}

	protected void appendOnClickAction(final LmlTagData lmlTagData, final LmlParser parser, final Widget actor) {
		if (lmlTagData.containsAttribute(ON_CLICK_ATTRIBUTE)) {
			final String actionId = lmlTagData.getAttribute(ON_CLICK_ATTRIBUTE);
			addOnClickAction(parser, actor, actionId);
		}
	}

	protected void addOnClickAction(final LmlParser parser, final Widget actor, final String actionId) {
		final ActorConsumer<Object, Object> action = getAction(parser, actor, actionId);
		actor.addListener(new ClickListener() {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				action.consume(actor);
			}
		});
	}

	private ActorConsumer<Object, Object> getAction(final LmlParser parser, final Widget actor,
			final String actionId) {
		return actionId.charAt(0) == ACTION_OPERATOR ? parser.findAction(actionId.substring(1), actor)
				: parser.findAction(actionId, actor);
	}

	protected void appendOnChangeAction(final LmlTagData lmlTagData, final LmlParser parser,
			final Widget actor) {
		if (lmlTagData.containsAttribute(ON_CHANGE_ATTRIBUTE)) {
			final String actionId = lmlTagData.getAttribute(ON_CHANGE_ATTRIBUTE);
			addOnChangeAction(parser, actor, actionId);
		}
	}

	protected void addOnChangeAction(final LmlParser parser, final Widget actor, final String actionId) {
		final ActorConsumer<Object, Object> action = getAction(parser, actor, actionId);
		actor.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				action.consume(actor);
			}
		});
	}

	private void attachTooltip(final LmlTagData lmlTagData, final LmlParser parser, final Widget actor) {
		if (lmlTagData.containsAttribute(TOOLTIP_ATTRIBUTE)) {
			final String tooltipAttribute = lmlTagData.getAttribute(TOOLTIP_ATTRIBUTE);
			final Tooltip tooltip =
					new Tooltip(getTooltipContent(parser, actor, tooltipAttribute), parser.getSkin(),
							getTooltipStyle(lmlTagData, parser, actor));
			setTooltipId(lmlTagData, parser, tooltip);
			tooltip.attachTo(actor);
		}
	}

	private Actor getTooltipContent(final LmlParser parser, final Widget actor, final String tooltipAttribute) {
		if (tooltipAttribute.charAt(0) == ACTION_OPERATOR) {
			final Object actionResult =
					parser.findAction(tooltipAttribute.substring(1), actor).consume(actor);
			if (actionResult instanceof Actor) {
				return (Actor) actionResult;
			} else {
				return toLabel(actionResult, parser);
			}
		} else {
			return toLabel(parser.parseStringData(tooltipAttribute, actor), parser);
		}
	}

	private Actor toLabel(final Object labelContent, final LmlParser parser) {
		return new Label(Nullables.toString(labelContent, NULL_ARGUMENT), parser.getSkin());
	}

	private String getTooltipStyle(final LmlTagData lmlTagData, final LmlParser parser, final Widget actor) {
		return lmlTagData.containsAttribute(TOOLTIP_STYLE_ATTRIBUTE) ? parseString(lmlTagData,
				TOOLTIP_STYLE_ATTRIBUTE, parser, actor) : DEFAULT_STYLE_NAME;
	}

	private void setTooltipId(final LmlTagData lmlTagData, final LmlParser parser, final Tooltip tooltip) {
		if (lmlTagData.containsAttribute(TOOLTIP_ID)) {
			tooltip.setName(parseString(lmlTagData, TOOLTIP_ID, parser, tooltip));
			parser.mapActorById(tooltip);
		}
	}

	protected void setDebug(final LmlTagData lmlTagData, final Widget actor, final LmlParser parser) {
		if (lmlTagData.containsAttribute(DEBUG_ATTRIBUTE)) {
			actor.setDebug(parseBoolean(lmlTagData, DEBUG_ATTRIBUTE, parser, actor));
		}
	}

	protected void fireOnCreateAction(final LmlTagData lmlTagData, final LmlParser parser, final Widget actor) {
		if (lmlTagData.containsAttribute(ON_CREATE_ATTRIBUTE)) {
			final String actionId = lmlTagData.getAttribute(ON_CREATE_ATTRIBUTE);
			getAction(parser, actor, actionId).consume(actor);
		}
	}

	/** Actual implementation of child parsing.
	 *
	 * @param lmlTagData is validated.
	 * @return actor parsed from tag data. */
	protected abstract Widget parseChildWithValidTag(LmlTagData lmlTagData, LmlParser parser);

	@Override
	public LmlParent<Widget> parseParent(final LmlTagData lmlTagData, final LmlParser parser,
			final LmlParent<?> parent) {
		if (lmlTagData.isClosed()) {
			throw new LmlParsingException("Invalid parser implementation. Tag is not a parent.", parser);
		}
		final LmlParent<Widget> newParent = parseParentWithValidTag(lmlTagData, parser, parent);
		handleCommonAttributes(lmlTagData, parser, newParent.getActor());
		return newParent;
	}

	/** Actual implementation of parent parsing.
	 *
	 * @param lmlTagData is validated.
	 * @return actor parsed from tag data with additional parental data. */
	protected abstract LmlParent<Widget> parseParentWithValidTag(LmlTagData lmlTagData, LmlParser parser,
			final LmlParent<?> parent);

	/** @return true if widget has an attribute that defines his style from skin. */
	protected boolean containsStyleAttribute(final LmlTagData lmlTagData) {
		return lmlTagData.containsAnyAttribute(STYLE_ATTRIBUTE, CLASS_ATTRIBUTE);
	}

	/** @return style name extracted from tag data. Check CLASS and STYLE attributes. Default if not set. */
	protected String getStyleName(final LmlTagData lmlTagData, final LmlParser parser) {
		String styleName = lmlTagData.getAttribute(STYLE_ATTRIBUTE);
		if (styleName == null) {
			styleName = lmlTagData.getAttribute(CLASS_ATTRIBUTE);
		}
		return styleName == null ? DEFAULT_STYLE_NAME : parser.parseStringData(styleName, null);
	}
}
