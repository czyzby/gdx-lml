package com.github.czyzby.autumn.gwt.reflection;

import com.github.czyzby.autumn.reflection.ReflectionProvider;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;

/** Extended reflection provider, additionally to wrapping class objects into ReflectedClasses, allows to
 * access the whole pool of available reflected classes.
 *
 * @author MJ */
public interface ExtendedReflectionProvider extends ReflectionProvider {
	/** @return every class available through reflection mechanism. */
	Iterable<ReflectedClass> getReflectedClassesPool();
}
