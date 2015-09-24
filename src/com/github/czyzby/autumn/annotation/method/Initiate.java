package com.github.czyzby.autumn.annotation.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** If contained by an object constructed by the context, will be invoked upon object creation after fields processing.
 * Should replace context-dependent logic in constructors.
 *
 * @author MJ */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Initiate {
    /** Defines the priority of initiation method invocation. When multiple objects are initiated at once (for example:
     * on context creation), methods with higher priority will be invoked first, allowing to prepare crucial assets and
     * variables - regardless of the original component order. Negative priorities are accepted. Even if only one
     * component is initiated, method invocations are also affected by the priority order. Note that by using parameters
     * of components with Initiate-annotated methods and lower priority will most likely result in access to not fully
     * initiated components (which might be a desired thing, but still). */
    int priority() default 0;
}
