package com.github.czyzby.autumn.annotation.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Context manager will scan classes for Component annotation and put them in the context. Components' fields and
 * methods can contain additional annotations that will be processed by the registered processors.
 *
 * @author MJ */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    /** If set to true, component object will be created and put into context, but it will be fully initiated (as in
     * fields' and methods' annotations will be processed) on first injection or extraction from context rather than on
     * start-up. */
    boolean lazy() default false;

    /** If set to false, component will be initiated, it's Inject fields will be injected and OnInit/OnDestroy methods
     * will be invoked, but it will not be put into the context. Can be useful for configurations. Such components
     * generally should not be injected as behavior is undetermined and it might lead to memory leaks. */
    boolean keepInContext() default true;
}
