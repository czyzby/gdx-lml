package com.github.czyzby.autumn.mvc.component.ui.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.Field;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.field.ComponentFieldAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.impl.AbstractAnnotatedController;
import com.github.czyzby.autumn.mvc.component.ui.dto.injection.ActorFieldInjection;
import com.github.czyzby.autumn.mvc.component.ui.dto.injection.ArrayActorFieldInjection;
import com.github.czyzby.autumn.mvc.component.ui.dto.injection.ObjectMapActorFieldInjection;
import com.github.czyzby.autumn.mvc.component.ui.dto.injection.ObjectSetActorFieldInjection;
import com.github.czyzby.autumn.mvc.component.ui.dto.injection.SingleActorFieldInjection;
import com.github.czyzby.autumn.mvc.stereotype.ViewActor;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

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
			final Field field) {
		final Class<?> controllerClass = field.getDeclaringClass();
		if (!registerField(field, interfaceService.get().getController(controllerClass))) {
			// If view controller not found, trying out dialog controllers:
			if (!registerField(field, interfaceService.get().getDialogController(controllerClass))) {
				throw new AutumnRuntimeException("Unable to assign injectable actor for field: " + field
						+ " of component: " + component.getComponent()
						+ ". Using custom controllers, which do not support actor injection.");
			}
		}
	}

	private boolean registerField(final Field field, final Object controller) {
		if (controller instanceof AbstractAnnotatedController) {
			final String[] actorIds = Reflection.getAnnotation(field, ViewActor.class).value();
			ActorFieldInjection injection;
			if (Array.class.equals(field.getType())) {
				injection = new ArrayActorFieldInjection(field, getActorIds(actorIds, field));
			} else if (ObjectSet.class.equals(field.getType())) {
				injection = new ObjectSetActorFieldInjection(field, getActorIds(actorIds, field));
			} else if (ObjectMap.class.equals(field.getType())) {
				injection = new ObjectMapActorFieldInjection(field, getActorIds(actorIds, field));
			} else {
				injection = new SingleActorFieldInjection(field, getSingleActorId(actorIds, field));
			}
			((AbstractAnnotatedController) controller).registerActorField(injection);
			return true;
		}
		return false;
	}

	private String[] getActorIds(final String[] annotatedActorIds, final Field field) {
		if (annotatedActorIds == null || annotatedActorIds.length == 0) {
			return new String[] { field.getName() };
		}
		return annotatedActorIds;
	}

	private String getSingleActorId(final String[] actorIds, final Field field) {
		if (actorIds != null && actorIds.length > 0) {
			return actorIds[0];
		}
		return field.getName();
	}
}