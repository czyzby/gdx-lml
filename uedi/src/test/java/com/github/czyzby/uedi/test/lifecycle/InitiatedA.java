package com.github.czyzby.uedi.test.lifecycle;

import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.stereotype.Initiated;
import com.github.czyzby.uedi.stereotype.Singleton;

public class InitiatedA implements Initiated, Singleton {
    private final InitiatedB constructorDependency;
    private Context context;
    private boolean notNull;
    private int order;

    public InitiatedA(final InitiatedB constructorDependency) {
        this.constructorDependency = constructorDependency;
    }

    @Override
    public int getInitiationOrder() {
        return 0;
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
