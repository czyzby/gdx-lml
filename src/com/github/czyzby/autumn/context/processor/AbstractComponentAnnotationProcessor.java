package com.github.czyzby.autumn.context.processor;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.error.AutumnRuntimeException;

/** Base implementation for annotation processors. Provides utilities for general annotation processing.
 *
 * @author MJ */
public abstract class AbstractComponentAnnotationProcessor implements ComponentAnnotationProcessor {
	private final ContextComponent component = new ContextComponent(getClass(), this, false, true, true);

	@Override
	public ContextComponent toContextComponent() {
		return component;
	}

	protected Object getNewInstanceOf(final Class<?> componentClass) {
		try {
			return ClassReflection.newInstance(componentClass);
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException(
					"Unable to construct a new instance of: " + componentClass + ".", exception);
		}
	}
}
