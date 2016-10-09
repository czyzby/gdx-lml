package com.github.czyzby.tests.reflected.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** A label that blinks. Created to show an example of a custom actor tag.
 *
 * @author MJ */
public class BlinkingLabel extends Label {
    private float blinkingTime = 0.5f;
    private float timeSinceLastBlink;

    /** @param text initial text of the label.
     * @param skin skin used to retrieve widget's style.
     * @param styleName name of LabelStyle stored in the skin. */
    public BlinkingLabel(final CharSequence text, final Skin skin, final String styleName) {
        super(text, skin, styleName);
    }

    @Override
    public void act(final float delta) {
        // Custom acting method. Makes the label blink.
        timeSinceLastBlink += delta;
        if (timeSinceLastBlink >= blinkingTime) {
            timeSinceLastBlink = 0f;
            getColor().a = getColor().a > 0f ? 0f : 1f;
        }
        super.act(delta);
    }

    /** @param blinkingTime time after which the label blinks. */
    public void setBlinkingTime(final float blinkingTime) {
        this.blinkingTime = blinkingTime;
    }

    /** @return time after which the label blinks. */
    public float getBlinkingTime() {
        return blinkingTime;
    }
}
