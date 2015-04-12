package com.github.czyzby.autumn.context.processor;

import java.lang.annotation.Annotation;

import com.github.czyzby.autumn.context.ContextComponent;

/** Interface shared by all annotation processors. Allows to determine the type of annotated field handled by
 * the processor. Note that custom processors have to extend one of abstract implementations of this class to
 * be correctly registered.
 *
 * @author MJ */
public interface ComponentAnnotationProcessor {
	/** @return annotation type handled by this processor. */
	Class<? extends Annotation> getProcessedAnnotationClass();

	/** @return scope of the processed annotation. */
	ComponentAnnotationType getAnnotationType();

	/** @return this annotation processor wrapped as a context component. */
	ContextComponent toContextComponent();
}
