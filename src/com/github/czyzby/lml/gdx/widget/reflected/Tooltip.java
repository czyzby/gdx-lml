package com.github.czyzby.lml.gdx.widget.reflected;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.czyzby.kiwi.util.gdx.scene2d.Alignment;
import com.github.czyzby.kiwi.util.gdx.scene2d.range.FloatRange;

/** A simple component representing a tooltip. Can contain any actor, including complex component tables. Has
 * to be manually called to show and hide, although the logic is already done when using default Listener.
 *
 * @author MJ */
public class Tooltip extends Widget implements Disableable {
	/** Makes sure that the cursor doesn't cover the tooltip. Can be set globally for all tooltips. */
	public static float X_OFFSET = 10f;
	private static final float INITIAL_OFFSET = 0f;

	public static float DEFAULT_TOOLTIP_FADING_TIME = 0.2f;
	public static float DEFAULT_TOOLTIP_MOVING_TIME = 0.15f;
	public static float DEFAULT_TOOLTIP_SHOWING_DELAY = 1.25f;

	private final Table content;
	private final FloatRange xOffset = new FloatRange(X_OFFSET, DEFAULT_TOOLTIP_MOVING_TIME),
			yOffset = new FloatRange(INITIAL_OFFSET, DEFAULT_TOOLTIP_MOVING_TIME);
	/** Positions without offsets. */
	private float unmodifiedX, unmodifiedY, fadingTime = DEFAULT_TOOLTIP_FADING_TIME;
	private boolean disabled;
	private final Vector2 tempVector2 = new Vector2();

	public Tooltip(final Label content, final Skin skin) {
		this(toContent(content, skin), skin.get(TooltipStyle.class));
	}

	public Tooltip(final Label content, final Skin skin, final String styleName) {
		this(toContent(content, skin), skin.get(styleName, TooltipStyle.class));
	}

	public Tooltip(final Actor content, final Skin skin) {
		this(toContent(content, skin), skin.get(TooltipStyle.class));
	}

	public Tooltip(final Actor content, final Skin skin, final String styleName) {
		this(toContent(content, skin), skin.get(styleName, TooltipStyle.class));
	}

	private static Table toContent(final Actor actor, final Skin skin) {
		final Table table = new Table(skin);
		table.add(actor);
		return table;
	}

	public Tooltip(final Table content, final Skin skin) {
		this(content, skin.get(TooltipStyle.class));
	}

	public Tooltip(final Table content, final Skin skin, final String styleName) {
		this(content, skin.get(styleName, TooltipStyle.class));
	}

	private Tooltip(final Table content, final TooltipStyle style) {
		this.content = content;
		if (style != null) {
			this.content.setBackground(style.background);
		}
		this.content.align(Alignment.CENTER.getAlignment());
		this.content.setTouchable(Touchable.disabled);
		setTouchable(Touchable.disabled);
		this.content.pack();
		this.content.setWidth(this.content.getPrefWidth());
		this.content.setHeight(this.content.getPrefHeight());
	}

	/** @param parent will extract stage from this actor. */
	public void show(final Actor parent) {
		show(parent, fadingTime);
	}

	/** @param parent will extract stage from this actor. */
	public void show(final Actor parent, final float duration) {
		if (!disabled) {
			clearActions();
			parent.getStage().addActor(this);
			content.addAction(Actions.sequence(Actions.alpha(0f),
					Actions.fadeIn(duration, Interpolation.fade)));
			setInitialOffsets();
			setPosition();
		}
	}

	private void setInitialOffsets() {
		xOffset.setCurrentValue(X_OFFSET);
		yOffset.setCurrentValue(-getPrefHeight());
	}

	public boolean isShown() {
		return getStage() != null;
	}

	@Override
	public void setDisabled(final boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public Table getContent() {
		return content;
	}

	@Override
	public void setPosition(final float x, final float y) {
		unmodifiedX = x;
		unmodifiedY = y;
		getStage().stageToScreenCoordinates(
				tempVector2.set(x + getPrefWidth() + X_OFFSET, y - getPrefHeight()));
		if (tempVector2.x > Gdx.graphics.getWidth()) {
			xOffset.setTargetValue(-getPrefWidth());
		} else {
			xOffset.setTargetValue(X_OFFSET);
		}
		if (tempVector2.y > Gdx.graphics.getHeight()) {
			yOffset.setTargetValue(0f);
		} else {
			yOffset.setTargetValue(-getPrefHeight());
		}
		refreshPosition();
	}

	private void refreshPosition() {
		final float x = unmodifiedX + xOffset.getCurrentValue();
		final float y = unmodifiedY + yOffset.getCurrentValue();
		super.setPosition(x, y);
		content.setPosition(x, y);
	}

	private void setPosition() {
		getStage().screenToStageCoordinates(tempVector2.set(Gdx.input.getX(), Gdx.input.getY()));
		setPosition(tempVector2.x, tempVector2.y);
	}

	/** @param fadingTime time before the tooltip fully shows or hides (in seconds). */
	public void setFadingTime(final float fadingTime) {
		this.fadingTime = fadingTime;
	}

	/** @return time before the tooltip fully shows or hides (in seconds). */
	public float getFadingTime() {
		return fadingTime;
	}

	/** @param movingTime time of position transition when tooltip reaches edge of the screen (in seconds). */
	public void setMovingTime(final float movingTime) {
		xOffset.setTransitionLength(movingTime);
		yOffset.setTransitionLength(movingTime);
	}

	/** Has to be called manually. */
	public void hide() {
		hide(fadingTime);
	}

	/** Has to be called manually. */
	public void hide(final float duration) {
		clearActions();
		content.addAction(Actions.sequence(Actions.fadeOut(duration, Interpolation.fade),
				Actions.removeActor(), Actions.run(new Runnable() {
					@Override
					public void run() {
						setStage(null);
					}
				})));
	}

	@Override
	public float getMaxHeight() {
		return content.getMaxHeight();
	}

	@Override
	public float getMaxWidth() {
		return content.getMaxWidth();
	}

	@Override
	public float getMinHeight() {
		return content.getMinHeight();
	}

	@Override
	public float getMinWidth() {
		return content.getMinWidth();
	}

	@Override
	public float getPrefHeight() {
		return content.getPrefHeight();
	}

	@Override
	public float getPrefWidth() {
		return content.getPrefWidth();
	}

	@Override
	public void setWidth(final float width) {
		super.setWidth(width);
		content.setWidth(width);
	}

	@Override
	public void setHeight(final float height) {
		super.setHeight(height);
		content.setHeight(height);
	}

	@Override
	public void act(final float delta) {
		super.act(delta);
		if (xOffset.isTransitionInProgress()) {
			xOffset.update(delta);
			refreshPosition();
		}
		if (yOffset.isTransitionInProgress()) {
			yOffset.update(delta);
			refreshPosition();
		}
		content.act(delta);
	}

	@Override
	public void draw(final Batch batch, final float parentAlpha) {
		content.draw(batch, getColor().a * parentAlpha);
	}

	public void attachTo(final Actor actor) {
		attachTo(actor, DEFAULT_TOOLTIP_SHOWING_DELAY);
	}

	public void attachTo(final Actor actor, final float showingDelay) {
		if (actor != null) {
			actor.addListener(prepareTooltipListener(actor, showingDelay));
		}
	}

	private ClickListener prepareTooltipListener(final Actor toActor, final float showingDelay) {
		return new ClickListener() {
			private final Tooltip tooltip = Tooltip.this;

			@Override
			public void enter(final InputEvent event, final float x, final float y, final int pointer,
					final Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				toActor.addAction(Actions.sequence(Actions.delay(showingDelay,
						Actions.run(prepareTooltipShowingAction()))));
			}

			@Override
			public boolean mouseMoved(final InputEvent event, final float x, final float y) {
				if (tooltip.isShown()) {
					tooltip.getStage().screenToStageCoordinates(
							tooltip.tempVector2.set(Gdx.input.getX(), Gdx.input.getY()));
					tooltip.setPosition(tooltip.tempVector2.x, tooltip.tempVector2.y);
				}
				return false;
			}

			@Override
			public void exit(final InputEvent event, final float x, final float y, final int pointer,
					final Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				if (tooltip.isShown()) {
					tooltip.hide();
				}
			}

			private Runnable prepareTooltipShowingAction() {
				return new Runnable() {
					@Override
					public void run() {
						showTooltip();
					}
				};
			}

			private void showTooltip() {
				if (!tooltip.isShown() && isOver()) {
					tooltip.show(toActor);
				}
			}
		};
	}

	/** Determines tooltip's look.
	 *
	 * @author MJ */
	public static class TooltipStyle {
		public Drawable background;
	}
}
