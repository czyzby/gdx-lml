package com.github.czyzby.autumn.annotation;

/** Component annotation utilities. Provides common static methods for annotation processing.
 *
 * @author MJ */
public class ComponentAnnotations {
    private ComponentAnnotations() {
    }

    /** @param annotationClassParameter class parameter from a component annotation that defaults to Void.class.
     * @return true if the passed class was properly set and can be processed. */
    public static boolean isClassSet(final Class<?> annotationClassParameter) {
        return annotationClassParameter != null && !annotationClassParameter.equals(void.class);
    }
}
