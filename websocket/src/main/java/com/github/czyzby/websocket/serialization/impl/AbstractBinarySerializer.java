package com.github.czyzby.websocket.serialization.impl;

import java.io.UnsupportedEncodingException;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Serializer;

/** Abstract base for serializers that convert objects to byte arrays. Methods consuming and producing strings convert
 * their data to byte arrays and delegate the operations to binary-based methods.
 *
 * @author MJ */
public abstract class AbstractBinarySerializer implements Serializer {
    @Override
    public String serializeAsString(final Object object) {
        try {
            return new String(serialize(object), "UTF-8");
        } catch (final UnsupportedEncodingException exception) {
            throw new SerializationException("Unexpected: UTF-8 format not supported.", exception);
        }
    }

    @Override
    public Object deserialize(final String data) {
        try {
            return deserialize(data.getBytes("UTF-8"));
        } catch (final UnsupportedEncodingException exception) {
            throw new SerializationException("Unexpected: UTF-8 format not supported.", exception);
        }
    }
}
