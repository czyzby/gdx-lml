package com.github.czyzby.kiwi.util.gdx.reflection;

import com.badlogic.gdx.utils.reflect.Annotation;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.github.czyzby.kiwi.util.common.Exceptions;

/** Utilities for LibGDX annotation processing.
 *
 * @author MJ
 * @see Reflection */
public class Annotations {
    private Annotations() {
    }

    /** @param classToProcess will be validated.
     * @return true if the class has at least 1 runtime annotation. */
    public static boolean hasAnnotations(final Class<?> classToProcess) {
        try {
            final Annotation[] annotations = ClassReflection.getAnnotations(classToProcess);
            return annotations != null && annotations.length > 0;
        } catch (final Exception exception) {
            Exceptions.ignore(exception); // Somewhat expected on GWT.
            return false;
        }
    }

    /** @param classInAnnotation will be validated.
     * @return true if the class is not null and does not equal {@link Void} or void class. Void class is often used in
     *         annotation as default return value for not required class methods. */
    public static boolean isNotVoid(final Class<?> classInAnnotation) {
        // Null should never happen in regular Java, but who knows what can happen on GWT.
        return classInAnnotation != null && !classInAnnotation.equals(void.class)
                && !classInAnnotation.equals(Void.class);
    }
}
