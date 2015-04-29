package com.github.czyzby.autumn.context.processor.meta;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.ComponentAnnotations;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.ComponentAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;

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
			injectFields(context, componentClass, processor);
			processor.getAnnotationType().registerProcessor(context, processor);
		} else {
			throw new AutumnRuntimeException(
					"Unable to process meta component of class: "
							+ componentClass
							+ ". By default, meta components have to implement ComponentAnnotationProcessor interface.");
		}
	}

	private void injectFields(final ContextContainer context, final ReflectedClass componentClass,
			final ComponentAnnotationProcessor processor) {
		try {
			for (final ReflectedField field : componentClass.getFields()) {
				injectField(context, processor, field);
			}
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException(
					"Unable to inject meta component fields. Note that injection is limited for meta components.",
					exception);
		}
	}

	private void injectField(final ContextContainer context, final ComponentAnnotationProcessor processor,
			final ReflectedField field) throws ReflectionException {
		if (field.isAnnotatedWith(Inject.class)) {
			final Inject injectionData = field.getAnnotation(Inject.class);
			if (ComponentAnnotations.isClassSet(injectionData.lazy())) {
				field.set(processor, prepareLazyInjectedField(context, injectionData));
			} else {
				field.set(processor, context.extractFromContext(field.getFieldType()).getComponent());
			}
		}
	}

	private Lazy<?> prepareLazyInjectedField(final ContextContainer context, final Inject injectionData) {
		if (injectionData.concurrentLazy()) {
			return Lazy.concurrentProvidedBy(MetaComponentProvider.with(context, injectionData.lazy()));
		} else {
			return Lazy.providedBy(MetaComponentProvider.with(context, injectionData.lazy()));
		}
	}

	private static class MetaComponentProvider<Type> implements ObjectProvider<Type> {
		private final ContextContainer context;
		private final Class<Type> componentClass;

		public MetaComponentProvider(final ContextContainer context, final Class<Type> componentClass) {
			this.context = context;
			this.componentClass = componentClass;
		}

		public static <Type> MetaComponentProvider<Type> with(final ContextContainer context,
				final Class<Type> componentClass) {
			return new MetaComponentProvider<Type>(context, componentClass);
		}

		@Override
		public Type provide() {
			return context.extractFromContext(componentClass).getComponent(componentClass);
		}
	}
}
