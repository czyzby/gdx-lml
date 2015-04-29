package com.github.czyzby.autumn.annotation.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Turns the method into a 'listener' for messages with the selected body. If the method returns a boolean, it
 * will decide whether the listener should be removed after invocation - true keeps the listener, false
 * removes it from the processor. A lighter alternative to OnEvent annotation, although it does not allow to
 * pass additional data in an event object.
 *
 * @author MJ */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OnMessage {
	/** If a message with this content is posted, selected method will be invoked. Note that method parameters
	 * can be context components and will be properly passed to the method. */
	String value();

	/** If set to true, message listener created with this method will be removed from context after first
	 * invocation. Note that when set to true, it will cause the listener to be removed even if the method
	 * itself returns false (which would normally keep the listener). Defaults to false. */
	boolean removeAfterInvocation() default false;

	/** If true, method invocations will be executed in the main thread using Gdx.app.postRunnable. Defaults to
	 * false. */
	boolean forceMainThread() default false;

	/** If true, throws exception if unable to execute event. Otherwise, reflection and context exceptions are
	 * ignored. */
	boolean strict() default true;
}
