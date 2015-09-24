package com.github.czyzby.lml.error;

import com.github.czyzby.lml.parser.LmlParser;

/** Thrown when LML file is considered invalid.
 *
 * @author MJ */
public class LmlParsingException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_MESSAGE = "Unable to parse LML file. Look for syntax errors.";
    private static final String LINE_MARKER_MESSAGE = " Error occured near line ";
    private static final String FILE_MARKER_MESSAGE = " of the original file: ";
    private static final String ERROR_PROMPT = ".\n\tNote that if a macro was called or has ended in this line (even one of the default ones), the real problematic line may not be exact due to how parser works. If the given line appears to be valid, please recheck the called macro and its content between tags.\n\tOriginal exception class and message: ";

    public LmlParsingException(final String message) {
        super(prepareMessage(message == null ? DEFAULT_ERROR_MESSAGE : message, null, null));
    }

    public LmlParsingException(final LmlParser parser) {
        super(prepareMessage(DEFAULT_ERROR_MESSAGE, parser, null));
    }

    public LmlParsingException(final String message, final LmlParser parser) {
        super(prepareMessage(message, parser, null));
    }

    private static String prepareMessage(final String message, final LmlParser parser, final Throwable cause) {
        return prepareMessage(message, parser == null ? "unknown" : parser.getLastParsedDocumentName(),
                parser == null ? -1 : parser.getCurrentlyParsedLine(), cause);
    }

    private static String prepareMessage(final String message, final String documentName, final int line,
            final Throwable cause) {
        return message + LINE_MARKER_MESSAGE + line + FILE_MARKER_MESSAGE + documentName + ERROR_PROMPT
                + getCauseMessage(cause);
    }

    private static String getCauseMessage(final Throwable cause) {
        if (cause == null) {
            return "none";
        }
        return cause.getClass() + ", " + cause.getMessage();
    }

    public LmlParsingException(final LmlParser parser, final Throwable cause) {
        super(prepareMessage(DEFAULT_ERROR_MESSAGE, parser, cause), cause);
    }

    public LmlParsingException(final String message, final LmlParser parser, final Throwable cause) {
        super(prepareMessage(message, parser, cause), cause);
    }
}