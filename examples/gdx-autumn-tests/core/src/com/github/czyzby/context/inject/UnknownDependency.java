package com.github.czyzby.context.inject;

import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.context.ContextInitializer;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** When you use {@link Inject} annotation on an unknown type (no component or provider in the context), a new instance
 * of this object will be created using reflection invoking the default no-arg constructor. This can be disabled with
 * {@link ContextInitializer#createMissingDependencies(boolean)} method - when set to false, unknown dependency
 * occurrence will cause an exception.
 *
 * <p>
 * However, this example is based on assumption that this setting is set to true and demonstrates such injection.
 *
 * @author MJ */
@Component
public class UnknownDependency {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(UnknownDependency.class);

    @Inject ObjectSet<String> set;

    /** Thanks to {@link Initiate} annotation, this method will be automatically invoked during context creation. */
    @Initiate
    public void onInit() {
        set.add("Set");
        LOGGER.info("{0} was injected and works as expected.", set);
    }
}
