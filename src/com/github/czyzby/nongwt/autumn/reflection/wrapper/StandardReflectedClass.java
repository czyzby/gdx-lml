package com.github.czyzby.nongwt.autumn.reflection.wrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedMethod;

/** Wraps around a single class, providing access to its methods, fields, interfaces and annotations and
 * allowing to create a new instance of this class with no-arg constructor.
 *
 * @author MJ */
public class StandardReflectedClass implements ReflectedClass {
	private final Class<?> reflectedClass;

	public StandardReflectedClass(final Class<?> reflectedClass) {
		this.reflectedClass = reflectedClass;
	}

	@Override
	public Class<?> getReflectedClass() {
		return reflectedClass;
	}

	@Override
	public ReflectedClass getSuperclass() {
		final Class<?> superclass = reflectedClass.getSuperclass();
		if (superclass == null) {
			return null;
		}
		return new StandardReflectedClass(superclass);
	}

	@Override
	public Class<?>[] getInterfaces() {
		return reflectedClass.getInterfaces();
	}

	@Override
	public <Type extends Annotation> boolean isAnnotatedWith(final Class<Type> annotationType) {
		return reflectedClass.isAnnotationPresent(annotationType);
	}

	@Override
	public <Type extends Annotation> Type getAnnotation(final Class<Type> annotationType) {
		return reflectedClass.getAnnotation(annotationType);
	}

	@Override
	public ReflectedMethod[] getMethods() {
		final Method[] methods = reflectedClass.getDeclaredMethods();
		final ReflectedMethod[] reflectedMethods = new ReflectedMethod[methods.length];
		for (int methodIndex = 0; methodIndex < methods.length; methodIndex++) {
			reflectedMethods[methodIndex] = new StandardReflectedMethod(this, methods[methodIndex]);
		}
		return reflectedMethods;
	}

	@Override
	public ReflectedField[] getFields() {
		final Field[] fields = reflectedClass.getDeclaredFields();
		final ReflectedField[] reflectedFields = new ReflectedField[fields.length];
		for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
			reflectedFields[fieldIndex] = new StandardReflectedField(this, fields[fieldIndex]);
		}
		return reflectedFields;
	}

	@Override
	public Object newInstance() throws ReflectionException {
		try {
			return reflectedClass.newInstance();
		} catch (final Throwable exception) {
			throw new ReflectionException("Unable to initiate an object of class: " + reflectedClass
					+ ". Does it have a public, no-arg constructor?", exception);
		}
	}

	@Override
	public boolean equals(final Object object) {
		return object == this || object instanceof ReflectedClass
				&& ((ReflectedClass) object).getReflectedClass().equals(reflectedClass);
	}

	@Override
	public int hashCode() {
		return reflectedClass.hashCode();
	}

	@Override
	public String toString() {
		return reflectedClass.toString();
	}
}
