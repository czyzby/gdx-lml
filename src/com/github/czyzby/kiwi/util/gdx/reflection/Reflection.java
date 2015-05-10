package com.github.czyzby.kiwi.util.gdx.reflection;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.Method;

/** LibGDX reflection utilities.
 *
 * @author MJ */
public class Reflection {
	/** Utility method that allows to extract annotation with one method. Returns null if annotation is not
	 * present.
	 *
	 * @param method method that might be annotated.
	 * @param annotationType class of the annotation.
	 * @return an instance of the annotation if the method is annotated or null. */
	public static <Type extends Annotation> Type getAnnotation(final Method method,
			final Class<Type> annotationType) {
		if (method.isAnnotationPresent(annotationType)) {
			return method.getDeclaredAnnotation(annotationType).getAnnotation(annotationType);
		}
		return null;
	}
}
