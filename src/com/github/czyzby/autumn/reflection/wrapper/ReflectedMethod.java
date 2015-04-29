package com.github.czyzby.autumn.reflection.wrapper;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.ReflectionException;

/** Interface for utility wrappers of reflected classes' methods.
 *
 * @author MJ */
public interface ReflectedMethod {
	/** @return wrapped class that possesses this method. */
	ReflectedClass getReflectedClass();

	/** @return ordered parameter types of the wrapped method. */
	Class<?>[] getParameterTypes();

	/** @param annotationType annotation class.
	 * @return true if the method is annotated with selected annotation type. */
	<Type extends Annotation> boolean isAnnotatedWith(Class<Type> annotationType);

	/** @param annotationType annotation class.
	 * @return annotation instance of the selected type or null. */
	<Type extends Annotation> Type getAnnotation(Class<Type> annotationType);

	/** @return all annotations present on this method. */
	Annotation[] getAnnotations();

	/** @param owner object of type that contains this method.
	 * @param parameters will be passed to the actual method invocation.
	 * @return result of the method invocation. Null if method return type is void.
	 * @throws ReflectionException if unable to execute method invocation. */
	Object invoke(Object owner, Object... parameters) throws ReflectionException;
}
