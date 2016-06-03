package com.github.czyzby.uedi.stereotype.impl;

import com.github.czyzby.uedi.stereotype.Property;
import com.github.czyzby.uedi.stereotype.Provider;

/** Provides {@link String} instances. Manages {@link Property} instances.
 *
 * @author MJ */
public interface StringProvider extends Provider<String> {
    /** @param key unique ID of the property.
     * @return true if a property instance was registered to the key. */
    boolean hasProperty(String key);

    /** @param key unique ID of the property.
     * @return {@link Property} instance mapped to the ID or null if not registered. */
    Property getProperty(String key);

    /** @param property will be mapped to its {@link Property#getKey() key}. */
    void addProperty(Property property);
}