package com.github.czyzby.autumn.annotation.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Defines classes that are used to process other annotations. Meta components are also scanned for and need to be
 * reflected. Meta components - such as custom annotation processors - are also available for injection as regular
 * components, but they cannot have injected fields or initiated/destroyed methods themselves - they are processed
 * before all other components (even allowing some components to actually be scanned) and they have to be fully
 * operational before the context is even built.
 *
 * @author MJ */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaComponent {
}
