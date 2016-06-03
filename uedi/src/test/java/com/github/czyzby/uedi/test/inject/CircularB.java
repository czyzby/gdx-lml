package com.github.czyzby.uedi.test.inject;

import com.github.czyzby.uedi.stereotype.Singleton;

public class CircularB implements Singleton {
    private CircularA a;

    public CircularA getA() {
        return a;
    }
}
