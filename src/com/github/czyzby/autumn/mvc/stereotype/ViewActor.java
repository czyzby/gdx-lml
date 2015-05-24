package com.github.czyzby.autumn.mvc.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Allows to specify a view actor inside a {@link com.github.czyzby.autumn.mvc.stereotype.View}- or
 * {@link com.github.czyzby.autumn.mvc.stereotype.ViewDialog}-annotated controller class. Actor is injected to
 * the field each time the view is built and the actor with the selected ID is present.
 *
 * @author MJ */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewActor {
	/** @return ID of the actor that should be injected. Has to be specified with id tag attribute in LML view. */
	String value() default "";
}