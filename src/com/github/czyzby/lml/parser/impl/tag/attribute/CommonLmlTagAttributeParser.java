package com.github.czyzby.lml.parser.impl.tag.attribute;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.czyzby.kiwi.util.common.Nullables;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.gdx.widget.reflected.Tooltip;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.dto.ActorConsumer;
import com.github.czyzby.lml.parser.impl.dto.LmlTagData;
import com.github.czyzby.lml.parser.impl.util.LmlAttributes;
import com.github.czyzby.lml.util.LmlSyntax;

/** Contains attributes common for all widgets.
 *
 * @author MJ */
public enum CommonLmlTagAttributeParser implements LmlTagAttributeParser, LmlSyntax {
	ID("id") {
		@Override
		public void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			actor.setName(LmlAttributes.parseString(actor, parser, attributeValue));
			parser.mapActorById(actor);
		}
	},
	USER_OBJECT("userObject") {
		@Override
		protected void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			if (Strings.startsWith(attributeValue, ACTION_OPERATOR)) {
				actor.setUserObject(parser.findAction(attributeValue.substring(1), actor).consume(actor));
			} else {
				actor.setUserObject(attributeValue);
			}
		}
	},
	ON_CLICK("onClick", "click") {
		@Override
		public void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			final ActorConsumer<Object, Object> action =
					LmlAttributes.parseAction(actor, parser, attributeValue);
			actor.addListener(new ClickListener() {
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					action.consume(actor);
				}
			});
		}
	},
	ON_CHANGE("onChange", "change") {
		@Override
		public void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			final ActorConsumer<Object, Object> action =
					LmlAttributes.parseAction(actor, parser, attributeValue);
			actor.addListener(new ChangeListener() {
				@Override
				public void changed(final ChangeEvent event, final Actor actor) {
					action.consume(actor);
				}
			});
		}
	},
	ON_CREATE("onCreate", "create") {
		@Override
		public void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			LmlAttributes.parseAction(actor, parser, attributeValue).consume(actor);
		}
	},
	VISIBLE("visible") {
		@Override
		public void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			actor.setVisible(LmlAttributes.parseBoolean(actor, parser, attributeValue));
		}
	},
	TOUCHABLE("touchable") {
		@Override
		public void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			actor.setTouchable(Touchable.valueOf(LmlAttributes.parseString(actor, parser, attributeValue)));
		}
	},
	SCALE("scale") {
		@Override
		protected void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			actor.setScale(LmlAttributes.parseFloat(actor, parser, attributeValue));
		}
	},
	SCALE_X("scaleX") {
		@Override
		protected void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			actor.setScaleX(LmlAttributes.parseFloat(actor, parser, attributeValue));
		}
	},
	SCALE_Y("scaleY") {
		@Override
		protected void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			actor.setScaleY(LmlAttributes.parseFloat(actor, parser, attributeValue));
		}
	},
	TOOLTIP("tooltip") {
		public static final String TOOLTIP_ID = "TOOLTIPID";
		public static final String TOOLTIP_STYLE = "TOOLTIPSTYLE";

		@Override
		public void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			// TODO Convert to "native" LibGDX tooltips.
			final Tooltip tooltip =
					new Tooltip(getTooltipContent(parser, actor, attributeValue), parser.getSkin(),
							getTooltipStyle(actor, parser, lmlTagData));
			setTooltipId(lmlTagData, parser, tooltip);
			tooltip.attachTo(actor);
		}

		private Actor getTooltipContent(final LmlParser parser, final Actor actor, final String attributeValue) {
			if (attributeValue.charAt(0) == ACTION_OPERATOR) {
				final Object actionResult =
						parser.findAction(attributeValue.substring(1), actor).consume(actor);
				if (actionResult instanceof Actor) {
					return (Actor) actionResult;
				} else {
					return toLabel(actionResult, parser);
				}
			} else {
				return toLabel(parser.parseStringData(attributeValue, actor), parser);
			}
		}

		private Actor toLabel(final Object labelContent, final LmlParser parser) {
			return new Label(Nullables.toString(labelContent, NULL_ARGUMENT), parser.getSkin());
		}

		private String getTooltipStyle(final Actor actor, final LmlParser parser, final LmlTagData lmlTagData) {
			return lmlTagData.containsAttribute(TOOLTIP_STYLE) ? LmlAttributes.parseString(actor, parser,
					lmlTagData.getAttribute(TOOLTIP_STYLE)) : DEFAULT_VALUE_NAME;
		}

		private void setTooltipId(final LmlTagData lmlTagData, final LmlParser parser, final Tooltip tooltip) {
			if (lmlTagData.containsAttribute(TOOLTIP_ID)) {
				tooltip.setName(LmlAttributes.parseString(tooltip, parser,
						lmlTagData.getAttribute(TOOLTIP_ID)));
				parser.mapActorById(tooltip);
			}
		}
	},
	DEBUG("debug") {
		@Override
		public void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			actor.setDebug(LmlAttributes.parseBoolean(actor, parser, attributeValue));
		}
	},
	COLOR("color") {
		@Override
		protected void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			String colorName;
			if (attributeValue.charAt(0) == ACTION_OPERATOR) {
				final Object actionResult =
						parser.findAction(attributeValue.substring(1), actor).consume(actor);
				if (actionResult instanceof Color) {
					actor.setColor((Color) actionResult);
					return;
				} else {
					colorName = Strings.toString(actionResult);
				}
			} else {
				colorName = LmlAttributes.parseString(actor, parser, attributeValue);
			}
			actor.setColor(parser.getSkin().getColor(colorName));
		}
	},
	RED("r", "red") {
		@Override
		protected void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			final Color currentColor = actor.getColor();
			// getColor() returns actor's Color instance, but just to be safe - setting with Actor's API:
			actor.setColor(LmlAttributes.parseFloat(actor, parser, attributeValue), currentColor.g,
					currentColor.b, currentColor.a);
		}
	},
	GREEN("g", "green") {
		@Override
		protected void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			final Color currentColor = actor.getColor();
			// getColor() returns actor's Color instance, but just to be safe - setting with Actor's API:
			actor.setColor(currentColor.r, LmlAttributes.parseFloat(actor, parser, attributeValue),
					currentColor.b, currentColor.a);
		}
	},
	BLUE("b", "blue") {
		@Override
		protected void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			final Color currentColor = actor.getColor();
			// getColor() returns actor's Color instance, but just to be safe - setting with Actor's API:
			actor.setColor(currentColor.r, currentColor.g,
					LmlAttributes.parseFloat(actor, parser, attributeValue), currentColor.a);
		}
	},
	ALPHA("a", "alpha") {
		@Override
		protected void apply(final Actor actor, final LmlParser parser, final String attributeValue,
				final LmlTagData lmlTagData) {
			final Color currentColor = actor.getColor();
			// getColor() returns actor's Color instance, but just to be safe - setting with Actor's API:
			actor.setColor(currentColor.r, currentColor.g, currentColor.b,
					LmlAttributes.parseFloat(actor, parser, attributeValue));
		}
	};

	@Override
	public void apply(final Object actor, final LmlParser parser, final String attributeValue,
			final LmlTagData lmlTagData) {
		apply((Actor) actor, parser, attributeValue, lmlTagData);
	}

	protected abstract void apply(Actor actor, LmlParser parser, String attributeValue, LmlTagData lmlTagData);

	private final String[] aliases;

	private CommonLmlTagAttributeParser(final String... aliases) {
		this.aliases = aliases;
	}

	@Override
	public String[] getAttributeNames() {
		return aliases;
	}
}
