package com.github.czyzby.autumn.context.processor.type;

import java.lang.annotation.Annotation;

import com.github.czyzby.autumn.annotation.stereotype.Configuration;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;

/** Finds actual configuration context components, which are destroyed immediately after initiation.
 *
 * @author MJ */
public class ConfigurationAnnotationProcessor extends ComponentTypeAnnotationProcessor {
	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return Configuration.class;
	}

	@Override
	public void processClass(final ContextContainer context, final ReflectedClass componentClass) {
		context.registerComponent(prepareComponent(context, componentClass));
	}

	@Override
	public ContextComponent prepareComponent(final ContextContainer context,
			final ReflectedClass componentClass) {
		// No need for annotation data, settings are static.
		return new ContextComponent(componentClass, getNewInstanceOf(componentClass), false, false);
	}
}
