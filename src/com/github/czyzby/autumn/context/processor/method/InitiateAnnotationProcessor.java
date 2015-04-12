package com.github.czyzby.autumn.context.processor.method;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.method.Initiate;
import com.github.czyzby.autumn.context.ContextComponent;
import com.github.czyzby.autumn.context.ContextContainer;
import com.github.czyzby.autumn.context.processor.method.invocation.ComponentMethodInvocation;
import com.github.czyzby.autumn.context.processor.method.message.ComponentMessageListener;
import com.github.czyzby.autumn.context.processor.method.message.common.AutumnRestrictedMessages;
import com.github.czyzby.autumn.reflection.wrapper.ReflectedMethod;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

/** Handles invocation of methods annotated with Initiate.
 *
 * @author MJ */
public class InitiateAnnotationProcessor extends ComponentMethodAnnotationProcessor implements
		ComponentMessageListener {
	private final Array<ComponentMethodInvocation> scheduledInvocations = GdxArrays.newArray();

	public InitiateAnnotationProcessor(final MessageProcessor messageProcessor) {
		messageProcessor.registerListener(this);
	}

	@Override
	public Class<? extends Annotation> getProcessedAnnotationClass() {
		return Initiate.class;
	}

	@Override
	public <Type> void processMethod(final ContextContainer context, final ContextComponent component,
			final ReflectedMethod method) {
		final Initiate initiationData = method.getAnnotation(Initiate.class);
		scheduledInvocations.add(new ComponentMethodInvocation(initiationData.priority(), method, context,
				component));
	}

	@Override
	public String getMessageContent() {
		return AutumnRestrictedMessages.COMPONENT_INITIATION;
	}

	@Override
	public boolean processMessage() {
		scheduledInvocations.sort();
		for (final ComponentMethodInvocation methodInvocation : scheduledInvocations) {
			methodInvocation.invoke();
		}
		scheduledInvocations.clear();
		return KEEP_AFTER_INVOCATION;
	}

	@Override
	public boolean isForcingMainThread() {
		return false;
	}
}
