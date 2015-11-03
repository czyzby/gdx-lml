package com.github.czyzby.lml.vis.util;

import com.github.czyzby.kiwi.util.common.Exceptions;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.kotcrab.vis.ui.widget.color.ColorPicker;

/** Manages a single {@link ColorPicker} instance. Since color picker is a heavy widget that should be reused, this
 * class aims to force use of a single picker in LML. Unless you want to use color picker with the default settings, you
 * might want to call {@link #setInstance(ColorPicker)} or {@link #initiateInstance(String, String)} before parsing LML
 * templates to fully customize the widget. If you have ever used the color picker with LML attributes (or manually,
 * with this container's methods), remember to call {@link #dispose()} when you no longer need the widget.
 *
 * @author MJ */
public class ColorPickerContainer {
    private static ColorPicker INSTANCE;

    private ColorPickerContainer() {
    }

    /** @return direct reference to the managed {@link ColorPicker}. Might be null or disposed.
     * @see #requestInstance() */
    public static ColorPicker getInstance() {
        return INSTANCE;
    }

    /** @return an instance of {@link ColorPicker} managed by this class. Never null, although might be already
     *         disposed.
     * @see #getInstance() */
    public static ColorPicker requestInstance() {
        if (INSTANCE == null) {
            initiateInstance();
        }
        return INSTANCE;
    }

    /** Disposes of current instance (if present) and creates a new default instance. */
    public static void reloadInstance() {
        dispose();
        initiateInstance();
    }

    /** Disposes of current instance (if present) and creates a new instance.
     *
     * @param title will become window's title.
     * @param styleName determines the style of {@link ColorPicker}. */
    public static void reloadInstance(final String title, final String styleName) {
        dispose();
        initiateInstance(title, styleName);
    }

    /** Creates an instance of {@link ColorPicker} which will be accessible through {@link #getInstance()} and
     * {@link #requestInstance()} methods. */
    public static void initiateInstance() {
        INSTANCE = new ColorPicker(Strings.EMPTY_STRING);
    }

    /** Creates an instance of {@link ColorPicker} which will be accessible through {@link #getInstance()} and
     * {@link #requestInstance()} methods.
     *
     * @param title will become window's title.
     * @param styleName determines the style of {@link ColorPicker}. */
    public static void initiateInstance(final String title, final String styleName) {
        INSTANCE = new ColorPicker(styleName, title, null);
    }

    /** Changes currently managed {@link ColorPicker} instance. Note that previous instance - if it exists - has to be
     * manually disposed of, so if you want to replace another color picker with a new one, make sure to properly
     * destroy it before calling this method.
     *
     * @param colorPicker will become managed {@link ColorPicker} instance.
     * @see #initiateInstance()
     * @see #initiateInstance(String, String)
     * @see #reloadInstance()
     * @see #reloadInstance(String, String) */
    public static void setInstance(final ColorPicker colorPicker) {
        INSTANCE = colorPicker;
    }

    /** Disposes of managed {@link ColorPicker} instance (if present). Null-safe, never throws an exception. */
    public static void dispose() {
        try {
            Disposables.disposeOf(INSTANCE);
        } catch (final Exception exception) {
            // Although Disposables utility method is null-safe, exception might still be thrown if color picker
            // instance if already disposed of. Ignored.
            Exceptions.ignore(exception);
        }
    }
}
