package com.github.czyzby.context.platform;

import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** An example of platform-specific injection.
 *
 * @author MJ
 * @see PlatformSpecific */
@Component
public class PlatformSpecificDependency {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(PlatformSpecificDependency.class);

    @Inject PlatformSpecific platformSpecific;

    /** Thanks to {@link Initiate} annotation, this method will be automatically invoked during context creation. */
    @Initiate
    public void onInit() {
        LOGGER.info("Got platform-specific: {0}.", platformSpecific);
    }
}
