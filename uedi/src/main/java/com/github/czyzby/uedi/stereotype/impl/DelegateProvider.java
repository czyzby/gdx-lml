package com.github.czyzby.uedi.stereotype.impl;

import com.github.czyzby.uedi.stereotype.Provider;

/** Should be implemented by all providers that delegate their functionality to another object.
 *
 * @author MJ
 *
 * @param <Type> type of provided values. */
public interface DelegateProvider<Type> extends Provider<Type> {
    /** @return true if the wrapped object is marked as default. */
    boolean isDefault();

    /** @return instance of the wrapped object which performs the actual providing. */
    Object getWrappedObject();
}
