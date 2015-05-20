package com.github.czyzby.lml.parser;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.lml.error.LmlParsingException;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;
import com.github.czyzby.lml.parser.impl.dto.ActorConsumer;

/** Universal interface of LML parsers. Allows to implement a custom parser, using modified language version or
 * completely custom widgets. Scene2DLmlParser is the default implementation, creating Scene2D widgets.
 *
 * @author MJ */
public interface LmlParser {
	/** @param lmlFile file handle of a LML file.
	 * @return actors specified by the LML file. */
	Array<Actor> parse(FileHandle lmlFile) throws LmlParsingException;

	/** @param lmlDocument has to use LML syntax.
	 * @return actors specified by the LML document. */
	Array<Actor> parse(String lmlDocument) throws LmlParsingException;

	/** @param lmlFile file handle of a LML file.
	 * @param stage will contain all parsed actors. */
	void fill(Stage stage, FileHandle lmlFile) throws LmlParsingException;

	/** @param lmlDocument has to use LML syntax.
	 * @param stage will contain all parsed actors. */
	void fill(Stage stage, String lmlDocument) throws LmlParsingException;

	/** @return skin used to create widgets. */
	Skin getSkin();

	/** @param skin used to create widgets. */
	void setSkin(Skin skin);

	/** @return bundle used to parse text proceeded with @. */
	I18NBundle getDefaultI18nBundle();

	/** @param i18nBundle used to parse text proceeded with @. */
	void setDefaultI18nBundle(I18NBundle i18nBundle);

	/** @return bundle used to parse text proceeded with @, i18n bundle name and another @. */
	I18NBundle getI18nBundle(String forName);

	/** @param i18nBundle used to parse text proceeded with @, i18n bundle name and another @. */
	void setI18nBundle(String bundleName, I18NBundle i18nBundle);

	/** @return preferences which values can be referenced with #. */
	Preferences getDefaultPreferences();

	/** @param preferences its values can be referenced with #. */
	void setDefaultPreferences(Preferences preferences);

	/** @return preferences which values can be referenced with #name#. */
	Preferences getPreferences(String forName);

	/** @param preferences its values can be referenced with #name#. */
	void setPreferences(String preferencesName, Preferences preferences);

	/** Mostly for internal use.
	 *
	 * @param rawData starts with # or #preferencesKey#; the rest is the actual preference name.
	 * @return preference value or NULL. */
	String getPreference(final String rawData);

	/** For internal use only. Extracts bundle line from i18n bundle or attributes, provided it follows the
	 * syntax.
	 *
	 * @param rawData a possible bundle line or preference.
	 * @return rawData if the text is not considered a bundle line or preference. Converted line otherwise. */
	String parseStringData(String rawData, Actor forActor);

	/** Registers an action container with the given ID. ID can be referenced with onClick and onChange
	 * attributes before the method. */
	void addActionContainer(String containerId, ActionContainer actionContainer);

	/** Unregisters action container with the selected ID. */
	void removeActionContainer(String containerId);

	/** Registers action containers with the given IDs. IDs can be referenced with onClick and onChange
	 * attributes before the method. */
	void addActionContainers(ObjectMap<String, ActionContainer> actionContainers);

	/** Registers action containers with the given IDs. IDs can be referenced with onClick and onChange
	 * attributes before the method. */
	void setActionContainers(ObjectMap<String, ActionContainer> actionContainers);

	/** @return actions mapped by action IDs. */
	ObjectMap<String, ActorConsumer<?, ?>> getActions();

	/** Registers an action with the given ID. ID can be referenced with onClick and onChange attributes. */
	void addAction(String actionId, ActorConsumer<?, ?> action);

	/** Removes action referenced by the selected ID. */
	void removeAction(String actionId);

	/** Registers actions with the given IDs. IDs can be referenced with onClick and onChange attributes. */
	void addActions(ObjectMap<String, ActorConsumer<?, ?>> actions);

	/** Registers actions with the given IDs, replacing the old ones (if any). IDs can be referenced with
	 * onClick and onChange attributes. */
	void setActions(ObjectMap<String, ActorConsumer<?, ?>> actions);

	/** Mostly for internal use.
	 *
	 * @param actionId ID associated with the action. ActorConsumer name, ActionContainer method name or
	 *            ActionContainer ID followed by method name, separated by dot.
	 * @param forActor Actor that is expected to be consumed by the return consumer.
	 * @return ActorConsumer connected with the key or null. */
	public ActorConsumer<Object, Object> findAction(final String actionId, final Actor forActor);

	/** Mostly for internal use.
	 *
	 * @param actionId ID associated with the action. ActorConsumer name, ActionContainer method name or
	 *            ActionContainer ID followed by method name, separated by dot.
	 * @param parameterClass class of the parameter that is expected to be consumed by the ActorConsumer.
	 * @return ActorConsumer connected with the key or null. */
	public ActorConsumer<Object, Object> findAction(final String actionId, final Class<?> parameterClass);

	/** @param strict if true, parser will throw exceptions for actions that would be normally ignored, like
	 *            trying to append a child to a widget that is not considered an actor group and cannot have
	 *            children. Defaults to true. */
	void setStrict(boolean strict);

	/** @return if true, parser will throw exceptions for actions that would be normally ignored, like trying to
	 *         append a child to a widget that is not considered an actor group and cannot have children.
	 *         Defaults to true. */
	boolean isStrict();

	/** Associates chosen parser with the given tag name. Overrides previous settings if tag is already
	 * registered. */
	void registerParser(String tagName, LmlTagDataParser<?> parser);

	/** Associates chosen parser with the given tag names. Overrides previous settings if tag is already
	 * registered. */
	void registerParser(LmlTagDataParser<?> parser, String... tagNames);

	/** Associates chosen parsers with the given tag names. Overrides previous settings if tags are already
	 * registered. */
	void registerParsers(ObjectMap<String, LmlTagDataParser<?>> parsersMappedByTagNames);

	/** Associates chosen parsers with the given tag names. Deletes and overrides previous settings. */
	void setParsers(ObjectMap<String, LmlTagDataParser<?>> parsersMappedByTagNames);

	/** Associates chosen macro parser with the given tag name. Overrides previous settings if tag is already
	 * registered. */
	void registerMacroParser(String tagName, LmlMacroParser parser);

	/** Associates chosen macro parser with the given tag names. Overrides previous settings if tag is already
	 * registered. */
	void registerMacroParser(LmlMacroParser parser, String... tagNames);

	/** Associates chosen macro parsers with the given tag names. Overrides previous settings if tags are
	 * already registered. */
	void registerMacroParsers(ObjectMap<String, LmlMacroParser> parsersMappedByTagNames);

	/** Associates chosen macro parsers with the given tag names. Deletes and overrides previous settings. */
	void setMacroParsers(ObjectMap<String, LmlMacroParser> parsersMappedByTagNames);

	/** @param lmlTextToParse if in the middle for parsing, this text will be appended to the place where the
	 *            original text is currently parsed. If before parsing, behavior is undetermined. Should be
	 *            used for internal parsing cases only. */
	void appendToBuffer(CharSequence lmlTextToParse);

	/** Associates given argument with the key. Arguments can be referenced with ${key} in .lml. */
	void addArgument(String key, int value);

	/** Associates given argument with the key. Arguments can be referenced with ${key} in .lml. */
	void addArgument(String key, float value);

	/** Associates given argument with the key. Arguments can be referenced with ${key} in .lml. */
	void addArgument(String key, String value);

	/** Converts the argument to a range array and associates it with the key. Arguments can be referenced with
	 * ${key} in .lml. */
	void addArgument(String key, String baseValue, int rangeStart, int rangeEnd);

	/** Converts the argument to an array and associates it with the key. Arguments can be referenced with
	 * ${key} in .lml. */
	void addArgument(String key, String... values);

	/** Converts the argument to an array and associates it with the key. Arguments can be referenced with
	 * ${key} in .lml. */
	void addArgument(String key, Iterable<String> values);

	/** Registers all given arguments. Arguments can be referenced with ${key} in .lml. */
	void addArguments(ObjectMap<String, String> arguments);

	/** Registers actionKey proceeded with &amp; to the argument key. Note that actions in tag attributes (like
	 * onClick=someAction) can, but do not have to be proceeded with &amp; - method invocation arguments are
	 * meant for macros that will parse them to the method return results. */
	void addMethodInvocationArgument(String key, String actionKey);

	/** Registers joined actionContainerKey and methodName, proceeded with &amp; to the argument key. Note that
	 * actions in tag attributes (like onClick=someAction) can, but do not have to be proceeded with &amp; -
	 * method invocation arguments are meant for macros that will parse them to the method return results. */
	void addMethodInvocationArgument(String key, String actionContainerKey, String methodName);

	/** Replaces current arguments with passed values. Arguments can be referenced with &amp;{key} in .lml. */
	void setArguments(ObjectMap<String, String> arguments);

	/** @param actor will be mapped by its name (ID) in the parser. Note that it's done internally when parsing
	 *            widgets, but additional actors can be manually appended. */
	void mapActorById(Actor actor);

	/** Available after parsing.
	 *
	 * @return all actors that were appended with ID attribute in their tags. */
	ObjectMap<String, Actor> getActorsMappedById();

	/** Resets map that keeps track of Actors mapped by their ID. Note that it's never done by the parser
	 * itself, so if a parser is used multiple times by documents that might have colliding IDs, it's a good
	 * idea to reset the map. */
	void clearActorsMappedById();

	/** For internal use and more informant exceptions.
	 *
	 * @return currently parsed line of the original file. */
	int getCurrentlyParsedLine();

	/** @param tagName one of the tags handled by the selected tag parser.
	 * @param parser will be registered as one of attribute parsers for the selected tag parser. Note that you
	 *            can also register attributes globally by accessing tag parsers' static methods. */
	void registerAttributeParser(String tagName, LmlTagAttributeParser parser);

	/** @param tagName one of the tags handled by the selected tag parser.
	 * @param attributeName attribute parser connected with this name will be unregistered for tag parser with
	 *            the passed tag name. Note that you can also unregister attributes globally by accessing tag
	 *            parsers' static methods. */
	void unregisterAttributeParser(String tagName, String attributeName);

	/** @return name of the last document parsed from a file. Internal utility. */
	String getLastParsedDocumentName();
}
