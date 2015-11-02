package com.github.czyzby.kiwi.log;

/** Common interface for Kiwi loggers.
 *
 * @author MJ
 * @since 1.2
 * @see com.github.czyzby.kiwi.log.impl.DefaultLogger */
public interface Logger {
    /** @return true if debug messages are logged. */
    boolean isDebugOn();

    /** @return true if info messages are logged. */
    boolean isInfoOn();

    /** @return true if error messages are logged. */
    boolean isErrorOn();

    /** @param message will be logged on debug level. */
    void debug(String message);

    /** @param message will be logged on debug level. Can contain indexed placeholders.
     * @param arguments will replace placeholders.
     * @see com.github.czyzby.kiwi.log.formatter.TextFormatter */
    void debug(String message, Object... arguments);

    /** @param exception cause of the log. Will be logged.
     * @param message will be logged on debug level. */
    void debug(Throwable exception, String message);

    /** @param exception cause of the log. Will be logged.
     * @param message will be logged on debug level. Can contain indexed placeholders.
     * @param arguments will replace placeholders.
     * @see com.github.czyzby.kiwi.log.formatter.TextFormatter */
    void debug(Throwable exception, String message, Object... arguments);

    /** @param message will be logged on info level. */
    void info(String message);

    /** @param message will be logged on info level. Can contain indexed placeholders.
     * @param arguments will replace placeholders.
     * @see com.github.czyzby.kiwi.log.formatter.TextFormatter */
    void info(String message, Object... arguments);

    /** @param exception cause of the log. Will be logged.
     * @param message will be logged on info level. */
    void info(Throwable exception, String message);

    /** @param exception cause of the log. Will be logged.
     * @param message will be logged on info level. Can contain indexed placeholders.
     * @param arguments will replace placeholders.
     * @see com.github.czyzby.kiwi.log.formatter.TextFormatter */
    void info(Throwable exception, String message, Object... arguments);

    /** @param message will be logged on error level. */
    void error(String message);

    /** @param message will be logged on error level. Can contain indexed placeholders.
     * @param arguments will replace placeholders.
     * @see com.github.czyzby.kiwi.log.formatter.TextFormatter */
    void error(String message, Object... arguments);

    /** @param exception cause of the log. Will be logged.
     * @param message will be logged on error level. */
    void error(Throwable exception, String message);

    /** @param exception cause of the log. Will be logged.
     * @param message will be logged on error level. Can contain indexed placeholders.
     * @param arguments will replace placeholders.
     * @see com.github.czyzby.kiwi.log.formatter.TextFormatter */
    void error(Throwable exception, String message, Object... arguments);
}
