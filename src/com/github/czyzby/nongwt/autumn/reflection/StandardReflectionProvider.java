package com.github.czyzby.nongwt.autumn.reflection;

import com.github.czyzby.autumn.reflection.ReflectionProvider;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.nongwt.autumn.reflection.wrapper.StandardReflectedClass;

/** Uses regular Java reflection mechanism.
 *
 * @author MJ */
public class StandardReflectionProvider implements ReflectionProvider {
	@Override
	public ReflectedClass provide(final Class<?> reflectedClass) {
		return new StandardReflectedClass(reflectedClass);
	}
}
