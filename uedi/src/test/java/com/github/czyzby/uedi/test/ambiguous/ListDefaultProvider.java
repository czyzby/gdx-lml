package com.github.czyzby.uedi.test.ambiguous;

import java.lang.reflect.Member;
import java.util.ArrayList;

import com.github.czyzby.uedi.stereotype.Default;
import com.github.czyzby.uedi.stereotype.Provider;

public class ListDefaultProvider<E> implements Default, Provider<ArrayList<E>> {
    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends ArrayList<E>> getType() {
        return (Class<ArrayList<E>>) (Object) ArrayList.class;
    }

    @Override
    public ArrayList<E> provide(final Object target, final Member member) {
        return new ArrayList<E>();
    }
}
