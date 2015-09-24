package com.github.czyzby.autumn.annotation.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Configuration is a specialized Component that will be initiated and immediately destroyed (invoking processing of
 * all of its fields and methods immediately), allowing to configure injected components or the application in general.
 *
 * @author MJ */
@Component(keepInContext = false)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {
}
