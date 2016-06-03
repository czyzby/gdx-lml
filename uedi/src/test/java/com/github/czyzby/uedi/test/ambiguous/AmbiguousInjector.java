package com.github.czyzby.uedi.test.ambiguous;

import com.github.czyzby.uedi.stereotype.Singleton;

public class AmbiguousInjector implements Singleton {
    private Ambiguous ambiguousA;
    private Ambiguous ambiguousB;
    private Ambiguous named;

    public Ambiguous getA() {
        return ambiguousA;
    }

    public Ambiguous getB() {
        return ambiguousB;
    }

    public Ambiguous getNamed() {
        return named;
    }
}
