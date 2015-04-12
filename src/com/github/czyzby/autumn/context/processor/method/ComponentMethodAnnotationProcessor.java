package com.github.czyzby.autumn.context.processor.method;

import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.AbstractComponentAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.ComponentAnnotationType;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedMethod;

/** The base class for processors that handle method annotations.
 *
 * @author MJ */
public abstract class ComponentMethodAnnotationProcessor extends AbstractComponentAnnotationProcessor {
	@Override
	public ComponentAnnotationType getAnnotationType() {
		return ComponentAnnotationType.METHOD;
	}

	/** @param context contains the component.
	 * @param component contains the annotated method.
	 * @param method is annotated with the processed type. */
	public abstract <Type> void processMethod(ContextContainer context, ContextComponent component,
			ReflectedMethod method);
}
