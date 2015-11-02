package com.github.czyzby.kiwi.log;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.github.czyzby.kiwi.log.formatter.TextFormatter;
import com.github.czyzby.kiwi.log.impl.AsynchronousLogger;
import com.github.czyzby.kiwi.log.impl.DefaultLogger;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

/** Manages loggers. Default instance is accessible through {@link #INSTANCE} field and static methods.
 *
 * <p>
 * Even through services are {@link Disposable}, {@link #dispose()} method should be called only if you use asynchronous
 * loggers.
 *
 * @author MJ
 * @since 1.2 */
public class LoggerService implements Disposable {
    /** Main logger service, used by static methods. */
    public static final LoggerService INSTANCE = new LoggerService();

    private final ObjectMap<Class<?>, Logger> loggers = GdxMaps.newObjectMap();
    private final TextFormatter formatter = new TextFormatter();
    private volatile AsyncExecutor executor;

    // Settings:
    private boolean debugOn = true, infoOn = true, errorOn = true;
    private boolean logTime;
    private boolean useSimpleClassNames;
    private boolean useAsynchronousLoggers;

    public LoggerService() {
        // Services manage their own settings, we need to override default settings to turn on all logging levels.
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    /** @param forClass will be included in logs of the returned logger.
     * @return logger connected with the selected class, created with the current settings. This always returns the same
     *         logger instance for same class. */
    public Logger getLoggerForClass(final Class<?> forClass) {
        if (!loggers.containsKey(forClass)) {
            createLogger(forClass);
        }
        return loggers.get(forClass);
    }

    private void createLogger(final Class<?> forClass) {
        if (useAsynchronousLoggers) {
            loggers.put(forClass, new AsynchronousLogger(this, forClass));
        } else {
            loggers.put(forClass, new DefaultLogger(this, forClass));
        }
    }

    /** @return an instance of {@link AsyncExecutor}, used by asynchronous loggers. */
    public AsyncExecutor getExecutor() {
        if (executor == null) {
            synchronized (this) {
                if (executor == null) {
                    executor = new AsyncExecutor(1);
                }
            }
        }
        return executor;
    }

    /** @return formatter used to format messages with arguments. */
    public TextFormatter getFormatter() {
        return formatter;
    }

    /** @return true if debug messages are logged. */
    public boolean isDebugOn() {
        return debugOn;
    }

    /** @param debugOn if true, debug messages will be logged. */
    public void setDebugOn(final boolean debugOn) {
        this.debugOn = debugOn;
    }

    /** @return true if info messages are logged. */
    public boolean isInfoOn() {
        return infoOn;
    }

    /** @param infoOn if true, info messages will be logged. */
    public void setInfoOn(final boolean infoOn) {
        this.infoOn = infoOn;
    }

    /** @return true if error messages are logged. */
    public boolean isErrorOn() {
        return errorOn;
    }

    /** @param errorOn if true, error messages will be logged. */
    public void setErrorOn(final boolean errorOn) {
        this.errorOn = errorOn;
    }

    /** @return true if time is included in logs. */
    public boolean isLoggingTime() {
        return logTime;
    }

    /** @param logTime if true, current time will be included in logs. This does add some logging overhead. Defaults to
     *            false. */
    public void setLoggingTime(final boolean logTime) {
        this.logTime = logTime;
    }

    /** @param useSimpleClassNames if true, simple class names are used instead of full class names. Defaults to false.
     *            This value should be set BEFORE any loggers are returned, as it affects logger's creation. */
    public void setUseSimpleClassNames(final boolean useSimpleClassNames) {
        this.useSimpleClassNames = useSimpleClassNames;
    }

    /** @return true if simple class names are used instead of full class names. */
    public boolean isUsingSimpleClassNames() {
        return useSimpleClassNames;
    }

    /** @param useAsynchronousLoggers if true, asynchronous loggers will be used. While it delegates logging calls to a
     *            single thread, it has to create an instance of asynchronous task each time logging method is called
     *            and it has practically no gain on GWT. Use asynchronous loggers if you use multiple threads and
     *            regular logging causes delays or starvation. Defaults to false and should generally should not be set
     *            to true, unless justified. Note that this setting should be changed BEFORE any loggers are obtained,
     *            as it changes the way loggers are created. */
    public void setUseAsynchronousLoggers(final boolean useAsynchronousLoggers) {
        this.useAsynchronousLoggers = useAsynchronousLoggers;
    }

    @Override
    public void dispose() {
        Disposables.disposeOf(executor);
    }

    /** @param forClass its name will be included in logs.
     * @return an instance of {@link Logger} linked with the selected class. This method always returns the same logger
     *         instance for the same class. */
    public static Logger forClass(final Class<?> forClass) {
        return INSTANCE.getLoggerForClass(forClass);
    }

    /** @param debugOn if true, debug messages will be logged. */
    public static void debug(final boolean debugOn) {
        INSTANCE.setDebugOn(debugOn);
    }

    /** @param infoOn if true, info messages will be logged. */
    public static void info(final boolean infoOn) {
        INSTANCE.setInfoOn(infoOn);
    }

    /** @param errorOn if true, error messages will be logged. */
    public static void error(final boolean errorOn) {
        INSTANCE.setErrorOn(errorOn);
    }

    /** @param useAsynchronousLoggers if true, asynchronous loggers will be used. While it delegates logging calls to a
     *            single thread, it has to create an instance of asynchronous task each time logging method is called
     *            and it has practically no gain on GWT. Use asynchronous loggers if you use multiple threads and
     *            regular logging causes delays or starvation. Defaults to false and should generally should not be set
     *            to true, unless justified. Note that this setting should be changed BEFORE any loggers are obtained,
     *            as it changes the way loggers are created. */
    public static void asynchronous(final boolean useAsynchronousLoggers) {
        INSTANCE.setUseAsynchronousLoggers(useAsynchronousLoggers);
    }

    /** @param logTime if true, current time will be included in logs. This does add some logging overhead. Defaults to
     *            false. */
    public static void logTime(final boolean logTime) {
        INSTANCE.setLoggingTime(logTime);
    }

    /** @param useSimpleClassNames if true, simple class names are used instead of full class names. Defaults to false.
     *            This value should be set BEFORE any loggers are returned, as it affects logger's creation. */
    public static void simpleClassNames(final boolean useSimpleClassNames) {
        INSTANCE.setUseSimpleClassNames(useSimpleClassNames);
    }
}
