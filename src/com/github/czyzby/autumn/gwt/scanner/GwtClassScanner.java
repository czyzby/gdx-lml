package com.github.czyzby.autumn.gwt.scanner;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.github.czyzby.autumn.gwt.reflection.ReflectionPool;
import com.github.czyzby.autumn.scanner.ClassScanner;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;
import com.google.gwt.core.client.GWT;

/** Scans the whole GWT reflection pool.
 *
 * @author MJ */
public class GwtClassScanner implements ClassScanner {
	private static final ReflectionPool REFLECTION_POOL = GWT.create(ReflectionPool.class);

	@Override
	public ObjectMap<Class<? extends Annotation>, ObjectSet<Class<?>>> findClassesAnnotatedWith(
			final Class<?> root, final Iterable<Class<? extends Annotation>> annotations) {
		final ObjectMap<Class<? extends Annotation>, ObjectSet<Class<?>>> reflectedClasses =
				LazyObjectMap.newMapOfSets();
		final String packageName = getPackageName(root);
		for (final Class<?> reflectedClass : REFLECTION_POOL.getReflectedClasses()) {
			if (!isFromPackage(packageName, reflectedClass)) {
				continue;
			}
			for (final Class<? extends Annotation> annotation : annotations) {
				try {
					if (ClassReflection.isAnnotationPresent(reflectedClass, annotation)) {
						reflectedClasses.get(annotation).add(reflectedClass);
					}
				} catch (final Throwable exception) {
					// It's GWT - everything can happen.
					// TODO Errors might occur here due to changes in LibGDX reflection. Fix generator.
				}
			}
		}
		return reflectedClasses;
	}

	private static String getPackageName(final Class<?> ofClass) {
		return ofClass.getName().substring(0, ofClass.getName().length() - ofClass.getSimpleName().length());
	}

	private static boolean isFromPackage(final String packageName, final Class<?> reflectedClass) {
		return reflectedClass.getName().startsWith(packageName);
	}
}