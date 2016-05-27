package com.github.czyzby.autumn.jtransc.scanner;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.github.czyzby.autumn.scanner.AbstractClassScanner;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.jtransc.reflection.JTranscReflection;

/** Default {@link com.github.czyzby.autumn.scanner.ClassScanner ClassScanner} implementation for the JTransc
 * applications.
 *
 * @author MJ
 * @see com.github.czyzby.autumn.scanner.FixedClassScanner */
public class JTranscClassScanner extends AbstractClassScanner {
    @Override
    public Array<Class<?>> findClassesAnnotatedWith(final Class<?> root,
            final Iterable<Class<? extends Annotation>> annotations) {
        final Array<Class<?>> result = GdxArrays.newArray();
        final String packageName = extractPackageName(root);
        for (final String className : JTranscReflection.getAllClasses()) {
            if (Strings.isNotEmpty(className) && className.startsWith(packageName)) {
                try {
                    final Class<?> processed = ClassReflection.forName(className);
                    if (isAnnotatedWithAny(processed, annotations)) {
                        result.add(processed);
                    }
                } catch (final Exception exception) {
                    Gdx.app.debug("JTransc", "Error thrown during processing of class: " + className, exception);
                }
            }
        }
        return result;
    }
}
