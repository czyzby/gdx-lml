package com.github.czyzby.uedi.test.lifecycle;

import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.stereotype.Initiated;
import com.github.czyzby.uedi.stereotype.Singleton;
import com.github.czyzby.uedi.test.inject.Injected;

public class InitiatedB implements Initiated, Singleton {
    private final Injected constructorDependency;
    private Context context;
    private boolean notNull;
    private int order;

    public InitiatedB(final Injected constructorDependency) {
        this.constructorDependency = constructorDependency;
    }

    @Override
    public int getInitiationOrder() {
        return 1;
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
