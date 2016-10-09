package com.github.czyzby.uedi.error.circular;

import com.github.czyzby.uedi.stereotype.Singleton;

public class CircularErrorA implements Singleton {
    public CircularErrorA(final CircularErrorB circularErrorB) {
    }
}
