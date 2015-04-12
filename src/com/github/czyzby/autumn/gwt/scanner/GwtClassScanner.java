package com.github.czyzby.autumn.gwt.scanner;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.autumn.gwt.reflection.GwtReflection;
import com.github.czyzby.autumn.reflection.Reflection;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.autumn.scanner.ClassScanner;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;

/** Scans the whole GWT Autumn reflection pool.
 *
 * @author MJ */
public class GwtClassScanner implements ClassScanner {
	public GwtClassScanner() {
		Reflection.setReflectionProvider(GwtReflection.getReflectionProvider());
	}

	@Override
	public ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> findClassesAnnotatedWith(
			final Class<?> root, final Iterable<Class<? extends Annotation>> annotations) {
		final ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> reflectedClasses =
				LazyObjectMap.newMapOfSets();
		final String packageName = getPackageName(root);
		for (final ReflectedClass reflectedClass : GwtReflection.getAllReflectedClasses()) {
			if (!isFromPackage(packageName, reflectedClass)) {
				continue;
			}
			for (final Class<? extends Annotation> annotation : annotations) {
				if (reflectedClass.isAnnotatedWith(annotation)) {
					reflectedClasses.get(annotation).add(reflectedClass);
				}
			}
		}
		return reflectedClasses;
	}

	private String getPackageName(final Class<?> ofClass) {
		return ofClass.getName().substring(0, ofClass.getName().length() - ofClass.getSimpleName().length());
	}

	private boolean isFromPackage(final String packageName, final ReflectedClass reflectedClass) {
		return reflectedClass.getReflectedClass().getName().startsWith(packageName);
	}
}
