package com.github.czyzby.autumn.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Basic annotation of components. These classes are scanned for, initiated and fully processed by the context
 * initializer. Be careful NOT to annotate classes that you add to context manually: this might result in two components
 * being mapped to the same class.
 *
 * @author MJ */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}
