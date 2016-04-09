package com.github.czyzby.context.provider;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** This class demonstrates use of providers. {@link #injectedArray} will be created using {@link MyArrayProvider}.
 *
 * @author MJ */
@Component
public class MyProviderUser {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(MyProviderUser.class);

    @Inject Array<String> injectedArray;

    /** Thanks to {@link Initiate} annotation, this method will be automatically invoked during context creation. */
    @Initiate
    public void onInit() {
        injectedArray.add("D");
        LOGGER.info("Array {0} was injected and works as expected.", injectedArray);
    }
}
