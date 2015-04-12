package com.github.czyzby.autumn.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** When a Disposable variable is annotated with Dispose, it will be disposed of upon destruction of the object
 * in the context. There is no guarantee which event will be invoked first - fields disposing or methods with
 * Destroy annotation - so these two should not collide or depend on each other.
 *
 * @author MJ */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Dispose {
}
