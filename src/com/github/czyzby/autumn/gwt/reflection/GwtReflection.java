package com.github.czyzby.autumn.gwt.reflection;

import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.google.gwt.core.client.GWT;

/** GWT-specific reflection utilities.
 *
 * @author MJ */
public class GwtReflection {
	private static final ExtendedReflectionProvider REFLECTION_PROVIDER = GWT
			.create(ExtendedReflectionProvider.class);

	private GwtReflection() {
	}

	/** @return all GWT-reflected classes that can be used by Autumn. */
	public static Iterable<ReflectedClass> getAllReflectedClasses() {
		return REFLECTION_PROVIDER.getReflectedClassesPool();
	}

	/** @return GWT implementation of ReflectionProvider. */
	public static ExtendedReflectionProvider getReflectionProvider() {
		return REFLECTION_PROVIDER;
	}
}
