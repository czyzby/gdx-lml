package com.github.czyzby.lml.parser.impl;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.kiwi.util.common.Nullables;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.LmlMacroParser;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlTagAttributeParser;
import com.github.czyzby.lml.parser.LmlTagDataParser;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;
import com.github.czyzby.lml.parser.impl.dto.ActorConsumer;
import com.github.czyzby.lml.parser.impl.dto.LmlParent;
import com.github.czyzby.lml.parser.impl.dto.StageAttacher;
import com.github.czyzby.lml.util.LmlSyntax;

/** Provides utility methods, variables and default method implementations, allowing to create a LmlParser
 * easier.
 *
 * @author MJ */
public abstract class AbstractLmlParser implements LmlParser, LmlSyntax {
	// Originally used Deque for these two, but it doesn't seem to be supported by GWT (at least 2.6.0).
	protected final LinkedList<Queue<Character>> buffer = new LinkedList<Queue<Character>>();
	protected final LinkedList<LmlParent<?>> widgetsHierarchy = new LinkedList<LmlParent<?>>();
	protected final ObjectMap<String, LmlTagDataParser<?>> tagParsers = GdxMaps.newObjectMap();
	protected final ObjectMap<String, LmlMacroParser> macroParsers = GdxMaps.newObjectMap();
	protected final ObjectMap<String, ActionContainer> actionContainers = GdxMaps.newObjectMap();
	protected final ObjectMap<String, ActorConsumer<?, ?>> actions = GdxMaps.newObjectMap();
	protected final ObjectMap<String, Actor> actorsByIds = GdxMaps.newObjectMap();
	protected final ObjectMap<String, String> arguments = GdxMaps.newObjectMap();
	private boolean strict = true;

	protected final ObjectMap<String, I18NBundle> i18nBundles = GdxMaps.newObjectMap();
	protected final ObjectMap<String, Preferences> preferences = GdxMaps.newObjectMap();
	protected Skin skin;

	public AbstractLmlParser(final Skin skin) {
		this.skin = skin;
	}

	public AbstractLmlParser(final Skin skin, final I18NBundle i18nBundle) {
		this.skin = skin;
		i18nBundles.put(DEFAULT_VALUE_NAME, i18nBundle);
	}

	public AbstractLmlParser(final Skin skin, final I18NBundle i18nBundle, final Preferences preferences) {
		this.skin = skin;
		i18nBundles.put(DEFAULT_VALUE_NAME, i18nBundle);
		this.preferences.put(DEFAULT_VALUE_NAME, preferences);
	}

	public AbstractLmlParser(final AbstractLmlParser parser) {
		tagParsers.putAll(parser.tagParsers);
		macroParsers.putAll(parser.macroParsers);
		actionContainers.putAll(parser.actionContainers);
		actions.putAll(parser.actions);
		arguments.putAll(parser.arguments);
		i18nBundles.putAll(parser.i18nBundles);
		preferences.putAll(parser.preferences);
		strict = parser.strict;
	}

	@Override
	public Array<Actor> parse(final FileHandle lmlFile) throws LmlParsingException {
		return parse(lmlFile.readString());
	}

	@Override
	public void fill(final Stage stage, final FileHandle lmlFile) throws LmlParsingException {
		fill(stage, lmlFile.readString());
	}

	@Override
	public void fill(final Stage stage, final String lmlDocument) throws LmlParsingException {
		for (final Actor actor : parse(lmlDocument)) {
			stage.addActor(actor);
			if (actor.getUserObject() instanceof StageAttacher) {
				((StageAttacher) actor.getUserObject()).attachToStage(stage);
			}
		}
	}

	@Override
	public Skin getSkin() {
		return skin;
	}

	@Override
	public void setSkin(final Skin skin) {
		this.skin = skin;
	}

	@Override
	public I18NBundle getDefaultI18nBundle() {
		return i18nBundles.get(DEFAULT_VALUE_NAME);
	}

	@Override
	public void setDefaultI18nBundle(final I18NBundle i18nBundle) {
		i18nBundles.put(DEFAULT_VALUE_NAME, i18nBundle);
	}

	@Override
	public I18NBundle getI18nBundle(final String forName) {
		return i18nBundles.get(forName);
	}

	@Override
	public void setI18nBundle(final String bundleName, final I18NBundle i18nBundle) {
		i18nBundles.put(bundleName, i18nBundle);
	}

	@Override
	public Preferences getDefaultPreferences() {
		return preferences.get(DEFAULT_VALUE_NAME);
	}

	@Override
	public void setDefaultPreferences(final Preferences preferences) {
		this.preferences.put(DEFAULT_VALUE_NAME, preferences);
	}

	@Override
	public Preferences getPreferences(final String forName) {
		return preferences.get(forName);
	}

	@Override
	public void setPreferences(final String preferencesName, final Preferences preferences) {
		this.preferences.put(preferencesName, preferences);
	}

	@Override
	public String getPreference(final String rawData) {
		final String preferenceData = rawData.substring(1); // Omitting #.
		final Preferences preferences;
		final String preferenceKey;
		if (Strings.contains(preferenceData, PREFERENCE_SIGN)) {
			final String[] preference = preferenceData.split(PREFERENCE_SEPARATOR);
			preferences = this.preferences.get(preference[0]);
			preferenceKey = preference[1];
		} else {
			preferences = getDefaultPreferences();
			preferenceKey = preferenceData;
		}
		return preferences == null ? NULL_ARGUMENT : preferences.getString(preferenceKey, NULL_ARGUMENT);
	}

	protected boolean isNewLine(final char character) {
		return NEW_LINE == character || CARRIAGE_RETURN == character;
	}

	protected boolean isMacro(final CharSequence rawTagData) {
		return rawTagData.charAt(0) == MACRO_SIGN;
	}

	@Override
	public String parseStringData(final String rawData, final Actor forActor) {
		if (rawData == null || rawData.length() == 0) {
			return "";
		}
		if (GdxMaps.isNotEmpty(i18nBundles) && rawData.charAt(0) == BUNDLE_LINE_SIGN) {
			return parseStringDataFromBundle(rawData);
		}
		if (GdxMaps.isNotEmpty(preferences) && rawData.charAt(0) == PREFERENCE_SIGN) {
			return getPreference(rawData);
		}
		if (rawData.charAt(0) == ACTION_OPERATOR) {
			final ActorConsumer<Object, Object> action = findAction(rawData.substring(1), forActor);
			if (action != null) {
				return Nullables.toString(action.consume(forActor), NULL_ARGUMENT);
			}
		}
		return rawData;
	}

	private String parseStringDataFromBundle(final String rawData) {
		final String bundleData = rawData.substring(1); // Ommiting @.
		String bundleKey;
		I18NBundle i18nBundle;
		if (Strings.contains(bundleData, BUNDLE_LINE_SIGN)) {
			final String[] bundleDescriptor = bundleData.split(BUNDLE_LINE_SEPARATOR);
			i18nBundle = i18nBundles.get(bundleDescriptor[0]);
			bundleKey = bundleDescriptor[1];
		} else {
			i18nBundle = getDefaultI18nBundle();
			bundleKey = bundleData;
		}

		final String[] bundleLine = bundleKey.split(BUNDLE_LINE_ARGUMENT_SEPARATOR);
		if (bundleLine.length == 0) {
			throw new LmlParsingException("Invalid bundle line invocation: " + rawData + ".", this);
		} else if (bundleLine.length == 1) {
			return i18nBundle.format(bundleLine[0]);
		}
		final String[] arguments = new String[bundleLine.length - 1];
		System.arraycopy(bundleLine, 1, arguments, 0, arguments.length);
		return i18nBundle.format(bundleLine[0], (Object[]) arguments);
	}

	@Override
	public ObjectMap<String, Actor> getActorsMappedById() {
		return actorsByIds;
	}

	@Override
	public ObjectMap<String, ActionContainer> getActionContainers() {
		return actionContainers;
	}

	@Override
	public void setActionContainers(final ObjectMap<String, ActionContainer> actionContainers) {
		this.actionContainers.clear();
		this.actionContainers.putAll(actionContainers);
	}

	@Override
	public void addActionContainer(final String containerId, final ActionContainer actionContainer) {
		actionContainers.put(containerId, actionContainer);
	}

	@Override
	public void addActionContainers(final ObjectMap<String, ActionContainer> actionContainers) {
		this.actionContainers.putAll(actionContainers);
	}

	@Override
	public ObjectMap<String, ActorConsumer<?, ?>> getActions() {
		return actions;
	}

	@Override
	public void setActions(final ObjectMap<String, ActorConsumer<?, ?>> actions) {
		this.actions.clear();
		this.actions.putAll(actions);
	}

	@Override
	public void addAction(final String actionId, final ActorConsumer<?, ?> action) {
		actions.put(actionId, action);
	}

	@Override
	public void addActions(final ObjectMap<String, ActorConsumer<?, ?>> actions) {
		this.actions.putAll(actions);
	}

	@Override
	public void setStrict(final boolean strict) {
		this.strict = strict;
	}

	@Override
	public boolean isStrict() {
		return strict;
	}

	@Override
	public void registerParser(final String tagName, final LmlTagDataParser<?> parser) {
		tagParsers.put(tagName.toUpperCase(), parser);
	}

	@Override
	public void registerParser(final LmlTagDataParser<?> parser, final String... tagNames) {
		for (final String tagName : tagNames) {
			registerParser(tagName, parser);
		}
	}

	@Override
	public void registerParsers(final ObjectMap<String, LmlTagDataParser<?>> parsersMappedByTagNames) {
		for (final Entry<String, LmlTagDataParser<?>> parser : parsersMappedByTagNames) {
			registerParser(parser.key, parser.value);
		}
	}

	@Override
	public void setParsers(final ObjectMap<String, LmlTagDataParser<?>> parsersMappedByTagNames) {
		tagParsers.clear();
		registerParsers(parsersMappedByTagNames);
	}

	@Override
	public void registerMacroParser(final String tagName, final LmlMacroParser parser) {
		macroParsers.put(tagName.toUpperCase(), parser);
	}

	@Override
	public void registerMacroParser(final LmlMacroParser parser, final String... tagNames) {
		for (final String tagName : tagNames) {
			registerMacroParser(tagName, parser);
		}
	}

	@Override
	public void registerMacroParsers(final ObjectMap<String, LmlMacroParser> parsersMappedByTagNames) {
		for (final Entry<String, LmlMacroParser> parser : parsersMappedByTagNames) {
			registerMacroParser(parser.key, parser.value);
		}
	}

	@Override
	public void setMacroParsers(final ObjectMap<String, LmlMacroParser> parsersMappedByTagNames) {
		macroParsers.clear();
		registerMacroParsers(parsersMappedByTagNames);
	}

	/** @return currently parsed parent. Can be null. Utility method. */
	protected LmlParent<?> getCurrentParent() {
		if (widgetsHierarchy.isEmpty()) {
			return null;
		}
		return widgetsHierarchy.getFirst();
	}

	@Override
	public void appendToBuffer(final CharSequence lmlTextToParse) {
		if (lmlTextToParse == null || lmlTextToParse.length() == 0) {
			return;
		}
		final Queue<Character> sequence = new LinkedList<Character>();
		for (final char character : lmlTextToParse.toString().toCharArray()) {
			sequence.add(character);
		}
		buffer.addFirst(sequence);
	}

	/** @param errorMessage exception message. Can be null. */
	protected void throwErrorIfStrict(final String errorMessage) {
		if (isStrict()) {
			if (errorMessage == null || errorMessage.length() == 0) {
				throw new LmlParsingException(this);
			}
			throw new LmlParsingException(errorMessage, this);
		}
	}

	/** @param errorMessage exception message. Can be null. */
	protected void throwErrorIfStrict(final String errorMessage, final Throwable cause) {
		if (isStrict()) {
			if (errorMessage == null || errorMessage.length() == 0) {
				throw new LmlParsingException(this, cause);
			}
			throw new LmlParsingException(errorMessage, this, cause);
		}
	}

	@Override
	public void addArgument(final String key, final String value) {
		if (value != null) {
			arguments.put(key, value);
		} else {
			arguments.put(key, NULL_ARGUMENT);
		}
	}

	@Override
	public void addArgument(final String key, final String... values) {
		if (values.length == 0) {
			addArgument(key, NULL_ARGUMENT);
		} else if (values.length == 1) {
			addArgument(key, values[0]);
		} else {
			int index = 0;
			final StringBuilder argument = new StringBuilder(values[index++]);
			for (; index < values.length; index++) {
				argument.append(ARGUMENT_SEPARATOR).append(values[index]);
			}
			addArgument(key, argument.toString());
		}
	}

	@Override
	public void addArgument(final String key, final Iterable<String> values) {
		addArgument(key, toArray(values));
	}

	private String[] toArray(final Iterable<String> values) {
		final Array<String> array = GdxArrays.newArray(String.class);
		for (final String value : values) {
			array.add(value);
		}
		return array.toArray();
	}

	@Override
	public void addArgument(final String key, final String baseValue, final int rangeStart, final int rangeEnd) {
		addArgument(key, toRangeArgument(baseValue, rangeStart, rangeEnd));
	}

	public static String toRangeArgument(final String baseValue, final int rangeStart, final int rangeEnd) {
		if (rangeStart < 0 || rangeEnd < 0) {
			throw new LmlParsingException("Range values cannot be negative.");
		}
		return baseValue + RANGE_ARGUMENT_OPENING + rangeStart + RANGE_ARGUMENT_SEPARATOR + rangeEnd
				+ RANGE_ARGUMENT_CLOSING;
	}

	@Override
	public void addArguments(final ObjectMap<String, String> arguments) {
		this.arguments.putAll(arguments);
	}

	@Override
	public void addMethodInvocationArgument(final String key, final String actionKey) {
		arguments.put(key, ACTION_OPERATOR + actionKey);
	}

	@Override
	public void addMethodInvocationArgument(final String key, final String actionContainerKey,
			final String methodName) {
		addMethodInvocationArgument(key, actionContainerKey + ACTION_SEPARATOR + methodName);
	}

	@Override
	public void setArguments(final ObjectMap<String, String> arguments) {
		this.arguments.clear();
		this.arguments.putAll(arguments);
	}

	protected String getArgument(final String key) {
		final String argument = arguments.get(key);
		return argument == null ? NULL_ARGUMENT : argument;
	}

	@Override
	public ActorConsumer<Object, Object> findAction(final String actionId, final Actor forActor) {
		return findAction(actionId, forActor.getClass());
	}

	@Override
	@SuppressWarnings("unchecked")
	public ActorConsumer<Object, Object> findAction(final String actionId, final Class<?> parameterClass) {
		ActorConsumer<Object, Object> action = (ActorConsumer<Object, Object>) actions.get(actionId);
		if (action == null && actionId.indexOf(ACTION_SEPARATOR) != -1) {
			action = findMethodWithContainerId(actionId, parameterClass);
		}
		if (action == null) {
			action = findMethod(actionId, parameterClass);
		}
		if (action == null) {
			throwErrorIfStrict("Referenced unregistered action: " + actionId + ".");
		}
		return action;
	}

	private ActorConsumer<Object, Object> findMethod(final String actionId, final Class<?> parameterClass) {
		ActorConsumer<Object, Object> actorConsumer = null;
		for (final ActionContainer actionContainer : actionContainers.values()) {
			actorConsumer = getActorConsumerForContainer(actionId, actionContainer, parameterClass);
			if (actorConsumer != null) {
				break;
			}
		}
		return actorConsumer;
	}

	private ActorConsumer<Object, Object> findMethodWithContainerId(final String actionId,
			final Class<?> parameterClass) {
		final String[] actionData = actionId.split(ACTION_SEPARATOR_REGEX);
		if (actionData.length != 2) {
			throwErrorIfStrict("Unexpected action ID: " + actionId + ".");
			return null;
		}
		final String actionContainerId = actionData[0];
		final String actionName = actionData[1];
		final ActionContainer actionContainer = actionContainers.get(actionContainerId);
		if (actionContainer == null) {
			throwErrorIfStrict("Referenced unregistered action container: " + actionContainerId + ".");
			return null;
		}
		return getActorConsumerForContainer(actionName, actionContainer, parameterClass);
	}

	private ActorConsumer<Object, Object> getActorConsumerForContainer(final String actionName,
			final ActionContainer actionContainer, final Class<?> parameterClass) {
		final Method method = getMethod(actionName, parameterClass, actionContainer);
		if (method == null) {
			return null;
		}
		return prepareActorConsumerForMethod(actionContainer, method);
	}

	private Method getMethod(final String methodName, final Class<?> parameterClass,
			final ActionContainer actionContainer) {
		if (parameterClass == null) {
			return null;
		}
		try {
			return ClassReflection.getMethod(actionContainer.getClass(), methodName, parameterClass);
		} catch (final ReflectionException exception) {
			return getMethod(methodName, parameterClass.getSuperclass(), actionContainer);
		}
	}

	private ActorConsumer<Object, Object> prepareActorConsumerForMethod(
			final ActionContainer actionContainer, final Method method) {
		return new ActorConsumer<Object, Object>() {
			@Override
			public Object consume(final Object actor) {
				try {
					return method.invoke(actionContainer, actor);
				} catch (final ReflectionException exception) {
					throw new RuntimeException("An error occured while executing action.", exception);
				}
			}
		};
	}

	@Override
	public void mapActorById(final Actor actor) {
		actorsByIds.put(actor.getName(), actor);
	}

	@Override
	public void clearActorsMappedById() {
		actorsByIds.clear();
	}

	@Override
	public void registerAttributeParser(String tagName, final LmlTagAttributeParser parser) {
		tagName = tagName.toUpperCase();
		if (!tagParsers.containsKey(tagName)) {
			throw new IllegalArgumentException("Unknown tag: " + tagName
					+ ". Cannot register attribute parser.");
		}
		tagParsers.get(tagName).registerAttributeParser(parser);
	}

	@Override
	public void unregisterAttributeParser(final String tagName, final String attributeName) {
		if (!tagParsers.containsKey(tagName)) {
			throw new IllegalArgumentException("Unknown tag: " + tagName
					+ ". Cannot unregister attribute parser.");
		}
		tagParsers.get(tagName).unregisterAttributeParser(attributeName);
	}
}
