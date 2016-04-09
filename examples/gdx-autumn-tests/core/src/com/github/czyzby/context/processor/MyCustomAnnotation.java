package com.github.czyzby.context.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** This is an example annotation. Autumn allows you to write and easily register custom annotations and annotation
 * processors - see {@link MyCustomProcessor} class to see handling of this annotation.
 *
 * @author MJ */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
public @interface MyCustomAnnotation {
    /** @return an example value stored in the annotation. */
    String value() default "default";

    /** @return an example value stored in the annotation. */
    int id() default 0;
}
