package com.github.czyzby.autumn.scanner;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;

/** Used to scan classes to look for annotated types.
 *
 * @author MJ */
public interface ClassScanner {
	/** @param root class in the root package. This is were the scanning begins. This setting may or may not be
	 *            ignored, depending on the scanner implementation.
	 * @param annotations will find classes that have one of these annotations.
	 * @return all classes in the root package that have an annotation of the selected type mapped by scanned
	 *         annotations. */
	public ObjectMap<Class<? extends Annotation>, ObjectSet<ReflectedClass>> findClassesAnnotatedWith(
			Class<?> root, Iterable<Class<? extends Annotation>> annotations);
}
