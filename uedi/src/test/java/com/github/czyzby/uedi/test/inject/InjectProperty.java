package com.github.czyzby.uedi.test.inject;

import com.github.czyzby.uedi.stereotype.Property;

public class InjectProperty implements Property {
    public static final String VALUE = "value";

    @Override
    public String getKey() {
        return "property";
    }

    @Override
    public String getValue() {
        return VALUE;
    }

    @Override
    public String setValue(final String value) {
        return VALUE; // Not implemented.
    }
}
