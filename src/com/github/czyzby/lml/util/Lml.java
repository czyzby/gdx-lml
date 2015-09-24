package com.github.czyzby.lml.util;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.common.Nullables;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.impl.AbstractLmlParser;
import com.github.czyzby.lml.parser.impl.Scene2DLmlParser;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;
import com.github.czyzby.lml.parser.impl.dto.ActorConsumer;

/** Allows to (hopefully) easily prepare LML document parsers.
 *
 * @author MJ */
public class Lml implements LmlSyntax {
    /** When action is referenced in LML template, it parser looks for ActorConsumers with the selected key - then it
     * looks for action containers containing the referenced action. When none of action container's methods match the
     * key, normally container's field with the given name is returned, provided it exists. However, this causes
     * problems on GWT (probably due to LibGDX reflection implementation), so this functionality can be globally turned
     * off by setting this variable to false. */
    public static boolean EXTRACT_FIELDS_AS_METHODS = true;

    /** When an action is attached to a dialog tag with "onResult" attribute, it will be fired upon clicking one of the
     * widgets in button table. If it returns true (boolean), dialog will not be hidden. If it returns false, dialog
     * will hide. */
    public static final boolean APPROVE_DIALOG_HIDING = false, CANCEL_DIALOG_HIDING = true;

    private Lml() {
    }

    /** Allows to build an LmlParser with the default implementation. Each builder instance should be used once.
     * Basically, it creates an instance of default LmlParser implementation and wraps around its methods to make them
     * chainable. Uncommon settings - like default parsers - are omitted on purpose and have to be set after the
     * building is complete.
     *
     * After building, use parse() or fill() methods to parse .lml files.
     *
     * @return a new LmlParserBuilder instance. */
    public static LmlParserBuilder parser() {
        return new LmlParserBuilder((Skin) null);
    }

    /** Allows to build an LmlParser with the default implementation. Each builder instance should be used once.
     * Basically, it creates an instance of default LmlParser implementation and wraps around its methods to make them
     * chainable. Uncommon settings - like default parsers - are omitted on purpose and have to be set after the
     * building is complete.
     *
     * After building, use parse() or fill() methods to parse .lml files.
     *
     * @return a new LmlParserBuilder instance. */
    public static LmlParserBuilder parser(final Skin skin) {
        return new LmlParserBuilder(skin);
    }

    /** Allows to build an LmlParser with the default implementation. Each builder instance should be used once.
     * Basically, it creates an instance of default LmlParser implementation and wraps around its methods to make them
     * chainable. Uncommon settings - like default parsers - are omitted on purpose and have to be set after the
     * building is complete.
     *
     * After building, use parse() or fill() methods to parse .lml files.
     *
     * @return a new LmlParserBuilder instance with settings copied from the passed builder. */
    public static LmlParserBuilder parser(final LmlParserBuilder fromBuilder) {
        return new LmlParserBuilder(fromBuilder.build());
    }

    /** Allows to build an LmlParser with the default implementation. Each builder instance should be used once.
     * Basically, it creates an instance of default LmlParser implementation and wraps around its methods to make them
     * chainable. Uncommon settings - like default parsers - are omitted on purpose and have to be set after the
     * building is complete.
     *
     * After building, use parse() or fill() methods to parse .lml files.
     *
     * @return a new LmlParserBuilder instance with settings copied from the passed parser. */
    public static LmlParserBuilder parser(final LmlParser fromParser) {
        return new LmlParserBuilder(fromParser);
    }

    /** Allows to build an LmlParser with the default implementation. Each builder instance should be used once.
     * Basically, it creates an instance of default LmlParser implementation and wraps around its methods to make them
     * chainable. Uncommon settings - like default parsers - are omitted on purpose and have to be set after the
     * building is complete.
     *
     * After building, use parse() or fill() methods to parse .lml files.
     *
     * @return a new LmlParserBuilder instance; */
    public static LmlParserBuilder parser(final Skin skin, final I18NBundle i18nBundle) {
        return new LmlParserBuilder(skin).i18nBundle(i18nBundle);
    }

    /** Allows to build an LmlParser with the default implementation. Each builder instance should be used once.
     * Basically, it creates an instance of default LmlParser implementation and wraps around its methods to make them
     * chainable. Uncommon settings - like default parsers - are omitted on purpose and have to be set after the
     * building is complete.
     *
     * After building, use parse() or fill() methods to parse .lml files.
     *
     * @return a new LmlParserBuilder instance; */
    public static LmlParserBuilder parser(final Skin skin, final I18NBundle i18nBundle, final Preferences preferences) {
        return new LmlParserBuilder(skin).i18nBundle(i18nBundle).preferences(preferences);
    }

    /** @param arguments will be converted to a LML-compatible array. Note that arrays, ranges and method invocations
     *            can be stored together and are properly parsed by marcos that expect arrays. */
    public static String toArrayArgument(final Object... arguments) {
        if (arguments.length == 0) {
            return NULL_ARGUMENT;
        } else if (arguments.length == 1) {
            return Nullables.toString(arguments[0], NULL_ARGUMENT);
        } else {
            int index = 0;
            final StringBuilder argument = new StringBuilder(Nullables.toString(arguments[index++], NULL_ARGUMENT));
            for (; index < arguments.length; index++) {
                argument.append(ARGUMENT_SEPARATOR).append(Nullables.toString(arguments[index], NULL_ARGUMENT));
            }
            return argument.toString();
        }
    }

    /** @param arguments will be converted to a LML-compatible array. Note that arrays, ranges and method invocations
     *            can be stored together and are properly parsed by marcos that expect arrays. */
    public static String toArrayArgument(final Iterable<?> arguments) {
        final StringBuilder argumentBuilder = new StringBuilder();
        boolean isFirst = true;
        for (final Object argument : arguments) {
            if (isFirst) {
                isFirst = false;
            } else {
                argumentBuilder.append(ARGUMENT_SEPARATOR);
            }
            argumentBuilder.append(Nullables.toString(argument, NULL_ARGUMENT));
        }
        return argumentBuilder.toString();
    }

    /** @param argumentBase will be converted into a range argument (from 0 to rangeEnd). Internally, when marcos expect
     *            arrays, ranges are converted into arrays using this schema: tag[0-2] = tag0,tag1,tag2. Range values
     *            cannot be negative. Ranges can be put in arrays (with other arguments, including other ranges). */
    public static String toRangeArgument(final String argumentBase, final int rangeEnd) {
        return AbstractLmlParser.toRangeArgument(argumentBase, 0, rangeEnd);
    }

    /** @param argumentBase will be converted into a range argument (from rangeStart to rangeEnd). Internally, when
     *            marcos expect arrays, ranges are converted into arrays using this schema: tag[0-2] = tag0,tag1,tag2.
     *            Range values cannot be negative. Start can be greater than end. Ranges can be put in arrays (with
     *            other arguments, including other ranges). */
    public static String toRangeArgument(final String argumentBase, final int rangeStart, final int rangeEnd) {
        return AbstractLmlParser.toRangeArgument(argumentBase, rangeStart, rangeEnd);
    }

    /** @return a method invocation constructed with the method key. Note that actions can be referenced with simple
     *         strings in tags (for example, onClick=clickOnLabel will properly look for clickOnLabel action), but
     *         macros that consume arrays or other parseable arguments need a special operator to invoke methods: &amp;.
     *         Method invocations can be stored in arrays and will be properly parsed (method return result will be
     *         converted into a string or an array of strings, if the result is iterable). */
    public static String toMethodInvocationArgument(final String methodKey) {
        return ACTION_OPERATOR + methodKey;
    }

    /** @param actionContainerKey has to contain the referenced method.
     * @param methodName no parenthesis.
     * @return a method invocation constructed with the method key. Note that actions can be referenced with simple
     *         strings in tags (for example, onClick=clickOnLabel will properly look for clickOnLabel action), but
     *         macros that consume arrays or other parseable arguments need a special operator to invoke methods: &amp;.
     *         Method invocations can be stored in arrays and will be properly parsed (method return result will be
     *         converted into a string or an array of strings, if the result is iterable). */
    public static String toMethodInvocationArgument(final String actionContainerKey, final String methodName) {
        return toMethodInvocationArgument(actionContainerKey + ACTION_SEPARATOR + methodName);
    }

    /** @param preferenceKey will be proceeded with #, making it extract text from preferences. */
    public static String toPreference(final String preferenceKey) {
        return PREFERENCE_SIGN + preferenceKey;
    }

    /** @param bundleLineKey will be proceeded with @, making it extract text from i18n bundle. */
    public static String toBundleLine(final String bundleLineKey) {
        return BUNDLE_LINE_SIGN + bundleLineKey;
    }

    /** Used to create customize LmlParsers.
     *
     * @author MJ */
    public static class LmlParserBuilder {
        private final LmlParser lmlParser;

        public LmlParserBuilder(final Skin skin) {
            lmlParser = getNewInstance(skin);
        }

        public LmlParserBuilder(final LmlParser lmlParser) {
            this.lmlParser = getNewInstance(lmlParser);
        }

        protected LmlParser getNewInstance(final Skin skin) {
            return new Scene2DLmlParser(skin);
        }

        protected LmlParser getNewInstance(final LmlParser parser) {
            return new Scene2DLmlParser((AbstractLmlParser) parser);
        }

        /** @param skin has to store referenced widget styles. */
        public LmlParserBuilder skin(final Skin skin) {
            lmlParser.setSkin(skin);
            return this;
        }

        /** @param i18nBundle has to store texts referenced with proceeding @ in .lml files. */
        public LmlParserBuilder i18nBundle(final I18NBundle i18nBundle) {
            lmlParser.setDefaultI18nBundle(i18nBundle);
            return this;
        }

        /** @param preferences can store attributes referenced with proceeding # in .lml files. */
        public LmlParserBuilder preferences(final Preferences preferences) {
            lmlParser.setDefaultPreferences(preferences);
            return this;
        }

        /** @param i18nBundle has to store texts referenced with proceeding @ + bundleName + @ in .lml files. */
        public LmlParserBuilder i18nBundle(final String bundleName, final I18NBundle i18nBundle) {
            lmlParser.setI18nBundle(bundleName, i18nBundle);
            return this;
        }

        /** @param preferences can store attributes referenced with proceeding #preferencesName# in .lml files. */
        public LmlParserBuilder preferences(final String preferencesName, final Preferences preferences) {
            lmlParser.setPreferences(preferencesName, preferences);
            return this;
        }

        /** @param argumentValue will be passed to the .lml file. Arguments can be referenced with ${argumentKey}, case
         *            sensitive. */
        public LmlParserBuilder argument(final String argumentKey, final String argumentValue) {
            lmlParser.addArgument(argumentKey, argumentValue);
            return this;
        }

        /** @param arguments will be passed to the .lml file. Arguments can be referenced with ${argumentKey}, case
         *            sensitive. */
        public LmlParserBuilder arguments(final ObjectMap<String, String> arguments) {
            lmlParser.addArguments(arguments);
            return this;
        }

        /** @param values will be separated with ; and added as an argument. */
        public LmlParserBuilder arrayArgument(final String argumentKey, final String... values) {
            lmlParser.addArgument(argumentKey, values);
            return this;
        }

        /** @param values will be separated with ; and added as an argument. */
        public LmlParserBuilder arrayArgument(final String argumentKey, final Iterable<String> values) {
            lmlParser.addArgument(argumentKey, values);
            return this;
        }

        /** @param argumentValueBase will be added as range argument (ending with [0-rangeEnd]) with the given key. */
        public LmlParserBuilder rangeArgument(final String argumentKey, final String argumentValueBase,
                final int rangeEnd) {
            lmlParser.addArgument(argumentKey, argumentValueBase, 0, rangeEnd);
            return this;
        }

        /** @param argumentValueBase will be added as range argument (ending with [rangeStart-rangeEnd]) with the given
         *            key. */
        public LmlParserBuilder rangeArgument(final String argumentKey, final String argumentValueBase,
                final int rangeStart, final int rangeEnd) {
            lmlParser.addArgument(argumentKey, argumentValueBase, rangeStart, rangeEnd);
            return this;
        }

        /** @param actionExecutor executes a single action. Tag attributes that expect method names (like onClick) will
         *            look for executors based on these keys. ActorConsumers take precedence over methods added with
         *            reflection. */
        public LmlParserBuilder action(final String actionKey, final ActorConsumer<?, ?> actionExecutor) {
            lmlParser.addAction(actionKey, actionExecutor);
            return this;
        }

        /** @param actionExecutors execute a single action. Tag attributes that expect method names (like onClick) will
         *            look for executors based on keys passed in this map. ActorConsumers take precedence over methods
         *            added with reflection. Note that this method will add new actions rather than replace the old ones
         *            (unless there are collisions in method keys). */
        public LmlParserBuilder actions(final ObjectMap<String, ActorConsumer<?, ?>> actionExecutors) {
            lmlParser.addActions(actionExecutors);
            return this;
        }

        /** @param actionContainer can contain multiple methods that will be extracted with reflection. As opposed to
         *            ActorConsumers' keys, action container keys do not reference specific actions - they point to
         *            specific containers. To access a method from action container, you can use both methodName
         *            (although you cannot be sure which container will have its method extracted) or
         *            containerKey.methodName. Note that ActionContainer implementation cannot be anonymous and have to
         *            be GWT-reflected to work on HTML backend. */
        public LmlParserBuilder actionContainer(final String actionContainerKey,
                final ActionContainer actionContainer) {
            lmlParser.addActionContainer(actionContainerKey, actionContainer);
            return this;
        }

        /** @param actionContainers each can contain multiple methods that will be extracted with reflection. As opposed
         *            to ActorConsumers' keys, action container keys do not reference specific actions - they point to
         *            specific containers. To access a method from action container, you can use both methodName
         *            (although you cannot be sure which container will have its method extracted) or
         *            containerKey.methodName. Note that ActionContainer implementation cannot be anonymous and have to
         *            be GWT-reflected to work on HTML backend. */
        public LmlParserBuilder actionContainers(final ObjectMap<String, ActionContainer> actionContainers) {
            lmlParser.addActionContainers(actionContainers);
            return this;
        }

        /** @param strict if true, parsing will throw much more exceptions, even if the parsing itself could be
         *            (doubtfully) continued. Default is true. */
        public LmlParserBuilder strict(final boolean strict) {
            lmlParser.setStrict(strict);
            return this;
        }

        /** @param forTag one of the tags handled by the selected tag parser.
         * @param parser allows to customize attribute parsing with selected keys. */
        public LmlParserBuilder attributeParser(final String forTag, final LmlTagAttributeParser parser) {
            lmlParser.registerAttributeParser(forTag, parser);
            return this;
        }

        /** @return LmlParser stored and modified by the builder. Note that due to complexity of registering parser
         *         attributes, builder stores an instance of LmlParser the entire time, rather than keeping the
         *         arguments and building the parser with this method. Use one instance of builder for one parser
         *         only. */
        public LmlParser build() {
            return lmlParser;
        }
    }
}
