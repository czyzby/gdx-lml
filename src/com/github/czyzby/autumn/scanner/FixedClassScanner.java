package com.github.czyzby.autumn.scanner;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.autumn.reflection.Reflection;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;

/** Rather than scanning the whole application, this scanner uses a limited collection of classes that are
 * registered. This might be significantly faster than other scanning methods, but it also requires to
 * register all used components, which is rather redundant.
 *
 * @author MJ */
public class FixedClassScanner implements ClassScanner {
	private final Array<Class<?>> availableClasses;

	/** @param availableClasses will be scanned upon method invoking. Note that this scanner does not have
	 *            access to any other classes than the ones that are registered. */
	public FixedClassScanner(final Class<?>... availableClasses) {
		this.availableClasses = GdxArrays.newArray(availableClasses);
	}

	/** @param classes will become available for scanning. */
	public void register(final Class<?>... classes) {
		availableClasses.addAll(classes);
	}

	@Override
	public ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> findClassesAnnotatedWith(
			final Class<?> root, final Iterable<Class<? extends Annotation>> annotations) {
		final String packageName =
				root.getName().substring(0, root.getName().length() - root.getSimpleName().length() - 1);
		final ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> result =
				LazyObjectMap.newMapOfSets();
		for (final Class<?> classToProcess : availableClasses) {
			final ReflectedClass reflectedClass = Reflection.getWrapperForClass(classToProcess);
			if (classToProcess.getName().startsWith(packageName)) {
				for (final Class<? extends Annotation> annotation : annotations) {
					if (reflectedClass.isAnnotatedWith(annotation)) {
						result.get(annotation).add(reflectedClass);
					}
				}
			}
		}
		return result;
	}
}
