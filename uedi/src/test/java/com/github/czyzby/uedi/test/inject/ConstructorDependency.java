package com.github.czyzby.uedi.test.inject;

import com.github.czyzby.uedi.stereotype.Singleton;

public class ConstructorDependency implements Singleton {
    private final CircularA a;
    private final CircularB b;
    private final Injected injected = null;

    public ConstructorDependency(final CircularA a, final CircularB b) {
        this.a = a;
        this.b = b;
    }

    public CircularA getA() {
        return a;
    }

    public CircularB getB() {
        return b;
    }

    public Injected getInjected() {
        return injected;
    }
}
