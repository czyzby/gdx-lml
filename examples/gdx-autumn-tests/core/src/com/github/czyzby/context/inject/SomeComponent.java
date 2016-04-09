package com.github.czyzby.context.inject;

import com.github.czyzby.autumn.annotation.Component;

/** This is an example injected by other components. It prints its hash code on {@link #toString()} to prove that the
 * same instance is injected everywhere.
 *
 * @author MJ */
@Component
public class SomeComponent {
    @Override
    public String toString() {
        return "(SomeComponent[hashCode=" + hashCode() + "])";
    }
}
