package com.github.czyzby.uedi.test.inject;

import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.stereotype.Singleton;

public class Injector implements Singleton {
    public transient Ignored transientValue;
    public Ignored notNull = Ignored.TEST_INSTANCE;
    public static Ignored staticValue = null;
    public long primitive = 42;

    public Context context; // via Context (always available to inject)
    public Injected injected; // via Injected
    public Built built; // via InjectFactory
    public String property; // via InjectProperty
    public Provided provided; // via InjectProvider
}
