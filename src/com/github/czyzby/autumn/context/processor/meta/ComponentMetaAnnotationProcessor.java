package com.github.czyzby.autumn.context.processor.meta;

import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.AbstractComponentAnnotationProcessor;
import com.github.czyzby.autumn.context.processor.ComponentAnnotationType;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;

/** The base class for processors that handle class annotations.
 *
 * @author MJ */
public abstract class ComponentMetaAnnotationProcessor extends AbstractComponentAnnotationProcessor {
	@Override
	public ComponentAnnotationType getAnnotationType() {
		return ComponentAnnotationType.META;
	}

	/** @param context schedules processing of the meta component.
	 * @param componentClass class annotated with the processed type of annotation. */
	public abstract void processClass(ContextContainer context, ReflectedClass componentClass);
}
