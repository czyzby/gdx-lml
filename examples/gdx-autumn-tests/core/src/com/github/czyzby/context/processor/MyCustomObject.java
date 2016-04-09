package com.github.czyzby.context.processor;

import com.github.czyzby.autumn.annotation.Inject;

/** A simple example of a class annotated with {@link MyCustomAnnotation}.
 *
 * @author MJ */
@MyCustomAnnotation
public class MyCustomObject {
    /** This value will be injected with an instance of {@link MyCustomAnnotationTest}. */
    @Inject MyCustomAnnotationTest injectedTest;

    @Override
    public String toString() {
        return "(MyCustomObject[injectedTest=" + injectedTest + "])";
    }
}
