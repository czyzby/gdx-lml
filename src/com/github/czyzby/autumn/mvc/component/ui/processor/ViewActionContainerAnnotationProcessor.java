package com.github.czyzby.autumn.mvc.component.ui.processor;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.field.Inject;
import com.github.czyzby.autumn.annotation.stereotype.Component;
import com.github.czyzby.autumn.annotation.stereotype.MetaComponent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.type.ComponentTypeAnnotationProcessor;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.dto.provider.ActionContainerViewActionProvider;
import com.github.czyzby.autumn.mvc.component.ui.dto.provider.ActorConsumerViewActionProvider;
import com.github.czyzby.autumn.mvc.component.ui.dto.provider.ViewActionProvider;
import com.github.czyzby.autumn.mvc.stereotype.ViewActionContainer;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedClass;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.Lazy;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.parser.impl.dto.ActionContainer;
import com.github.czyzby.lml.parser.impl.dto.ActorConsumer;

@MetaComponent
public class ViewActionContainerAnnotationProcessor extends ComponentTypeAnnotationProcessor {
	@Inject(lazy = InterfaceService.class)
	private Lazy<InterfaceService> interfaceService;

	private final Array<ViewActionProvider> actionProviders = GdxArrays.newArray();

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return ViewActionContainer.class;
	}

	@Override
	public void processClass(final ContextContainer context, final ReflectedClass componentClass) {
		final boolean isInContext = context.contains(componentClass.getReflectedClass());
		final ContextComponent component =
				isInContext ? context.extractFromContext(componentClass.getReflectedClass())
						: prepareComponent(context, componentClass);
		final Object actionContainer = component.getComponent();
		final ViewActionContainer actionData = componentClass.getAnnotation(ViewActionContainer.class);
		if (isGlobal(actionData)) {
			registerGlobalAction(actionData.value(), actionContainer);
		} else {
			registerLocalizedAction(actionData.value(), actionData.views(), actionContainer);
		}
		if (!isInContext) {
			context.registerComponent(component);
		}
	}

	private boolean isGlobal(final ViewActionContainer actionData) {
		return actionData.views().length == 0;
	}

	private void registerGlobalAction(final String id, final Object actionContainer) {
		if (actionContainer instanceof ActionContainer) {
			interfaceService.get().addViewActionContainer(id, (ActionContainer) actionContainer);
		} else if (actionContainer instanceof ActorConsumer) {
			interfaceService.get().addViewAction(id, (ActorConsumer<?, ?>) actionContainer);
		} else {
			throw new AutumnRuntimeException("Invalid type annotated with ViewActionContainer: "
					+ actionContainer);
		}
	}

	private void registerLocalizedAction(final String actionId, final String[] viewIds,
			final Object actionContainer) {
		if (actionContainer instanceof ActionContainer) {
			actionProviders.add(new ActionContainerViewActionProvider(actionId,
					(ActionContainer) actionContainer, viewIds));
		} else if (actionContainer instanceof ActorConsumer) {
			actionProviders.add(new ActorConsumerViewActionProvider(actionId,
					(ActorConsumer<?, ?>) actionContainer, viewIds));
		} else {
			throw new AutumnRuntimeException("Invalid type annotated with ViewActionContainer: "
					+ actionContainer);
		}
	}

	@Override
	public ContextComponent prepareComponent(final ContextContainer context,
			final ReflectedClass componentClass) {
		if (componentClass.isAnnotatedWith(Component.class)) {
			final Component componentData = componentClass.getAnnotation(Component.class);
			return new ContextComponent(componentClass, getNewInstanceOf(componentClass),
					componentData.lazy(), componentData.keepInContext());
		}
		// Not lazy and kept in context by default.
		return new ContextComponent(componentClass, getNewInstanceOf(componentClass), false, true);
	}

	/** @return all registered actions that should be available only for specific views. */
	public Array<ViewActionProvider> getActionProviders() {
		return actionProviders;
	}
}