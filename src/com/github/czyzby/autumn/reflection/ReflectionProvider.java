package com.github.czyzby.autumn.reflection;

import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;

/** An interface that allows to convert a class object to wrapped reflection helper class.
 *
 * @author MJ */
public interface ReflectionProvider {
	/** @return reflection wrapper for the passed class. */
	ReflectedClass provide(Class<?> reflectedClass);
}
