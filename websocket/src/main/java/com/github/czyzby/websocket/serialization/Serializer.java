package com.github.czyzby.websocket.serialization;

import com.github.czyzby.websocket.serialization.impl.Base64Serializer;
import com.github.czyzby.websocket.serialization.impl.JsonSerializer;

/** Common interface for packet serializers.
 *
 * @author MJ
 * @see JsonSerializer
 * @see Base64Serializer */
public interface Serializer {
    /** @param object will be serialized as string.
     * @return passed object serialized to a string. */
    String serializeAsString(Object object);

    /** @param object will be serialized.
     * @return passed object serialized to a byte array. */
    byte[] serialize(Object object);

    /** @param data serialized object.
     * @return an instance of deserialized object. */
    Object deserialize(String data);

    /** @param data serialized object.
     * @return an instance of deserialized object. */
    Object deserialize(byte[] data);
}
