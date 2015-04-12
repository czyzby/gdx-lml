package com.github.czyzby.autumn.reflection.wrapper.gwt;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedMethod;

/** GWT reflected method data container.
 *
 * @author MJ */
public abstract class GwtReflectedMethod implements ReflectedMethod {
	private final int methodId;
	private final Class<?>[] parameterTypes;
	private ReflectedClass reflectedClass;
	private final Map<Class<? extends Annotation>, Annotation> annotations;

	/** @param methodId has to be unique among all methods of all classes. */
	public GwtReflectedMethod(final int methodId, final Class<?>[] parameterTypes,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		this.methodId = methodId;
		this.parameterTypes = parameterTypes;
		this.annotations = annotations;
	}

	@Override
	public ReflectedClass getReflectedClass() {
		return reflectedClass;
	}

	public void setReflectedClass(final ReflectedClass reflectedClass) {
		this.reflectedClass = reflectedClass;
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	@Override
	public <Type extends Annotation> boolean isAnnotatedWith(final Class<Type> annotationType) {
		return annotations.containsKey(annotationType);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Type extends Annotation> Type getAnnotation(final Class<Type> annotationType) {
		return (Type) annotations.get(annotationType);
	}

	@Override
	public boolean equals(final Object object) {
		return object == this || object instanceof GwtReflectedMethod
				&& ((GwtReflectedMethod) object).methodId == methodId;
	}

	@Override
	public int hashCode() {
		return methodId;
	}

	@Override
	public String toString() {
		return "method of: " + reflectedClass + ", parameter types: " + Arrays.toString(parameterTypes)
				+ ", method ID: " + methodId;
	}
}
