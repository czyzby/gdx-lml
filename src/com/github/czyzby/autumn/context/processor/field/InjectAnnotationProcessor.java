package com.github.czyzby.autumn.context.processor.field;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.ComponentAnnotations;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.provider.ContextComponentProvider;
import com.github.czyzby.autumn.context.provider.UniqueContextComponentProvider;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;

/** Processes Inject annotation. Handles variable injection.
 *
 * @author MJ */
public class InjectAnnotationProcessor extends ComponentFieldAnnotationProcessor {
	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return Inject.class;
	}

	@Override
	public <Type> void processField(final ContextContainer context, final ContextComponent component,
			final ReflectedField field) {
		final Inject injectionData = field.getAnnotation(Inject.class);
		if (ComponentAnnotations.isClassSet(injectionData.lazy())) {
			injectLazyVariable(context, component, field, injectionData);
		} else {
			injectRegularVariable(context, component, field, injectionData);
		}
	}

	private void injectLazyVariable(final ContextContainer context, final ContextComponent component,
			final ReflectedField field, final Inject injectionData) {
		Lazy<?> lazyComponent;
		if (injectionData.concurrentLazy()) {
			lazyComponent =
					Lazy.concurrentProvidedBy(getContextComponentProvider(context, injectionData.lazy(),
							injectionData.newInstance()));
		} else {
			lazyComponent =
					Lazy.providedBy(getContextComponentProvider(context, injectionData.lazy(),
							injectionData.newInstance()));
		}
		try {
			field.set(component.getComponent(), lazyComponent);
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to inject lazy variable for component: " + component
					+ ".", exception);
		}
	}

	private <Type> ObjectProvider<Type> getContextComponentProvider(final ContextContainer context,
			final Class<Type> componentClass, final boolean asNewInstance) {
		if (asNewInstance) {
			return new UniqueContextComponentProvider<Type>(context, componentClass);
		}
		return new ContextComponentProvider<Type>(context, componentClass);
	}

	private void injectRegularVariable(final ContextContainer context, final ContextComponent component,
			final ReflectedField field, final Inject injectionData) {
		try {
			if (injectionData.newInstance()) {
				injectNewInstance(context, component, field, injectionData);
			} else {
				injectExtractedFromContext(context, component, field, injectionData);
			}
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to inject variable for component: " + component + ".",
					exception);
		}
	}

	private void injectNewInstance(final ContextContainer context, final ContextComponent component,
			final ReflectedField field, final Inject injectionData) throws ReflectionException {
		final ReflectedClass componentClass =
				context.convertToReflectedClass(ComponentAnnotations.isClassSet(injectionData.value())
						? injectionData.value() : field.getFieldType());
		final ContextComponent componentToInject = context.constructRequestedComponent(componentClass);
		context.requestToWakeLazyComponent(componentToInject);
		field.set(component.getComponent(), componentToInject.getComponent());
	}

	private void injectExtractedFromContext(final ContextContainer context, final ContextComponent component,
			final ReflectedField field, final Inject injectionData) throws ReflectionException {
		final ContextComponent componentToInject;
		if (ComponentAnnotations.isClassSet(injectionData.value())) {
			// Custom class:
			componentToInject = context.extractFromContext(injectionData.value());
		} else {
			componentToInject = context.extractFromContext(field.getFieldType());
		}
		field.set(component.getComponent(), componentToInject.getComponent());
		if (componentToInject.isLazy() && !componentToInject.isInitiated()) {
			context.requestToWakeLazyComponent(componentToInject);
		}
	}
}
