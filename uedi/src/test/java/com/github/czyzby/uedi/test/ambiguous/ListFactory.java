package com.github.czyzby.uedi.test.ambiguous;

import java.util.LinkedList;
import java.util.List;

import com.github.czyzby.uedi.stereotype.Factory;

public class ListFactory implements Factory {
    public <E> List<E> linkedList() {
        return new LinkedList<E>();
    }
}
