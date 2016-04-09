package com.github.czyzby.context.processor;

/** This is an example class using {@link MyCustomAnnotation}.
 *
 * @author MJ */
@MyCustomAnnotation
public class MyCustomAnnotationTest {
    @MyCustomAnnotation("someValue") String myField;

    @MyCustomAnnotation(id = 42)
    int myMethod(final int param) {
        return param * param;
    }

    @Override
    public String toString() {
        return "(MyCustomAnnotationTest[field=" + myField + "])";
    }
}
