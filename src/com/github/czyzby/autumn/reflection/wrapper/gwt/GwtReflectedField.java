package com.github.czyzby.autumn.reflection.wrapper.gwt;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;

/** GWT reflected field data container.
 *
 * @author MJ */
public abstract class GwtReflectedField implements ReflectedField {
	private final int fieldId;
	private final Class<?> fieldType;
	private ReflectedClass reflectedClass;
	private final Map<Class<? extends Annotation>, Annotation> annotations;

	/** @param fieldId has to be unique among all fields of all classes. */
	public GwtReflectedField(final int fieldId, final Class<?> fieldType,
			final Map<Class<? extends Annotation>, Annotation> annotations) {
		this.fieldId = fieldId;
		this.fieldType = fieldType;
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
	public Class<?> getFieldType() {
		return fieldType;
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
		return object == this || object instanceof GwtReflectedField
				&& ((GwtReflectedField) object).fieldId == fieldId;
	}

	@Override
	public int hashCode() {
		return fieldId;
	}

	@Override
	public String toString() {
		return "field of: " + reflectedClass + ", type: " + fieldType + ", field ID: " + fieldId;
	}
}
