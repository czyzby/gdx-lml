package com.github.czyzby.context.asset;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Destroy;
import com.github.czyzby.autumn.annotation.Dispose;
import com.github.czyzby.autumn.context.ContextDestroyer;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

/** An example component demonstrating Autumn handling of heavy objects.
 *
 * @author MJ */
@Component
public class MyAssetComponent {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(MyAssetComponent.class);

    /** Marking a field with {@link Dispose} annotation results in disposing field value upon
     * {@link ContextDestroyer#dispose()} call. In other words, the field value will automatically disposed. */
    @Dispose private final SpriteBatch batch = new SpriteBatch();
    @Dispose private final MyDisposable disposable = new MyDisposable();

    private final BitmapFont font = new BitmapFont();

    // You can use @Dispose annotation to automatically dispose values that implement Disposable interface. But
    // sometimes this is not the case - some heavy assets might need manual destruction, but do not implement it.
    // Instead of using @Dispose, you can annotate a method with @Destroy - this method will also be invoked when the
    // context is destroyed. Rule of thumb: use @Dispose where possible; use @Destroy if you need more advanced
    // disposing methods or you need control over the order of disposing.
    @Destroy
    void destroy() {
        LOGGER.info("Disposing assets.");
        Disposables.disposeOf(font);
    }
    // Note that @Dispose and @Destroy are handled by two separate processors and do not depend on each other. @Destroy
    // priority order is not honored by @Dispose annotation. There is no way of telling which will finish executing
    // first. If your assets disposing depends on any other action, use @Destroy annotation with a valid priority set.
    // See 'initiate' package for more info about @Initiate and @Destroy annotations.

    /** @return application's only {@link SpriteBatch} instance. */
    public SpriteBatch getBatch() {
        return batch;
    }

    /** @return application's main font. */
    public BitmapFont getFont() {
        return font;
    }
}
