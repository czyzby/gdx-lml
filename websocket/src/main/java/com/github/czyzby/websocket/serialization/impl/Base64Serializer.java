package com.github.czyzby.websocket.serialization.impl;

import java.io.UnsupportedEncodingException;

import com.badlogic.gdx.utils.Base64Coder;
import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Serializer;

/** Wraps around an existing {@link Serializer} to provide BASE64 encoding and decoding. Uses LibGDX {@link Base64Coder}
 * API.
 *
 * @author MJ */
public class Base64Serializer implements Serializer {
    private final Serializer serializer;
    private boolean useUrlSafeEncoding;

    public Base64Serializer(final Serializer serializer) {
        this.serializer = serializer;
    }

    /** @return wrapped serializer used for actual object (de)serializing. */
    public Serializer getSerializer() {
        return serializer;
    }

    @Override
    public String serializeAsString(final Object object) {
        final String serialized = serializer.serializeAsString(object);
        try {
            return Base64Coder.encodeString(serialized, useUrlSafeEncoding);
        } catch (final Exception exception) {
            throw new SerializationException("Unable to encode data into BASE64: " + serialized, exception);
        }
    }

    @Override
    public byte[] serialize(final Object object) {
        final byte[] serialized = serializer.serialize(object);
        try {
            return Base64Coder.encodeString(new String(serialized, "UTF-8"), useUrlSafeEncoding).getBytes("UTF-8");
        } catch (final UnsupportedEncodingException exception) {
            throw new SerializationException("Unexpected: UTF-8 format not supported.", exception);
        } catch (final Exception exception) {
            throw new SerializationException("Unable to encode data into BASE64.", exception);
        }
    }

    @Override
    public Object deserialize(final String data) {
        String decoded;
        try {
            decoded = Base64Coder.decodeString(data, useUrlSafeEncoding);
        } catch (final Exception exception) {
            throw new SerializationException("Unable to decode data from BASE64: " + data, exception);
        }
        return serializer.deserialize(decoded);
    }

    @Override
    public Object deserialize(final byte[] data) {
        String decoded;
        try {
            decoded = Base64Coder.decodeString(new String(data, "UTF-8"), useUrlSafeEncoding);
        } catch (final UnsupportedEncodingException exception) {
            throw new SerializationException("Unexpected: UTF-8 format not supported.", exception);
        } catch (final Exception exception) {
            throw new SerializationException("Unable to decode data from BASE64: " + data, exception);
        }
        return serializer.deserialize(decoded);
    }

    /** @param useUrlSafeEncoding if true, encoded data will be URL-safe.
     * @see Base64Coder#encodeString(String, boolean)
     * @see Base64Coder#decodeString(String, boolean) */
    public void setUseUrlSafeEncoding(final boolean useUrlSafeEncoding) {
        this.useUrlSafeEncoding = useUrlSafeEncoding;
    }
}
