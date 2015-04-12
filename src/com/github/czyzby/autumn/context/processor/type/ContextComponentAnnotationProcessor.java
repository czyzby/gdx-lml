package com.github.czyzby.autumn.context.processor.type;

import java.lang.annotation.Annotation;

import com.github.czyzby.autumn.annotation.stereotype.Component;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;

/** Finds actual context components annotated with Component.
 *
 * @author MJ */
public class ContextComponentAnnotationProcessor extends ComponentTypeAnnotationProcessor {
	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return Component.class;
	}

	@Override
	public void processClass(final ContextContainer context, final ReflectedClass componentClass) {
		context.registerComponent(prepareComponent(context, componentClass));
	}

	@Override
	public ContextComponent prepareComponent(final ContextContainer context,
			final ReflectedClass componentClass) {
		final Component componentData = componentClass.getAnnotation(Component.class);
		return new ContextComponent(componentClass, getNewInstanceOf(componentClass), componentData.lazy(),
				componentData.keepInContext());
	}
}
