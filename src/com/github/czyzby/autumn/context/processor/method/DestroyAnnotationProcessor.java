package com.github.czyzby.autumn.context.processor.method;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.Method;
import com.github.czyzby.autumn.annotation.method.Destroy;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.method.event.ComponentEventListener;
import com.github.czyzby.autumn.context.processor.method.event.common.ComponentDestructionEvent;
import com.github.czyzby.autumn.context.processor.method.invocation.ComponentMethodInvocation;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.lazy.LazyObjectMap;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Handles invocation of methods annotated with Destroy upon components' destruction.
 *
 * @author MJ */
public class DestroyAnnotationProcessor extends ComponentMethodAnnotationProcessor implements
		ComponentEventListener {
	private final ObjectMap<ContextComponent, Array<ComponentMethodInvocation>> scheduledInvocations =
			LazyObjectMap.newMapOfArrays();
	// Avoids allocation. There might be multiple cases of component destruction.
	private final Array<ComponentMethodInvocation> invocationsToExecute = GdxArrays.newArray();

	public DestroyAnnotationProcessor(final EventProcessor eventProcessor) {
		eventProcessor.registerListener(this);
	}

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return Destroy.class;
	}

	@Override
	public <Type> void processMethod(final ContextContainer context, final ContextComponent component,
			final Method method) {
		final Destroy destructionData = Reflection.getAnnotation(method, Destroy.class);
		scheduledInvocations.get(component).add(
				new ComponentMethodInvocation(destructionData.priority(), method, context, component));
	}

	@Override
	public Class<?> getEventType() {
		return ComponentDestructionEvent.class;
	}

	@Override
	public boolean processEvent(final Object event) {
		for (final ContextComponent componentToDestroy : ((ComponentDestructionEvent) event)
				.getComponentsToDestroy()) {
			invocationsToExecute.addAll(scheduledInvocations.get(componentToDestroy));
			scheduledInvocations.remove(componentToDestroy);
		}
		invocationsToExecute.sort();
		for (final ComponentMethodInvocation methodInvocation : invocationsToExecute) {
			methodInvocation.invoke();
		}
		invocationsToExecute.clear();
		return KEEP_AFTER_INVOCATION;
	}

	@Override
	public boolean isForcingMainThread() {
		return false;
	}
}
