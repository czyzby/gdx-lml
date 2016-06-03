package com.github.czyzby.lml.uedi.logger;

import java.lang.reflect.Member;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.uedi.reflection.impl.ConstructorMember;
import com.github.czyzby.uedi.stereotype.Provider;

/** Provides {@link Logger} instances aware of the class of component that they are injected into. Supports both field
 * and constructor injection. Logging utility.
 *
 * @author MJ */
public class LoggerProvider implements Provider<Logger> {
    @Override
    public Class<? extends Logger> getType() {
        return Logger.class;
    }

    @Override
    public Logger provide(final Object target, final Member member) {
        if (target != null) {
            return getLogger(target.getClass());
        } else if (member instanceof ConstructorMember) { // Extracting component class from constructor:
            final ConstructorMember constructor = (ConstructorMember) member;
            return getLogger(constructor.getConstructor().getDeclaringClass());
        }
        return getGlobalLogger();
    }

    /** @return {@link Logger} assigned to the current application listener implementation. Can be treated as global
     *         logger. */
    public Logger getGlobalLogger() {
        return getLogger(Gdx.app.getApplicationListener().getClass());
    }

    /** @param forClass will be included in the logs.
     * @return a {@link Logger} instance unique to the selected class. */
    public Logger getLogger(final Class<?> forClass) {
        return LoggerService.forClass(forClass);
    }
}
