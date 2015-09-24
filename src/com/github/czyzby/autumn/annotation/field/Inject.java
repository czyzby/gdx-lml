package com.github.czyzby.autumn.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** If a field is annotated with Inject and the object is constructed with context manager, the field value will be
 * extracted from context.
 *
 * @author MJ */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    /** Used to resolve conflicts if multiple components share an interface or abstract class - selected class will be
     * the chosen implementation. If there is only one object of the variable's type in the context, this setting is
     * unnecessary. Ignored if lazy class is given. */
    Class<?>value() default void.class;

    /** If chosen, Lazy providing wrapped instance of chosen class will be injected instead of directly injecting the
     * component's instance. The wrapped object will be provided (or even constructed) by the context manager on the
     * first get() call - including components referenced by the lazy object that were not initiated before. */
    Class<?>lazy() default void.class;

    /** If true and lazy class is chosen, the constructed Lazy object will be concurrent. */
    boolean concurrentLazy() default false;

    /** If true, a new instance of the variable's class will be injected rather than the one stored in context (if any
     * is actually present). This instance will be unique to the object that has it injected, but it is still partially
     * managed by the context (initiated, disposed, destroyed, but cannot be injected). */
    boolean newInstance() default false;
}
