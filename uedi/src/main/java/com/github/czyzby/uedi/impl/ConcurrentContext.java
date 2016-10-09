package com.github.czyzby.uedi.impl;

import com.github.czyzby.uedi.scanner.ClassScanner;

/** This is a mock-up class and does NOT actually provide thread-safe API on LibGDX platform. It was created for
 * compatibility with uedi-core.
 *
 * @author MJ */
@Deprecated
public class ConcurrentContext extends DefaultContext {
    /** @param classScanner will be used to scan the context. */
    public ConcurrentContext(final ClassScanner classScanner) {
        super(classScanner);
    }
}
