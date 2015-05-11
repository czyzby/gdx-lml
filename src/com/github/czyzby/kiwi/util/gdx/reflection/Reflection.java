package com.github.czyzby.kiwi.util.gdx.reflection;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

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
		if (isAnnotationPresent(method, annotationType)) {
			return method.getDeclaredAnnotation(annotationType).getAnnotation(annotationType);
		}
		return null;
	}

	/** Utility method, workaround for LibGDX 1.6.0 bug where unannotated methods throw exceptions on GWT when
	 * {@link com.badlogic.gdx.utils.reflect.Method#isAnnotationPresent(Class)} is used.
	 *
	 * @param method might contain the specified annotation.
	 * @param annotationType class of the annotation that the method is checked against.
	 * @return true if method is annotated with the specified annotation. */
	public static boolean isAnnotationPresent(final Method method,
			final Class<? extends Annotation> annotationType) {
		try {
			return method.isAnnotationPresent(annotationType);
		} catch (final Throwable exception) {
			// Expected on GWT.
			return false;
		}
	}

	/** @param method will be set accessible and invoked.
	 * @param methodOwner instance of class with the method. Will have the method invoked. Can be null (static
	 *            methods).
	 * @param arguments method arguments.
	 * @return result of method invocation.
	 * @throws ReflectionException when unable to invoke the method. */
	public static Object invokeMethod(final Method method, final Object methodOwner,
			final Object... arguments) throws ReflectionException {
		method.setAccessible(true);
		return method.invoke(methodOwner, arguments);
	}

	/** @param method will be set accessible and invoked.
	 * @param methodOwner will have the method invoked. Can be null (static methods).
	 * @param resultType result will be casted to this type.
	 * @param arguments method arguments.
	 * @return result of method invocation.
	 * @throws ReflectionException when unable to invoke the method. */
	@SuppressWarnings("unchecked")
	public static <ResultType> ResultType invokeMethod(final Method method, final Object methodOwner,
			final Class<ResultType> resultType, final Object... arguments) throws ReflectionException {
		method.setAccessible(true);
		return (ResultType) method.invoke(methodOwner, arguments);
	}

	/** @param field will be set accessible and extracted.
	 * @param fieldOwner instance of class that contains the field.
	 * @return current field value.
	 * @throws ReflectionException if unable to extract. */
	public static Object getFieldValue(final Field field, final Object fieldOwner) throws ReflectionException {
		field.setAccessible(true);
		return field.get(fieldOwner);
	}

	/** @param field will be set accessible and extracted.
	 * @param fieldOwner instance of class that contains the field.
	 * @param fieldType class of the field. Will be used to cast the field object.
	 * @return current field value.
	 * @throws ReflectionException if unable to extract. */
	@SuppressWarnings("unchecked")
	public static <FieldType> FieldType getFieldValue(final Field field, final Object fieldOwner,
			final Class<FieldType> fieldType) throws ReflectionException {
		field.setAccessible(true);
		return (FieldType) field.get(fieldOwner);
	}

	/** @param field will be set in the passed object.
	 * @param fieldOwner instance of the class that contains the field.
	 * @param fieldValue will be set as the new field value.
	 * @throws ReflectionException if unable to set. */
	public static void setFieldValue(final Field field, final Object fieldOwner, final Object fieldValue)
			throws ReflectionException {
		field.setAccessible(true);
		field.set(fieldOwner, fieldValue);
	}
}