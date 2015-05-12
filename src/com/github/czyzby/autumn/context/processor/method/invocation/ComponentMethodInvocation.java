package com.github.czyzby.autumn.context.processor.method.invocation;

import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.kiwi.util.common.Comparables;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Holds information about a single method that should be invoked upon met conditions.
 *
 * @author MJ */
public class ComponentMethodInvocation implements Comparable<ComponentMethodInvocation> {
	private final int priority;
	private final Method method;
	private final ContextContainer context;
	private final ContextComponent component;

	/** @param priority method priority. Before processing, invocations are usually sorted by their priority and
	 *            invoked one by one.
	 * @param method a reference to the original annotated method.
	 * @param context contains the component.
	 * @param component contains the method. */
	public ComponentMethodInvocation(final int priority, final Method method, final ContextContainer context,
			final ContextComponent component) {
		this.priority = priority;
		this.method = method;
		this.context = context;
		this.component = component;
		for (final Class<?> componentClass : method.getParameterTypes()) {
			context.requestToWakeLazyComponent(context.extractFromContext(componentClass));
		}
	}

	/** Invokes the wrapped method, extracting method parameters from context. */
	public void invoke() {
		try {
			Reflection
					.invokeMethod(method, component.getComponent(), context.prepareMethodParameters(method));
		} catch (final ReflectionException exception) {
			throw new AutumnRuntimeException("Unable to invoke method: " + method + ".", exception);
		}
	}

	@Override
	public int compareTo(final ComponentMethodInvocation invocation) {
		// Reversed order, method invocations with higher priority are invoked first.
		return Comparables.normalizeResult(invocation.priority - priority);
	}
}
