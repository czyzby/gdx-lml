package com.github.czyzby.autumn.context.processor.meta;

import java.lang.annotation.Annotation;

import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.ComponentAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;

/** Default meta component processor. Registers custom processors annotated with MetaComponent.
 *
 * @author MJ */
public class MetaAnnotationProcessor extends ComponentMetaAnnotationProcessor {
	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return MetaComponent.class;
	}

	@Override
	public void processClass(final ContextContainer context, final ReflectedClass componentClass) {
		final Object metaComponent = getNewInstanceOf(componentClass);
		if (metaComponent instanceof ComponentAnnotationProcessor) {
			final ComponentAnnotationProcessor processor = (ComponentAnnotationProcessor) metaComponent;
			processor.getAnnotationType().registerProcessor(context, processor);
		} else {
			throw new AutumnRuntimeException(
					"Unable to process meta component of class: "
							+ componentClass
							+ ". By default, meta components have to implement ComponentAnnotationProcessor interface.");
		}
	}
}
