package com.github.czyzby.autumn.mvc.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Allows to specify a view actor inside a {@link com.github.czyzby.autumn.mvc.stereotype.View}- or
 * {@link com.github.czyzby.autumn.mvc.stereotype.ViewDialog}-annotated controller class. Actor is injected to
 * the field each time the view is built and the actor with the selected ID is present. Note that multiple
 * actor IDs can be passed to the annotation - if the field is an {@link com.badlogic.gdx.utils.Array},
 * {@link com.badlogic.gdx.utils.ObjectSet} or {@link com.badlogic.gdx.utils.ObjectMap}, actors will be stored
 * in a collection and injected into the field together. If you use an Array, the injected actors' order will
 * match the order of actor IDs that you pass into the annotation. If you use an ObjectMap, it has to use
 * Strings as map keys - actors will be put into the map with the key matching their ID.
 *
 * @author MJ */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewActor {
	/** @return ID of the actor(s) that should be injected. Has to be specified with "id" tag attribute in LML
	 *         view template. */
	String[] value() default {};
}