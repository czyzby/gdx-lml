package com.github.czyzby.context.asset;

import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.autumn.annotation.Dispose;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** An example of {@link Disposable} object. Proves that fields annotated with {@link Dispose} are actually disposed of.
 *
 * @author MJ */
public class MyDisposable implements Disposable {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(MyDisposable.class);

    @Override
    public void dispose() {
        LOGGER.info("Disposing MyDisposable.");
    }
}
