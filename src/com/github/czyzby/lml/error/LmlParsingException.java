package com.github.czyzby.lml.error;

import com.github.czyzby.lml.parser.LmlParser;

/** Thrown when LML file is considered invalid.
 *
 * @author MJ */
public class LmlParsingException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_ERROR_MESSAGE = "Unable to parse LML file. Look for syntax errors.";
	private static final String LINE_MARKER_MESSAGE = " Error occured near line ";
	private static final String LINE_MARKER_MESSAGE_ERROR =
			" of the original file.\n\tNote that if a macro was called or ended in this line (even one of the default ones), the real problematic line may vary a bit from the given value due to how parser works. If the given line appears to be valid, recheck the called macro and its content between tags.";

	public LmlParsingException(final String message) {
		super(message == null ? DEFAULT_ERROR_MESSAGE : message);
	}

	public LmlParsingException(final LmlParser parser) {
		super(prepareMessage(DEFAULT_ERROR_MESSAGE, parser));
	}

	public LmlParsingException(final String message, final LmlParser parser) {
		super(prepareMessage(message, parser));
	}

	private static String prepareMessage(final String message, final LmlParser parser) {
		return message + LINE_MARKER_MESSAGE + parser.getCurrentlyParsedLine() + LINE_MARKER_MESSAGE_ERROR;
	}

	public LmlParsingException(final LmlParser parser, final Throwable cause) {
		super(prepareMessage(DEFAULT_ERROR_MESSAGE, parser), cause);
	}

	public LmlParsingException(final String message, final LmlParser parser, final Throwable cause) {
		super(prepareMessage(message, parser), cause);
	}
}
