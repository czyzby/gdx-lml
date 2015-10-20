package com.github.czyzby.autumn.gwt.scanner;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.gwt.reflection.ReflectionPool;
import com.github.czyzby.autumn.scanner.AbstractClassScanner;
import com.google.gwt.core.client.GWT;

/** Scans the whole GWT reflection pool.
 *
 * @author MJ */
public class GwtClassScanner extends AbstractClassScanner {
    private static final ReflectionPool REFLECTION_POOL = GWT.create(ReflectionPool.class);

    @Override
    public Array<Class<?>> findClassesAnnotatedWith(final Class<?> root,
            final Iterable<Class<? extends Annotation>> annotations) {
        final String packageName = extractPackageName(root);
        final Array<Class<?>> result = new Array<Class<?>>();
        for (final Class<?> possibleMatch : REFLECTION_POOL.getReflectedClasses()) {
            if (isInPackage(possibleMatch, packageName) && isAnnotatedWithAny(possibleMatch, annotations)) {
                result.add(possibleMatch);
            }
        }
        return result;
    }
}