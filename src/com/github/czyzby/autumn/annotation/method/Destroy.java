package com.github.czyzby.autumn.annotation.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** If contained by an object managed by the context, will be invoked upon context disposing or component
 * destruction. Can be used to destroy heavy objects.
 *
 * @author MJ */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Destroy {
	/** Defines the priority of destruction method invocation. When multiple objects are destroyed at once (for
	 * example: on context destruction), methods with higher priority will be invoked first - regardless of
	 * original component order. Negative priorities are accepted. Even if only one component is destroyed,
	 * its methods are also affected by the order of priority. */
	int priority() default 0;
}
