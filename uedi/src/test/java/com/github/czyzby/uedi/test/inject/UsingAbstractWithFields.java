package com.github.czyzby.uedi.test.inject;

import com.github.czyzby.uedi.stereotype.Singleton;

public class UsingAbstractWithFields extends AbstractWithFields implements Singleton {
    private Injected childField;

    public Injected getChildField() {
        return childField;
    }
}
