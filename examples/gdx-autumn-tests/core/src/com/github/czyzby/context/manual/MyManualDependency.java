package com.github.czyzby.context.manual;

import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** This object injects a {@link MyManualComponent} instance to prove that manually registered objects can be injected.
 *
 * @author MJ */
@Component
public class MyManualDependency {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(MyManualDependency.class);

    @Inject MyManualComponent component;

    /** Thanks to {@link Initiate} annotation, this method will be automatically invoked during context creation. */
    @Initiate
    public void onInit() {
        LOGGER.info("Manually registered {0} was injected.", component);
    }
}
