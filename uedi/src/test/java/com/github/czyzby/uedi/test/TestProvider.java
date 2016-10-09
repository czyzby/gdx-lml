package com.github.czyzby.uedi.test;

import java.lang.reflect.Member;

import com.github.czyzby.uedi.Context;
import com.github.czyzby.uedi.stereotype.Provider;

public class TestProvider implements Provider<TestComponent> {
    private Context context;

    @Override
    public Class<? extends TestComponent> getType() {
        return TestComponent.class;
    }

    @Override
    public TestComponent provide(final Object target, final Member member) {
        return context.create(TestComponent.class);
    }
}
