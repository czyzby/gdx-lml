package com.github.czyzby.uedi.test.inject;

import java.lang.reflect.Member;

import com.github.czyzby.uedi.stereotype.Provider;

public class InjectProvider implements Provider<Provided> {
    @Override
    public Class<? extends Provided> getType() {
        return Provided.class;
    }

    @Override
    public Provided provide(final Object target, final Member member) {
        return new Provided();
    }
}
