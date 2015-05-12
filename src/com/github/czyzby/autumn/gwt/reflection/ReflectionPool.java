package com.github.czyzby.autumn.gwt.reflection;

/** Reflection-aware object that allows to access all classes available in the GWT reflection pool. Default
 * implementation is auto-generated.
 *
 * @author MJ */
public interface ReflectionPool {
	/** @return all classes available in GWT reflection pool. */
	Class<?>[] getReflectedClasses();
}