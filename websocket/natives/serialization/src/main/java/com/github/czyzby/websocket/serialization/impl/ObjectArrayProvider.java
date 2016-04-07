package com.github.czyzby.websocket.serialization.impl;

import com.github.czyzby.websocket.serialization.ArrayProvider;

/** Constructs non-typed arrays.
 *
 * @author MJ */
public class ObjectArrayProvider implements ArrayProvider<Object> {
    @Override
    public Object[] getArray(final int size) {
        return new Object[size];
    }
}
