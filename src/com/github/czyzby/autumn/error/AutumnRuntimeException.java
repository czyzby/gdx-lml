package com.github.czyzby.autumn.error;

import com.badlogic.gdx.utils.GdxRuntimeException;

/** Thrown when unexpected exceptions occur, most probably due to programming mistakes (reflection exceptions,
 * illegal states, etc).
 *
 * @author MJ */
public class AutumnRuntimeException extends GdxRuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_ERROR_MESSAGE = "Unexpected Autumn exception occured.";

	public AutumnRuntimeException() {
		this(DEFAULT_ERROR_MESSAGE);
	}

	public AutumnRuntimeException(final String message) {
		super(message);
	}

	public AutumnRuntimeException(final Throwable cause) {
		super(DEFAULT_ERROR_MESSAGE, cause);
	}

	public AutumnRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
