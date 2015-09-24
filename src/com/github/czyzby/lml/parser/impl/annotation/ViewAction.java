package com.github.czyzby.lml.parser.impl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Allows to invoke an {@link com.github.czyzby.lml.parser.impl.dto.ActionContainer}'s action with a custom parameter
 * instead of its name. This allows containers to be refactored without breaking the templates.
 *
 * @author MJ */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewAction {
    /** @return IDs of the method, as it should be referenced in the LML views. */
    String[]value();
}
