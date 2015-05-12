package com.github.czyzby.autumn.mvc.component.ui.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.Component;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.type.ComponentTypeAnnotationProcessor;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewController;
import com.github.czyzby.autumn.mvc.component.ui.controller.impl.AnnotatedViewController;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Processes {@link com.github.czyzby.autumn.mvc.stereotype.View} components. Initiates controllers.
 *
 * @author MJ */
@MetaComponent
public class ViewAnnotationProcessor extends ComponentTypeAnnotationProcessor {
	@Inject(lazy = InterfaceService.class)
	private Lazy<InterfaceService> interfaceService;

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return View.class;
	}

	@Override
	public void processClass(final ContextContainer context, final Class<?> componentClass) {
		final boolean isInContext = context.contains(componentClass);
		final ContextComponent component =
				isInContext ? context.extractFromContext(componentClass) : prepareComponent(context,
						componentClass);
		final Object controller = component.getComponent();
		final View viewData = Reflection.getAnnotation(componentClass, View.class);
		if (controller instanceof ViewController) {
			interfaceService.get().registerController(controller.getClass(), (ViewController) controller);
		} else {
			interfaceService.get().registerController(controller.getClass(),
					new AnnotatedViewController(viewData, controller, context));
		}
		if (!isInContext) {
			context.registerComponent(component);
		}
	}

	@Override
	public ContextComponent prepareComponent(final ContextContainer context, final Class<?> componentClass) {
		if (ClassReflection.isAnnotationPresent(componentClass, Component.class)) {
			final Component componentData = Reflection.getAnnotation(componentClass, Component.class);
			return new ContextComponent(componentClass, getNewInstanceOf(componentClass),
					componentData.lazy(), componentData.keepInContext());
		}
		// Not lazy and kept in context by default.
		return new ContextComponent(componentClass, getNewInstanceOf(componentClass), false, true);
	}
}