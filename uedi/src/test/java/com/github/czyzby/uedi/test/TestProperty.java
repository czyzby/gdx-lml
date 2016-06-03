package com.github.czyzby.uedi.test;

import com.github.czyzby.uedi.stereotype.Property;

public class TestProperty implements Property {
    public static final String ID = "someProperty", VALUE = "initialValue";
    private String value = VALUE;

    @Override
    public String getKey() {
        return ID;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(final String value) {
        final String previous = this.value;
        this.value = value;
        return previous;
    }
}
