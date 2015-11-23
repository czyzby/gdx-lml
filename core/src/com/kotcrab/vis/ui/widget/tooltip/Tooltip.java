package com.kotcrab.vis.ui.widget.tooltip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/** An input listener which displays a tooltip after a cursor is hovered over an actor for sufficient time. Contrary to
 * default {@link com.badlogic.gdx.scenes.scene2d.ui.Tooltip}, this tooltip is responsive: it constantly updates its
 * position as the cursor moves, and is never displayed outside of the stage. It also has less overhead, as it does not
 * include an additional container widget - it displays {@link Actor} set as its content directly on the {@link Stage}.
 *
 * <p>
 * All common settings can be managed globally using modifiable static variables.
 *
 * @author MJ
 *
 * @param <Content> type of actor displayed as the tooltip. */
public class Tooltip<Content extends Actor> extends InputListener implements Disableable {
    private static final Vector2 ORIGIN = new Vector2();
    private static final ObjectSet<Tooltip<?>> DISPLAYED_TOOLTIPS = new ObjectSet<Tooltip<?>>();
    private static final ShowTask SHOW_TASK = new ShowTask();

    /** Set as initial delay for all new tooltips.
     *
     * @see #setDelay(float) */
    public static float DEFAULT_DELAY = 1f;
    /** Additional offset on X axis. This allows to display the tooltip away from the cursor.
     *
     * @see #setOffsetX(float) */
    public static float DEFAULT_OFFSET_X = 0f;
    /** Additional offset on X axis. This allows to display the tooltip away from the cursor.
     *
     * @see #setOffsetY(float) */
    public static float DEFAULT_OFFSET_Y = -20f;
    /** Default length of tooltips' animations. In seconds.
     *
     * @see #setFadingTime(float) */
    public static float DEFAULT_FADING_TIME = 0.2f;
    /** Allows to globally disable all tooltips.
     *
     * @see #setDisabled(boolean) */
    public static boolean ENABLED = true;

    private Content content;

    private boolean disabled;
    private float delay = DEFAULT_DELAY;
    private float offsetX = DEFAULT_OFFSET_X;
    private float offsetY = DEFAULT_OFFSET_Y;
    private float fadingTime = DEFAULT_FADING_TIME;
    private final Runnable removeDisplayed = new Runnable() {
        @Override
        public void run() {
            DISPLAYED_TOOLTIPS.remove(Tooltip.this);
        }
    };

    /** Creates an empty tooltip. Content actor can be set later with {@link #setContent(Actor)}, but tooltip with no
     * content should not be attached and cannot be shown. */
    public Tooltip() {
    }

    /** @param content {@link Actor} instance representing the displayed tooltip. Cannot be null. */
    public Tooltip(final Content content) {
        setContent(content);
    }

    @Override
    public boolean isDisabled() {
        return disabled || !ENABLED;
    }

    @Override
    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

    /** @return {@link Actor} instance representing the displayed tooltip. */
    public Content getContent() {
        return content;
    }

    /** @param content {@link Actor} instance representing the displayed tooltip. Cannot be null. */
    public void setContent(final Content content) {
        if (content == null) {
            throw new IllegalArgumentException("Tooltip content cannot be null.");
        }
        this.content = content;
        content.setTouchable(Touchable.disabled);
        if (content instanceof Layout) {
            ((Layout) content).pack();
        }
        if (content instanceof Group) {
            ((Table) content).setTransform(true);
        }
    }

    /** @return length of tooltips' animations. */
    public float getFadingTime() {
        return fadingTime;
    }

    /** @param fadingTime length of tooltips' animations. In seconds. Defaults to {@link #DEFAULT_FADING_TIME}. */
    public void setFadingTime(final float fadingTime) {
        this.fadingTime = fadingTime;
    }

    /** @return time (in seconds) before hovered actor displays this tooltip. */
    public float getDelay() {
        return delay;
    }

    /** @param delay time (in seconds) before hovered actor displays this tooltip. */
    public void setDelay(final float delay) {
        this.delay = delay;
    }

    /** @return offset added to tooltip X position. */
    public float getOffsetX() {
        return offsetX;
    }

    /** @param offsetX added to tooltip X position. */
    public void setOffsetX(final float offsetX) {
        this.offsetX = offsetX;
    }

    /** @return offset added to tooltip Y position. */
    public float getOffsetY() {
        return offsetY;
    }

    /** @param offsetY added to tooltip Y position. */
    public void setOffsetY(final float offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public void enter(final InputEvent event, final float x, final float y, final int pointer, final Actor fromActor) {
        if (pointer != -1 || Gdx.input.isTouched() || event.getListenerActor() == SHOW_TASK.forActor) {
            return;
        }
        updateContentPosition(event);
        updateOrigin(event);
        if (DISPLAYED_TOOLTIPS.size == 0 && delay > 0f) {
            // No tooltips shown - scheduling show after initial delay.
            SHOW_TASK.cancel();
            SHOW_TASK.tooltip = this;
            SHOW_TASK.forActor = event.getListenerActor();
            Timer.schedule(SHOW_TASK, delay);
        } else {
            hideDisplayedTooltips();
            show(event.getListenerActor().getStage());
        }
    }

    @Override
    public boolean mouseMoved(final InputEvent event, final float x, final float y) {
        if (isShown() || SHOW_TASK.tooltip == this) {
            updateContentPosition(event);
        }
        return false;
    }

    @Override
    public void exit(final InputEvent event, final float x, final float y, final int pointer, final Actor toActor) {
        updateOrigin(event);
        SHOW_TASK.cancel();
        hide();
    }

    /** @param event contains event data. Will be used to update the content. */
    protected void updateContentPosition(final InputEvent event) {
        content.setPosition(event.getStageX() + offsetX, event.getStageY() + offsetY - content.getHeight());
        normalizePosition();
    }

    /** Updates content's origin point. Needed for showing and hiding actions.
     *
     * @param event contains event data. */
    protected void updateOrigin(final InputEvent event) {
        final Actor actor = event.getListenerActor();
        actor.localToStageCoordinates(ORIGIN.set(actor.getWidth() / 2, actor.getHeight() / 2));
        ORIGIN.sub(content.getX(), content.getY());
        content.setOrigin(ORIGIN.x, ORIGIN.y);
    }

    /** Keeps content position within stage bounds. */
    protected void normalizePosition() {
        if (content.getX() < 0f) {
            content.setX(0f);
        } else if (isShown() && content.getX() + content.getWidth() > content.getStage().getWidth()) {
            content.setX(content.getStage().getWidth() - content.getWidth());
        }
        if (content.getY() < 0f) {
            content.setY(0f);
        } else if (isShown() && content.getY() + content.getHeight() > content.getStage().getHeight()) {
            content.setY(content.getStage().getHeight() - content.getHeight());
        }
    }

    /** Displays tooltip content and sets showing action, as long as the tooltip is not {@link #isDisabled()}.
     *
     * @param stage will contain tooltip's content actor. */
    public void show(final Stage stage) {
        if (!isDisabled()) {
            DISPLAYED_TOOLTIPS.add(this);
            content.clearActions();
            doBeforeShow();
            stage.addActor(content);
            content.toFront();
            content.addAction(getShowingAction());
        }
    }

    /** Executed before the tooltip content is shown. */
    private void doBeforeShow() {
        content.setScale(0.05f);
        content.getColor().a = 0.1f;
    }

    /** @return action added to the tooltip's content before it is shown. */
    protected Action getShowingAction() {
        return Actions.parallel(Actions.fadeIn(fadingTime, Interpolation.fade),
                Actions.scaleTo(1f, 1f, fadingTime, Interpolation.fade)); // LibGDX tooltip action.
    }

    /** Adds hiding action and eventually removes the tooltip from the stage, if it is currently shown in the first
     * place. */
    public void hide() {
        if (isShown()) {
            content.clearActions();
            content.addAction(getHidingAction());
        }
    }

    /** @return action added to the tooltip's content before it is removed. Should end with
     *         {@link Actions#removeActor()}. */
    protected Action getHidingAction() {
        return Actions.sequence(
                Actions.parallel(Actions.scaleTo(0.05f, 0.05f, fadingTime, Interpolation.fade),
                        Actions.alpha(0.1f, fadingTime, Interpolation.fade)),
                Actions.run(removeDisplayed), Actions.removeActor()); // LibGDX tooltip action.
    }

    /** @return true if tooltip's content is currently displayed. */
    public boolean isShown() {
        return content.getStage() != null;
    }

    /** Hides all tooltips stored in {@link #DISPLAYED_TOOLTIPS}. */
    protected static void hideDisplayedTooltips() {
        if (DISPLAYED_TOOLTIPS.size == 0) {
            return;
        }
        for (final Tooltip<?> tooltip : DISPLAYED_TOOLTIPS) {
            tooltip.hide();
        }
        DISPLAYED_TOOLTIPS.clear();
    }

    /** Shows a {@link Tooltip}'s content.
     *
     * @author MJ */
    private static class ShowTask extends Task {
        private Tooltip<?> tooltip;
        private Actor forActor;

        @Override
        public void cancel() {
            super.cancel();
            if (tooltip != null) {
                tooltip.hide();
            }
            clearData();
        }

        private void clearData() {
            forActor = null;
            tooltip = null;
        }

        @Override
        public void run() {
            if (tooltip != null && forActor != null) {
                tooltip.show(forActor.getStage());
                tooltip.normalizePosition();
            }
            clearData();
        }
    }
}
