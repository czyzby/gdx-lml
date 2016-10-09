package com.github.czyzby.uedi.test.lifecycle;

import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.stereotype.Destructible;
import com.github.czyzby.uedi.stereotype.Singleton;

public class DestroyedB implements Destructible, Singleton {
    private final InitiatedB constructorDependency;
    private Context context;
    private boolean notNull;
    private int order;

    public DestroyedB(final InitiatedB constructorDependency) {
        this.constructorDependency = constructorDependency;
    }

    @Override
    public int getDestructionOrder() {
        return 1;
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

    public Context getInjectedDependency() {
        return context;
    }
}
