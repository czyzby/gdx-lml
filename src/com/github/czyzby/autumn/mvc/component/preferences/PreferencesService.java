package com.github.czyzby.autumn.mvc.component.preferences;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.Field;
import com.github.czyzby.autumn.annotation.Destroy;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.context.Context;
import com.github.czyzby.autumn.context.ContextDestroyer;
import com.github.czyzby.autumn.context.ContextInitializer;
import com.github.czyzby.autumn.mvc.component.preferences.dto.Preference;
import com.github.czyzby.autumn.mvc.component.preferences.dto.ReflectionPreference;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.config.AutumnActionPriority;
import com.github.czyzby.autumn.mvc.stereotype.preference.Property;
import com.github.czyzby.autumn.processor.AbstractAnnotationProcessor;
import com.github.czyzby.kiwi.util.common.Exceptions;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;

/** Manages application's preferences.
 *
 * @author MJ
 * @see Property */
public class PreferencesService extends AbstractAnnotationProcessor<Property> {
    /** Prefix used to add action of accessing the preference in LML templates. This value will be added before
     * preference name to construct the action. */
    public static String GETTER_PREFIX = "get", SETTER_PREFIX = "set";

    private final ObjectMap<String, Preference<?>> preferences = GdxMaps.newObjectMap();
    private final ObjectMap<String, Preferences> namesToFiles = GdxMaps.newObjectMap();

    @Inject private InterfaceService interfaceService;

    @Override
    public Class<Property> getSupportedAnnotationType() {
        return Property.class;
    }

    @Override
    public boolean isSupportingFields() {
        return true;
    }

    @Override
    public void processField(final Field field, final Property annotation, final Object component,
            final Context context, final ContextInitializer initializer, final ContextDestroyer contextDestroyer) {
        final String name = Strings.isBlank(annotation.value()) ? field.getName() : annotation.value();
        addPreference(name, annotation.preferences(),
                new ReflectionPreference(field.isStatic() ? null : component, field));
    }

    /** @param name name of the preference. Will determine how the preference is saved in the file. set(name) and
     *            get(name) methods will be added to LML templates.
     * @param preferencesName name of the registered preferences.
     * @param preference container of the preference. Will be initiated (read). */
    public void addPreference(final String name, final String preferencesName, final Preference<?> preference) {
        preferences.put(name, preference);
        final Preferences preferences = getPreferences(preferencesName);
        namesToFiles.put(name, preferences);
        initiatePreference(name, preference, preferences);
        addLmlActions(name, preference);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void initiatePreference(final String name, final Preference preference,
            final Preferences preferences) {
        if (preferences.contains(name)) {
            try {
                preference.read(name, preferences);
            } catch (final Exception exception) {
                Exceptions.ignore(exception);
                preference.set(preference.getDefault());
            }
        } else {
            preference.set(preference.getDefault());
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addLmlActions(final String name, final Preference<?> preference) {
        interfaceService.getParser().getData().addActorConsumer(GETTER_PREFIX + name,
                new Preference.PreferenceGetter(preference));
        interfaceService.getParser().getData().addActorConsumer(SETTER_PREFIX + name,
                new Preference.PreferenceSetter(preference));
    }

    /** @param preferencesName name of the registered preferences.
     * @return preferences object mapped to the selected name. */
    public Preferences getPreferences(final String preferencesName) {
        return interfaceService.getParser().getData().getPreferences(preferencesName);
    }

    @Override
    public boolean isSupportingTypes() {
        return true;
    }

    @Override
    public void processType(final Class<?> type, final Property annotation, final Object component,
            final Context context, final ContextInitializer initializer, final ContextDestroyer contextDestroyer) {
        if (component instanceof Preference<?>) {
            addPreference(annotation.value(), annotation.preferences(), (Preference<?>) component);
        } else {
            throw new GdxRuntimeException(
                    "Invalid type annotated with @Property: expecting Preference, got: " + component);
        }
    }

    /** Saves all current preferences. This is a reasonably heavy operation, as it flushes all preferences files - by
     * default, this is done once, before the application is closed. */
    @Destroy(priority = AutumnActionPriority.MIN_PRIORITY)
    public void savePreferences() {
        final ObjectSet<Preferences> preferencesToFlush = GdxSets.newSet();
        for (final Entry<String, Preference<?>> preference : preferences) {
            final Preferences preferencesFile = namesToFiles.get(preference.key);
            preferencesToFlush.add(preferencesFile);
            preference.value.save(preference.key, preferencesFile);
        }
        for (final Preferences preferencesFile : preferencesToFlush) {
            preferencesFile.flush();
        }
    }
}
