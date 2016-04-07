package com.github.czyzby.websocket.serialization;

import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

/** An interface representing an object that can be serialized with {@link Serializer} and deserialized with
 * {@link Deserializer}.
 *
 * @author MJ
 * @see Serializer
 * @see Deserializer */
public interface Transferable<Type extends Transferable<Type>> {
    /** Serializes the object. Note that in most cases serializing methods order should match
     * {@link #deserialize(Deserializer)} deserialization order.
     *
     * @param serializer should fully serialize the object, allowing it to be transfered.
     * @throws SerializationException if unable to serialize the object. */
    public void serialize(Serializer serializer) throws SerializationException;

    /** Deserializes the object, creating a new instance. Note that the implementation should match
     * {@link #serialize(Serializer)} serialization order.
     *
     * @param deserializer should create and fully deserialize an object, allowing it to be received.
     * @return object deserialized from the data contained by deserializer.
     * @throws SerializationException if unable to deserialize an object of this class. */
    public Type deserialize(Deserializer deserializer) throws SerializationException;
}
