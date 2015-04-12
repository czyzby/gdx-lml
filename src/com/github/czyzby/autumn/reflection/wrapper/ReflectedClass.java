package com.github.czyzby.autumn.reflection.wrapper;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.ReflectionException;

/** Interface for utility wrapper of a reflected class.
 *
 * @author MJ */
public interface ReflectedClass {
	/** @return actual reflected class. */
	Class<?> getReflectedClass();

	/** @return wrapped superclass or null. */
	ReflectedClass getSuperclass();

	/** @return classes of implemented interfaces. */
	Class<?>[] getInterfaces();

	/** @param annotationType annotation class.
	 * @return true if the class is annotated with selected annotation type. */
	<Type extends Annotation> boolean isAnnotatedWith(Class<Type> annotationType);

	/** @param annotationType annotation class.
	 * @return annotation instance of the selected type or null. */
	<Type extends Annotation> Type getAnnotation(Class<Type> annotationType);

	/** @return wrapped methods of this class. Note that super and static methods are not returned. */
	ReflectedMethod[] getMethods();

	/** @return wrapped fields of this class. Note that super and static fields are not returned. */
	ReflectedField[] getFields();

	/** @return a new instance of the selected class constructed with public no-arg constructor.
	 * @throws ReflectionException if unable to construct new instance of selected class. */
	Object newInstance() throws ReflectionException;
}
