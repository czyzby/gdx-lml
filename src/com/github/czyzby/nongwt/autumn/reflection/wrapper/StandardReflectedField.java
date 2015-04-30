package com.github.czyzby.nongwt.autumn.reflection.wrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;

/** Wraps around a single field, providing access to its annotations and allowing to modify it.
 *
 * @author MJ */
public class StandardReflectedField implements ReflectedField {
	private final ReflectedClass reflectedClass;
	private final Field field;

	public StandardReflectedField(final ReflectedClass reflectedClass, final Field field) {
		this.reflectedClass = reflectedClass;
		this.field = field;
		field.setAccessible(true);
	}

	@Override
	public ReflectedClass getReflectedClass() {
		return reflectedClass;
	}

	@Override
	public Class<?> getFieldType() {
		return field.getType();
	}

	@Override
	public <Type extends Annotation> boolean isAnnotatedWith(final Class<Type> annotationType) {
		return field.isAnnotationPresent(annotationType);
	}

	@Override
	public <Type extends Annotation> Type getAnnotation(final Class<Type> annotationType) {
		return field.getAnnotation(annotationType);
	}

	@Override
	public Annotation[] getAnnotations() {
		return field.getAnnotations();
	}

	@Override
	public void set(final Object owner, final Object fieldValue) throws ReflectionException {
		try {
			field.set(owner, fieldValue);
		} catch (final Throwable exception) {
			throw new ReflectionException("Unable to set value: " + fieldValue + " for field: " + field
					+ " in owner: " + owner, exception);
		}
	}

	@Override
	public Object get(final Object owner) throws ReflectionException {
		try {
			return field.get(owner);
		} catch (final Throwable exception) {
			throw new ReflectionException("Unable to get value of field: " + field + " with owner: " + owner,
					exception);
		}
	}

	@Override
	public boolean equals(final Object object) {
		return object == this || object instanceof StandardReflectedField
				&& ((StandardReflectedField) object).field.equals(field);
	}

	@Override
	public int hashCode() {
		return field.hashCode();
	}

	@Override
	public String toString() {
		return field.toString();
	}
}