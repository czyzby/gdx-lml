package com.github.czyzby.context.inject;

import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** This is an immutable component with a constructor dependency: {@link SomeComponent} will be injected into
 * {@link #ConstructorDepedency(SomeComponent)} to create an instance of this class. Note that circular constructor
 * dependencies are NOT supported. Use {@link Inject} annotation instead.
 *
 * @author MJ */
@Component
public class ConstructorDepedency {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(ConstructorDepedency.class);

    private final SomeComponent someComponent;

    /** @param someComponent will be injected to create this object. */
    public ConstructorDepedency(final SomeComponent someComponent) {
        this.someComponent = someComponent;
    }

    /** Thanks to {@link Initiate} annotation, this method will be automatically invoked during context creation. */
    @Initiate
    public void onInit() {
        LOGGER.info("{0} was injected in my constructor.", someComponent);
    }
}
