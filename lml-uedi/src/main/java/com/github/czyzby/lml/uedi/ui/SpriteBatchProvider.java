package com.github.czyzby.lml.uedi.ui;

import java.lang.reflect.Member;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.uedi.stereotype.Destructible;

/** Manages and provides a single {@link SpriteBatch} instance.
 *
 * @author MJ */
public class SpriteBatchProvider extends BatchProvider implements Destructible {
    private final SpriteBatch batch = new SpriteBatch();

    @Override
    public Class<? extends Batch> getType() {
        return SpriteBatch.class;
    }

    @Override
    public Batch provide(final Object target, final Member member) {
        return batch;
    }

    @Override
    public Batch getBatch() {
        return batch;
    }

    /** @return application's only {@link SpriteBatch} instance. */
    public SpriteBatch getSpriteBatch() {
        return batch;
    }

    @Override
    public int getDestructionOrder() {
        return 0;
    }

    @Override
    public void destroy() {
        Disposables.gracefullyDisposeOf(batch);
    }
}
