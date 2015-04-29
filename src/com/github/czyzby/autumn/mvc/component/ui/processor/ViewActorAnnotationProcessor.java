package com.github.czyzby.autumn.mvc.component.ui.processor;

import java.lang.annotation.Annotation;

import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.impl.AbstractAnnotatedController;
import com.github.czyzby.autumn.mvc.stereotype.ViewActor;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedField;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;

/** Used to process actor fields that should be injected upon view creation.
 *
 * @author MJ */
@MetaComponent
public class ViewActorAnnotationProcessor extends ComponentFieldAnnotationProcessor {
	@Inject(lazy = InterfaceService.class)
	private Lazy<InterfaceService> interfaceService;

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return ViewActor.class;
	}

	@Override
	public <Type> void processField(final ContextContainer context, final ContextComponent component,
			final ReflectedField field) {
		final Class<?> controllerClass = field.getReflectedClass().getReflectedClass();
		if (!registerField(field, interfaceService.get().getController(controllerClass))) {
			// If view controller not found, trying out dialog controllers:
			if (!registerField(field, interfaceService.get().getDialogController(controllerClass))) {
				throw new AutumnRuntimeException("Unable to assign injectable actor for field: " + field
						+ " of component: " + component.getComponent() + ".");
			}
		}
	}

	private boolean registerField(final ReflectedField field, final Object controller) {
		if (controller instanceof AbstractAnnotatedController) {
			((AbstractAnnotatedController) controller).registerActorField(field
					.getAnnotation(ViewActor.class).value(), field);
			return true;
		}
		return false;
	}
}