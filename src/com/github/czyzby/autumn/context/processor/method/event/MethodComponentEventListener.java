package com.github.czyzby.autumn.context.processor.method.event;

import com.badlogic.gdx.utils.reflect.Method;
import com.github.czyzby.autumn.annotation.method.OnEvent;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Default implementation of a listener created with a method annotated by OnEvent.
 *
 * @author MJ */
public class MethodComponentEventListener implements ComponentEventListener {
	private final ContextContainer context;
	private final Object listenerComponent;
	private final Method listenerMethod;
	private final Class<?> eventType;
	private final boolean removeAfterInvocation;
	private final boolean forcesMainThread;
	private final boolean strict;

	public MethodComponentEventListener(final OnEvent eventListenerData, final Method listenerMethod,
			final ContextComponent listenerComponent, final ContextContainer context) {
		this.context = context;
		this.listenerComponent = listenerComponent.getComponent();
		this.listenerMethod = listenerMethod;
		eventType = eventListenerData.value();
		removeAfterInvocation = eventListenerData.removeAfterInvocation();
		forcesMainThread = eventListenerData.forceMainThread();
		strict = eventListenerData.strict();
	}

	@Override
	public Class<?> getEventType() {
		return eventType;
	}

	@Override
	public boolean processEvent(final Object event) {
		try {
			final Object result =
					Reflection
							.invokeMethod(listenerMethod, listenerComponent, prepareMethodParameters(event));
			return result instanceof Boolean ? ((Boolean) result).booleanValue() || removeAfterInvocation
					: removeAfterInvocation;
		} catch (final Throwable exception) {
			if (strict) {
				throw new AutumnRuntimeException("Unable to execute event: " + event + " for method: "
						+ listenerMethod + " of component: " + listenerComponent + ".", exception);
			}
		}
		return removeAfterInvocation;
	}

	/** Custom method parameters preparation. Allows to pass event objects to the invoked methods. */
	private Object[] prepareMethodParameters(final Object event) {
		final Class<?>[] parameterTypes = listenerMethod.getParameterTypes();
		if (parameterTypes.length == 0) {
			return Reflection.EMPTY_OBJECT_ARRAY;
		}
		final Object[] parameters = new Object[parameterTypes.length];
		for (int parameterIndex = 0; parameterIndex < parameterTypes.length; parameterIndex++) {
			if (eventType.equals(parameterTypes[parameterIndex])) {
				parameters[parameterIndex] = event;
			} else {
				parameters[parameterIndex] = context.getFromContext(parameterTypes[parameterIndex]);
			}
		}
		return parameters;
	}

	@Override
	public boolean isForcingMainThread() {
		return forcesMainThread;
	}
}
