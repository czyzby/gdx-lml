package com.github.czyzby.uedi.test.lifecycle;

import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.stereotype.Initiated;
import com.github.czyzby.uedi.stereotype.Singleton;

public class InitiatedC implements Initiated, Singleton {
    private final InitiatedA constructorDependency;
    private Context context;
    private boolean notNull;
    private int order;

    public InitiatedC(final InitiatedA constructorDependency) {
        this.constructorDependency = constructorDependency;
    }

    @Override
    public int getInitiationOrder() {
        return 2;
    }

    @Override
    public void initiate() {
        notNull = context != null && constructorDependency != null;
        order = Counter.COUNTER.getAndIncrement();
    }

    public boolean wereDependenciesNotNull() {
        return notNull;
    }

    public boolean wasInitiated() {
        return order != 0; // Counter starts at 100.
    }

    public int getActualOrder() {
        return order;
    }
}
