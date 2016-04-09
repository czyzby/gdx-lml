package com.github.czyzby;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.github.czyzby.autumn.context.ContextDestroyer;
import com.github.czyzby.autumn.context.ContextInitializer;
import com.github.czyzby.autumn.scanner.ClassScanner;
import com.github.czyzby.context.Root;
import com.github.czyzby.context.event.MyEvent;
import com.github.czyzby.context.manual.MyManualComponent;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerFactory;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.log.impl.DefaultLogger;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

/** Uses {@link ContextInitializer} to find and initiate components in the {@link com.github.czyzby.context} package.
 * Note that this class has barely any direct references to other components - only the minimum required for testing.
 * Using Autumn makes the application highly modular and allows to easily move the components to other projects.
 *
 * @author MJ */
public class Core extends ApplicationAdapter {
    /** Default window size. Size of the virtual stage viewport. */
    public static final int WIDTH = 800, HEIGHT = 600;

    /** Allows to scan for annotated classes. Platform-specific. */
    private final ClassScanner scanner;
    /** Stores message displayed on the screen. */
    private final StringBuilder message = new StringBuilder();
    /** Allows to dispose of Autumn components. */
    private ContextDestroyer destroyer;

    // Utility assets:
    private SpriteBatch batch;
    private Label label;

    public Core(final ClassScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void create() {
        // Changing logger factory:
        MyLogger.setLoggerFactory();

        // Preparing Autumn context:
        final ContextInitializer initializer = new ContextInitializer();

        // Manual component registration example - you can both let the Autumn find annotated classes for you or add
        // some components manually:
        final MyManualComponent manualComponent = new MyManualComponent();
        initializer.addComponent(manualComponent);

        // Registering platform-specific scanner. Starting to scan classes from Root:
        initializer.scan(Root.class, scanner);

        // Creating context:
        destroyer = initializer.initiate();

        // Getting a utility logger:
        final Logger logger = LoggerService.forClass(Core.class);

        // Testing events API (see com.github.czyzby.context.event package):
        logger.info("Posting 'myMessage'.");
        manualComponent.getMessageDispatcher().postMessage("myMessage");
        logger.info("Posting MyEvent.");
        manualComponent.getEventDispatcher().postEvent(new MyEvent("Core#create"));

        // Final note, adding input listener:
        logger.info("Autumn context is created. "
                + "Click to close the application and watch the console to see the destruction process.");
        addInputProcessor();

        // Getting utility assets injected into manualComponent by Autumn:
        batch = manualComponent.getAssets().getBatch();
        label = manualComponent.getLabel();
        label.setText(message);
    }

    private static void addInputProcessor() {
        // Closing the application as soon as it's clicked.
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
                GdxUtilities.exit();
                return true;
            }
        });
    }

    @Override
    public void render() {
        GdxUtilities.clearScreen();
        batch.begin();
        label.draw(batch, 1f);
        batch.end();
    }

    @Override
    public void dispose() {
        // ContextDestroyer implements Disposable interface and can be disposed of like any other heavy object or asset.
        // We're using null-safe Disposables Kiwi utility (because we can) to dispose of the destroyer:
        Disposables.disposeOf(destroyer);
    }

    /** Extends the default Kiwi logger. {@link MyLogger#info(String)} and {@link MyLogger#info(String, Object...))}
     * methods will output the logs to console and append them to {@link Core#message} to be displayed on the screen.
     * This is basically a utility for passing messages to {@link Core} instance without static field access.
     *
     * @author MJ */
    public static class MyLogger extends DefaultLogger {
        public MyLogger(final LoggerService service, final Class<?> forClass) {
            super(service, forClass);
        }

        @Override
        protected void logInfo(final String tag, final String message) {
            super.logInfo(tag, message);
            final Core core = (Core) Gdx.app.getApplicationListener();
            core.message.append(tag).append(": ").append(message).append('\n');
        }

        /** Changes the default Kiwi logger factory to provide {@link MyLogger} instances. After invoking this method,
         * all created loggers will not only flood the console on info logs, but also update the message displayed on
         * the main application screen. */
        public static void setLoggerFactory() {
            LoggerService.simpleClassNames(true); // We don't want to flood the logs with packages!
            LoggerService.INSTANCE.setFactory(new LoggerFactory() {
                @Override
                public Logger newLogger(final LoggerService service, final Class<?> forClass) {
                    return new MyLogger(service, forClass);
                }
            });
        }
    }
}
