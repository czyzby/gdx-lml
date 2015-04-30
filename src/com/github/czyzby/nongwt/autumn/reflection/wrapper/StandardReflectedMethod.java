package com.github.czyzby.nongwt.autumn.reflection.wrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedMethod;

/** Wraps around a single method, allowing to access its annotations and invoke it.
 *
 * @author MJ */
public class StandardReflectedMethod implements ReflectedMethod {
	private final ReflectedClass reflectedClass;
	private final Method method;

	public StandardReflectedMethod(final ReflectedClass reflectedClass, final Method method) {
		this.reflectedClass = reflectedClass;
		this.method = method;
		method.setAccessible(true);
	}

	@Override
	public ReflectedClass getReflectedClass() {
		return reflectedClass;
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return method.getParameterTypes();
	}

	@Override
	public <Type extends Annotation> boolean isAnnotatedWith(final Class<Type> annotationType) {
		return method.isAnnotationPresent(annotationType);
	}

	@Override
	public <Type extends Annotation> Type getAnnotation(final Class<Type> annotationType) {
		return method.getAnnotation(annotationType);
	}

	@Override
	public Annotation[] getAnnotations() {
		return method.getAnnotations();
	}

	@Override
	public Object invoke(final Object owner, final Object... parameters) throws ReflectionException {
		try {
			return method.invoke(owner, parameters);
		} catch (final Throwable exception) {
			throw new ReflectionException("Unable to invoke method: " + method + " with owner: " + owner,
					exception);
		}
	}

	@Override
	public boolean equals(final Object object) {
		return object == this || object instanceof StandardReflectedMethod
				&& ((StandardReflectedMethod) object).method.equals(method);
	}

	@Override
	public int hashCode() {
		return method.hashCode();
	}

	@Override
	public String toString() {
		return method.toString();
	}
}