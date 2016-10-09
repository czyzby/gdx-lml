package com.github.czyzby.uedi.test.lifecycle;

import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.stereotype.Destructible;
import com.github.czyzby.uedi.stereotype.Singleton;

public class DestroyedA implements Destructible, Singleton {
    private final InitiatedB constructorDependency;
    private Context context;
    private boolean notNull;
    private int order;

    public DestroyedA(final InitiatedB constructorDependency) {
        this.constructorDependency = constructorDependency;
    }

    @Override
    public int getDestructionOrder() {
        return 0;
    }

    @Override
    public void destroy() {
        notNull = context != null && constructorDependency != null;
        order = Counter.COUNTER.getAndIncrement();
    }

    public boolean wereDependenciesNotNull() {
        return notNull;
    }

    public boolean wasDestroyed() {
        return order != 0; // Counter starts at 100.
    }

    public int getActualOrder() {
        return order;
    }

}
