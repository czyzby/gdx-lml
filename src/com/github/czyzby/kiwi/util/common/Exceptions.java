package com.github.czyzby.kiwi.util.common;

/** Common exception utilities.
 *
 * @author MJ */
public class Exceptions {
    private Exceptions() {
    }

    /** Allows to turn empty catch blocks into somewhat documented exception ignoring blocks.
     *
     * @param exception will be ignored. */
    public static void ignore(final Throwable exception) {
    }

    /** Allows to turn empty catch blocks into somewhat documented exception ignoring blocks.
     *
     * @param reason optional reason why the exception was ignored for code clarity.
     * @param exception will be ignored. */
    public static void ignore(final String reason, final Throwable exception) {
    }

    /** @param exception will be converted to {@link RuntimeException}.
     * @return a new runtime exception. */
    public static RuntimeException toRuntimeException(final Throwable exception) {
        return new RuntimeException(exception);
    }

    /** @param exception will be converted to {@link RuntimeException}.
     * @param message optional new exception message.
     * @return a new runtime exception. */
    public static RuntimeException toRuntimeException(final String message, final Throwable exception) {
        return new RuntimeException(message, exception);
    }

    /** @param exception will be converted to {@link RuntimeException} and thrown. */
    public static void throwRuntimeException(final Throwable exception) {
        throw toRuntimeException(exception);
    }

    /** @param exception will be converted to {@link RuntimeException} and thrown.
     * @param message optional new exception message. */
    public static void throwRuntimeException(final String message, final Throwable exception) {
        throw toRuntimeException(message, exception);
    }
}
