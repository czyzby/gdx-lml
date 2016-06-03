package com.github.czyzby.lml.uedi.ui;

import java.lang.reflect.Member;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.czyzby.uedi.stereotype.Provider;

/** Provides {@link Batch} instance.
 *
 * @author MJ */
public class BatchProvider implements Provider<Batch> {
    private final BatchProvider delegate;

    protected BatchProvider() {
        delegate = null;
    }

    /** @param delegate will be used to provide {@link Batch} instance. Workaround to map an interface in the
     *            context. */
    public BatchProvider(final BatchProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public Class<? extends Batch> getType() {
        return Batch.class;
    }

    @Override
    public Batch provide(final Object target, final Member member) {
        return delegate.provide(target, member);
    }

    /** @return application's only {@link Batch} instance. */
    public Batch getBatch() {
        return delegate.getBatch();
    }
}
