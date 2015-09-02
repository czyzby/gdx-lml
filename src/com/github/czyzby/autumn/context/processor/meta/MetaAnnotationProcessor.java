package com.github.czyzby.autumn.context.processor.meta;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.annotation.ComponentAnnotations;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.ComponentAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Default meta component processor. Registers custom processors annotated with MetaComponent.
 *
 * @author MJ */
public class MetaAnnotationProcessor extends ComponentMetaAnnotationProcessor {
	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return MetaComponent.class;
	}

	@Override
	public void processClass(final ContextContainer context, final Class<?> componentClass) {
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

	private static void injectFields(final ContextContainer context, final Class<?> componentClass,
			final ComponentAnnotationProcessor processor) {
		try {
			for (final Field field : ClassReflection.getDeclaredFields(componentClass)) {
				injectField(context, processor, field);
			}
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException(
					"Unable to inject meta component fields. Note that injection is limited for meta components.",
					exception);
		}
	}

	private static void injectField(final ContextContainer context,
			final ComponentAnnotationProcessor processor, final Field field) throws ReflectionException {
		if (field.isAnnotationPresent(Inject.class)) {
			final Inject injectionData = Reflection.getAnnotation(field, Inject.class);
			if (ComponentAnnotations.isClassSet(injectionData.lazy())) {
				Reflection.setFieldValue(field, processor, prepareLazyInjectedField(context, injectionData));
			} else {
				Reflection.setFieldValue(field, processor, context.extractFromContext(field.getType())
						.getComponent());
			}
		}
	}

	private static Lazy<?> prepareLazyInjectedField(final ContextContainer context, final Inject injectionData) {
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