package com.github.czyzby.websocket.serialization;

/** Utility for deserialization, allowing to create typed arrays of generic objects without knowing the size beforehand.
 *
 * @author MJ
 *
 * @param <Type> base class of the array.
 * @see com.github.czyzby.websocket.serialization.impl.ReflectionArrayProvider */
public interface ArrayProvider<Type> {
    /** @param size size of the array to create.
     * @return a new array of chosen type. */
    Type[] getArray(int size);
}
