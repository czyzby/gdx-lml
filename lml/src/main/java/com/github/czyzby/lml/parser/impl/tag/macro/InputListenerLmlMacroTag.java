package com.github.czyzby.lml.parser.impl.tag.macro;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.IntSet;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlTag;

/** Attaches {@link InputListener} to its actor parent. When the selected keys are typed, content between macro tags
 * will be parsed using {@link LmlParser#parseTemplate(String)} and the returned actors will be added to the stage. This
 * is very useful for adding "delayed" actors creation that should occur only if certain event is detected. For example,
 * this shows a dialog when space is typed:
 *
 * <blockquote>
 *
 * <pre>
 * &lt;textField&gt;
 *   &lt;:onInput keys="Space"&gt;
 *     &lt;dialog&gt;Space typed!&lt;/dialog&gt;
 *   &lt;/:onInput&gt;
 * &lt;/button&gt;
 * </pre>
 *
 * </blockquote>However, you might want to cache the actors to prevent from parsing it each time the event occurs. Also,
 * you might need to show the actors only if certain condition is met. Both of these functionalities are supported:
 *
 * <blockquote>
 *
 * <pre>
 * &lt;textField&gt;
 *   &lt;:onInput keys="Space;Tab;Enter" if="$myMethod == 13" cache="true" button="1" &gt;
 *     &lt;dialog&gt;Whitespace typed!&lt;/dialog&gt;
 *   &lt;/:onInput&gt;
 * &lt;/button&gt;
 * </pre>
 *
 * </blockquote>In the example above, the actors will be displayed only if the result of myMethod is 13. Also, actors
 * will be cached after first parsing ("cache" attribute). The listener will be triggered only by the mouse button with
 * ID of 1 ("button" attribute). Keys attribute should be an array of string values that represent keys as returned by
 * {@link Keys#toString(int)} or the exact int values of key codes. If you set "combined" attribute to true, actors will
 * be shown only when all keys are pressed at the same time.
 *
 * @author MJ */
public class InputListenerLmlMacroTag extends AbstractListenerLmlMacroTag {
    /** An array of keys that trigger the event. Names have to match exact values from {@link Keys}. This attribute is
     * not optional. */
    public static final String KEYS_ATTRIBUTE = "keys";
    /** If this attribute is set to true, the event will be invoked only all selected keys are pressed at the same time.
     * Optional. */
    public static final String COMBINED_ATTRIBUTE = "combined";
    private final IntSet keys = new IntSet();
    private boolean combined;
    private final InputListener listener = new InputListener() {
        private final IntSet pressed = new IntSet();

        @Override
        public boolean keyDown(final InputEvent event, final int keycode) {
            if (keys.contains(keycode)) {
                if (!combined) {
                    doOnEvent(event.getListenerActor());
                    return true;
                }
                pressed.add(keycode);
                if (keys.size == pressed.size) {
                    doOnEvent(event.getListenerActor());
                    pressed.clear();
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean keyUp(final InputEvent event, final int keycode) {
            if (keys.contains(keycode)) {
                pressed.remove(keycode);
                return true;
            }
            return false;
        }
    };

    public InputListenerLmlMacroTag(final LmlParser parser, final LmlTag parentTag, final StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected EventListener getEventListener() {
        return listener;
    }

    @Override
    public void closeTag() {
        if (!hasAttribute(KEYS_ATTRIBUTE)) {
            getParser().throwErrorIfStrict("Input listener cannot be attached without specified 'keys' attribute.");
            return;
        }
        extractKeys();
        combined = hasAttribute(COMBINED_ATTRIBUTE)
                && getParser().parseBoolean(getAttribute(COMBINED_ATTRIBUTE), getActor());
        super.closeTag();
    }

    /** Extract key codes from {@link #KEYS_ATTRIBUTE}. */
    protected void extractKeys() {
        final String[] keyNames = getParser().parseArray(getAttribute(KEYS_ATTRIBUTE), getActor());
        for (final String keyName : keyNames) {
            final int key = Keys.valueOf(keyName);
            if (key <= Keys.UNKNOWN) {
                if (Strings.isInt(keyName)) {
                    keys.add(Integer.parseInt(keyName));
                } else {
                    getParser().throwErrorIfStrict("Unable to determine key for name: " + keyName
                            + ". Note that key name should match the EXACT name from Keys class (see Keys#valueOf(String)) or be the desired int value of key code.");
                }
            } else {
                keys.add(key);
            }
        }
    }

    @Override
    public String[] getExpectedAttributes() {
        return new String[] { IF_ATTRIBUTE, CACHE_ATTRIBUTE, KEEP_ATTRIBUTE, IDS_ATTRIBUTE, KEYS_ATTRIBUTE,
                COMBINED_ATTRIBUTE };
    }
}
