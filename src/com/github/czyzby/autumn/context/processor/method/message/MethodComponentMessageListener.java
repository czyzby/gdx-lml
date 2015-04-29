package com.github.czyzby.autumn.context.processor.method.message;

import com.github.czyzby.autumn.annotation.method.OnMessage;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedMethod;

/** Default implementation of a listener created with a method annotated by OnMessage.
 *
 * @author MJ */
public class MethodComponentMessageListener implements ComponentMessageListener {
	private final ContextContainer context;
	private final Object listenerComponent;
	private final ReflectedMethod listenerMethod;
	private final String messageContent;
	private final boolean removeAfterInvocation;
	private final boolean forcesMainThread;
	private final boolean strict;

	public MethodComponentMessageListener(final OnMessage messageListenerData,
			final ReflectedMethod listenerMethod, final ContextComponent listenerComponent,
			final ContextContainer context) {
		this.context = context;
		this.listenerComponent = listenerComponent.getComponent();
		this.listenerMethod = listenerMethod;
		messageContent = messageListenerData.value();
		removeAfterInvocation = messageListenerData.removeAfterInvocation();
		forcesMainThread = messageListenerData.forceMainThread();
		strict = messageListenerData.strict();
	}

	@Override
	public String getMessageContent() {
		return messageContent;
	}

	@Override
	public boolean processMessage() {
		try {
			final Object result =
					listenerMethod.invoke(listenerComponent, context.prepareMethodParameters(listenerMethod));
			return result instanceof Boolean ? ((Boolean) result).booleanValue() || removeAfterInvocation
					: removeAfterInvocation;
		} catch (final Throwable exception) {
			if (strict) {
				throw new AutumnRuntimeException("Unable to execute method: " + listenerMethod
						+ " of component: " + listenerComponent + " on message: " + messageContent + ".",
						exception);
			}
		}
		return removeAfterInvocation;
	}

	@Override
	public boolean isForcingMainThread() {
		return forcesMainThread;
	}
}
