package com.github.czyzby.context.initiate;

import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Destroy;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;

/** {@link Initiate} and {@link Destroy} are annotations that allow you to control the flow of application's creation
 * and destruction. Both feature a priority setting, which is honored not only in class scope, but in the whole
 * application scope - if two classes contain method annotated with {@link Initiate}, you can be 100% sure that the
 * method with higher priority will be invoked first. Both {@link Initiate}- and {@link Destroy}-annotated methods can
 * consume any parameters that will be injected upon invocation. This class demonstrates use of these annotations.
 *
 * @author MJ */
@Component
public class Initiator {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(Initiator.class);

    @Initiate(priority = 42)
    void first() {
        LOGGER.info("I should be initiated first. I'm {0}.", this);
    }

    @Initiate(priority = -42)
    void last() {
        LOGGER.info("I should be initiated last.");
    }

    @Destroy(priority = 10)
    void destroy(final Destructor destructor) {
        LOGGER.info("I should be destroyed after {0}.", destructor);
    }

    @Override
    public String toString() {
        return "(Initiator[hashCode=" + hashCode() + "])";
    }
}
