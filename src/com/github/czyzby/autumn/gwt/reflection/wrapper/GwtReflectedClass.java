package com.github.czyzby.autumn.gwt.reflection.wrapper;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.reflection.Reflection;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedMethod;

/** GWT reflected class data container.
 *
 * @author MJ */
public class GwtReflectedClass implements ReflectedClass {
	private final Class<?> reflectedClass;
	private final Class<?>[] interfaces;
	private final Class<?> superclass;
	private final ReflectedMethod[] methods;
	private final ReflectedField[] fields;
	private final Map<Class<? extends Annotation>, Annotation> annotations;
	private final ReflectedMethod constructor;

	public GwtReflectedClass(final Class<?> reflectedClass, final Class<?>[] interfaces,
			final Class<?> superclass, final ReflectedMethod[] methods, final ReflectedField[] fields,
			final Map<Class<? extends Annotation>, Annotation> annotations, final ReflectedMethod constructor) {
		this.reflectedClass = reflectedClass;
		this.interfaces = interfaces;
		this.superclass = superclass;
		this.methods = methods;
		this.fields = fields;
		this.annotations = annotations;
		this.constructor = constructor;
	}

	public void initiate() {
		if (constructor != null) {
			((GwtReflectedMethod) constructor).setReflectedClass(this);
		}
		for (final ReflectedMethod method : methods) {
			((GwtReflectedMethod) method).setReflectedClass(this);
		}
		for (final ReflectedField field : fields) {
			((GwtReflectedField) field).setReflectedClass(this);
		}
	}

	@Override
	public Class<?> getReflectedClass() {
		return reflectedClass;
	}

	@Override
	public ReflectedClass getSuperclass() {
		try {
			return Reflection.getWrapperForClass(superclass);
		} catch (final Throwable exception) {
			// Class not available. Expected.
			return null;
		}
	}

	@Override
	public Class<?>[] getInterfaces() {
		return interfaces;
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
	public ReflectedMethod[] getMethods() {
		return methods;
	}

	@Override
	public ReflectedField[] getFields() {
		return fields;
	}

	@Override
	public Object newInstance() throws ReflectionException {
		return constructor.invoke(null, Reflection.EMPTY_ARRAY);
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
