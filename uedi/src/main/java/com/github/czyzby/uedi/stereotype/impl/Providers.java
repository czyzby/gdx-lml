package com.github.czyzby.uedi.stereotype.impl;

import java.lang.reflect.Member;

import com.github.czyzby.uedi.stereotype.Default;
import com.github.czyzby.uedi.stereotype.Named;
import com.github.czyzby.uedi.stereotype.Provider;

/** Static utilities for provider-related operations.
 *
 * @author MJ */
public class Providers {
    /** 0-element object array. Pass to methods to avoid creating var-arg arrays when using reflection API. */
    public static final Object[] EMPTY_ARRAY = new Object[0];

    /** Do not initiate. */
    private Providers() {
    }

    /** @param member will determine its ID. Can be null.
     * @return member name or null if unavailable. */
    public static String getName(final Member member) {
        return member == null ? "default" : member.getName();
    }

    /** @param component will determine its ID.
     * @return name of the component if its {@link Named} or normalized simple name of its class. */
    public static String getName(final Object component) {
        if (component instanceof Named) {
            return ((Named) component).getName();
        }
        final String className = component.getClass().getSimpleName();
        if (className == null || className.isEmpty()) { // Anonymous class.
            return "unknown";
        }
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }

    /** @param provider will be validated.
     * @return whether the provider should be treated as the default one in case of ambiguous dependencies. */
    public static boolean isDefault(final Provider<?> provider) {
        return provider instanceof Default
                || provider instanceof DelegateProvider<?> && ((DelegateProvider<?>) provider).isDefault();
    }
}
