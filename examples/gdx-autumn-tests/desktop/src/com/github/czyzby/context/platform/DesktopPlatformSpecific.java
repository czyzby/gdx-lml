package com.github.czyzby.context.platform;

import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** Extension of {@link PlatformSpecific} for desktop platform. This object will be injected any time a
 * {@link PlatformSpecific} instance is requested in a desktop application.
 *
 * @author MJ
 * @see PlatformSpecific */
@Component
public class DesktopPlatformSpecific extends PlatformSpecific {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(DesktopPlatformSpecific.class);

    // Note that create() was not annotated with @Initiate in this class, but in super type - PlatformSpecific. It will
    // still be invoked as expected. This allows you to focus on platform-specific logic and keep Autumn annotations in
    // the abstract class.
    @Override
    public void create() {
        LOGGER.info("Initiating {0}.", this);
    }

    @Override
    public String toString() {
        return "(DesktopPlatformSpecific[hashCode=" + hashCode() + "])";
    }
}
