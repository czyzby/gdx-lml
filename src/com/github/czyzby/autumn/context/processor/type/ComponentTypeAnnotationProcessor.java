package com.github.czyzby.autumn.context.processor.type;

import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.AbstractComponentAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.ComponentAnnotationType;

/** The base class for processors that handle type (class) annotations.
 *
 * @author MJ */
public abstract class ComponentTypeAnnotationProcessor extends AbstractComponentAnnotationProcessor {
	@Override
	public ComponentAnnotationType getAnnotationType() {
		return ComponentAnnotationType.TYPE;
	}

	/** @param context executes the processing of the given class.
	 * @param componentClass class annotated with the processed type of annotation. */
	public abstract void processClass(ContextContainer context, Class<?> componentClass);

	/** @param context executes the processing of the given class.
	 * @param componentClass class annotated with the processed type of annotation.
	 * @return a new instance of context component, created on demand. Can be null if the processor does not
	 *         create new components based on the type annotation. */
	public abstract ContextComponent prepareComponent(ContextContainer context, Class<?> componentClass);
}