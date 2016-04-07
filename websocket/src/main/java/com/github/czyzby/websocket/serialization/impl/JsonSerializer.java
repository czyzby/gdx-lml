package com.github.czyzby.websocket.serialization.impl;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.github.czyzby.websocket.serialization.SerializationException;

/** Default object serializer. Uses {@link Json} to serialize object to strings.
 *
 * @author MJ */
public class JsonSerializer extends AbstractStringSerializer {
    private final Json json = new Json();

    public JsonSerializer() {
        json.setOutputType(OutputType.javascript);
    }

    /** @param preserveClassName if true, object's class name will be added to the JSON representation under "class"
     *            key. If false, class data will not be preserved. Default to true. Note that if you set this value to
     *            false, deserialization might fail or return {@link com.badlogic.gdx.utils.JsonValue} instead of
     *            instance of the actual serialized object. Set to false only if you don't need the class data or your
     *            server is not using the same serialization and does not need class data. */
    public void setPreserveClassName(final boolean preserveClassName) {
        json.setTypeName(preserveClassName ? "class" : null);
    }

    @Override
    public String serializeAsString(final Object object) {
        try {
            return json.toJson(object, Object.class);
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
