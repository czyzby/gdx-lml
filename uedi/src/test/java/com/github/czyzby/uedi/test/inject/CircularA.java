package com.github.czyzby.uedi.test.inject;

import com.github.czyzby.uedi.stereotype.Singleton;

public class CircularA implements Singleton {
    CircularB b;

    public CircularB getB() {
        return b;
    }
}
