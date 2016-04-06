package com.github.czyzby.websocket.serialization.impl;

import com.badlogic.gdx.utils.Json;
import com.github.czyzby.websocket.serialization.SerializationException;

/** Default object serializer. Uses {@link Json} to serialize object to strings.
 *
 * @author MJ */
public class JsonSerializer extends AbstractStringSerializer {
    private final Json json = new Json();

    @Override
    public String serializeAsString(final Object object) {
        try {
            return json.toJson(object);
        } catch (final Exception exception) {
            throw new SerializationException("Unable to serialize object to JSON.", exception);
        }
    }

    @Override
    public Object deserialize(final String data) {
        try {
            return json.fromJson(null, data);
        } catch (final Exception exception) {
            throw new SerializationException("Unable to deserialize object from JSON.", exception);
        }
    }

    /** @return direct reference to the {@link Json} serializer, which can be used to change serialization settings. */
    public Json getJson() {
        return json;
    }
}
