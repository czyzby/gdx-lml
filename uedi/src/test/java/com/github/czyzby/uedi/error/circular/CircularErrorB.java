package com.github.czyzby.uedi.error.circular;

import com.github.czyzby.uedi.stereotype.Singleton;

public class CircularErrorB implements Singleton {
    public CircularErrorB(final CircularErrorA circularErrorA) {
    }
}
