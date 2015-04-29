package com.github.czyzby.autumn.reflection;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;

/** Static reflection utilities for Autumn framework. Autumn reflection is far from being feature-complete, but
 * it's implemented in a much more efficient way than the default GWT reflection, it keeps informations about
 * methods' annotations and, well, it's enough for Autumn MVC needs. Reflected classes' objects cannot be
 * referenced by names - they need to be retrieved by class objects. To be extra light-weight, GWT-reflected
 * classes keep track ONLY of registered classes and their ANNOTATED methods and fields, as other data is
 * pretty much ignored and unnecessary for Autumn core. Do NOT overuse reflection in custom annotation
 * processors (or, at least, use ClassReflection instead and convert Autumn-wrapped objects to GDX-wrapped
 * reflected objects. Yeah).
 *
 * @author MJ */
public class Reflection {
	private static ReflectionProvider reflectionProvider;
	/** Empty object array. Utility for invoking empty methods. */
	public static final Object[] EMPTY_ARRAY = new Object[0];

	private Reflection() {
	}

	/** @param reflectionProvider HAS to be set, otherwise Autumn will not be able to scan and process context
	 *            components. */
	public static void setReflectionProvider(final ReflectionProvider reflectionProvider) {
		Reflection.reflectionProvider = reflectionProvider;
	}

	/** @return reflection wrapper for the selected class. */
	public static ReflectedClass getWrapperForClass(final Class<?> reflectedClass) {
		return reflectionProvider.provide(reflectedClass);
	}

	/** @return new instance of class using Autumn reflection or LibGDX reflection, if unable to find
	 *         Autumn-reflected class. Suppresses reflection exceptions. */
	@SuppressWarnings("unchecked")
	public static <Type> Type newInstance(final Class<Type> reflectedClass) {
		try {
			return (Type) getWrapperForClass(reflectedClass).newInstance();
		} catch (final ReflectionException reflectionException) {
			try {
				return ClassReflection.newInstance(reflectedClass);
			} catch (final ReflectionException exception) {
				throw new AutumnRuntimeException("Unable to initiate instance of class: " + reflectedClass,
						exception);
			}
		}
	}
}
