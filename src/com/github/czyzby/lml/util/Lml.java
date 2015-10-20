package com.github.czyzby.lml.util;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.czyzby.lml.parser.LmlData;
import com.github.czyzby.lml.parser.LmlParser;

/** Utility class for simplified parser creation.
 *
 * @author MJ
 * @see LmlUtilities
 * @see LmlParserBuilder */
public class Lml {
    private Lml() {
    }

    /** When action is referenced in LML template, its parser looks for registered ActorConsumers with the selected key
     * (as they do not rely on reflection and are cheaper to invoke). If no
     * {@link com.github.czyzby.lml.parser.action.ActorConsumer} is found, then the parser looks for
     * {@link com.github.czyzby.lml.parser.action.ActionContainer}'s containing the referenced action. When none of
     * action container's methods match the key, normally container's field with the given name is returned, provided it
     * exists. The action - instead of invoking a method - will extract and return current field's value. However,
     * extracting fields causes problems on GWT (probably due to LibGDX reflection implementation), so this
     * functionality can be globally turned off by setting this variable to false (default state). The rule of thumb is:
     * if you use multiple action containers and plan on releasing GWT client, keep this variable as false. If you need
     * field extraction, set to true. */
    public static boolean EXTRACT_FIELDS_AS_METHODS = false;

    /** @return a new {@link LmlParserBuilder}, allowing to easily create a new instance of {@link LmlParser}. */
    public static LmlParserBuilder parser() {
        return new LmlParserBuilder();
    }

    /** @param defaultSkin will be registered as the default skin. Cannot be null.
     * @return a new {@link LmlParserBuilder}, allowing to easily create a new instance of {@link LmlParser}. */
    public static LmlParserBuilder parser(final Skin defaultSkin) {
        return new LmlParserBuilder().skin(defaultSkin);
    }

    /** @param data contains data necessary to properly parse LML templates.
     * @return a new {@link LmlParserBuilder}, allowing to easily create a new instance of {@link LmlParser}.
     * @see com.github.czyzby.lml.parser.impl.DefaultLmlData */
    public static LmlParserBuilder parser(final LmlData data) {
        return new LmlParserBuilder(data);
    }
}
