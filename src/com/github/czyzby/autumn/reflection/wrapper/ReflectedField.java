package com.github.czyzby.autumn.reflection.wrapper;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.ReflectionException;

/** Interface for utility wrappers of reflected classes' fields.
 *
 * @author MJ */
public interface ReflectedField {
	/** @return wrapped class that possesses this field. */
	ReflectedClass getReflectedClass();

	/** @return actual class of the reflected field. */
	Class<?> getFieldType();

	/** @param annotationType annotation class.
	 * @return true if the field is annotated with selected annotation type. */
	<Type extends Annotation> boolean isAnnotatedWith(Class<Type> annotationType);

	/** @param annotationType annotation class.
	 * @return annotation instance of the selected type or null. */
	<Type extends Annotation> Type getAnnotation(Class<Type> annotationType);

	/** @param owner an object of the type that has this kind of field.
	 * @param fieldValue value that will be assigned to the selected field in owner object.
	 * @throws ReflectionException if unable to assign field. */
	void set(Object owner, Object fieldValue) throws ReflectionException;

	/** @param owner an object of the type that has this kind of field.
	 * @return current value assigned to the field.
	 * @throws ReflectionException if unable to assign field. */
	Object get(Object owner) throws ReflectionException;
}
