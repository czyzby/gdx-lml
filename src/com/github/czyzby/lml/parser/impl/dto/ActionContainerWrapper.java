package com.github.czyzby.lml.parser.impl.dto;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;
import com.github.czyzby.lml.parser.impl.annotation.ViewAction;

/** Wraps around an {@link com.github.czyzby.lml.parser.impl.dto.ActionContainer}, providing access to its
 * methods and fields.
 *
 * @author MJ */
public class ActionContainerWrapper {
	private final ActionContainer actionContainer;
	private final ObjectMap<String, Method> annotatedMethods = GdxMaps.newObjectMap();

	public ActionContainerWrapper(final ActionContainer actionContainer) {
		this.actionContainer = actionContainer;
		mapAnnotatedMethods();
	}

	private void mapAnnotatedMethods() {
		try {
			for (final Method method : ClassReflection.getDeclaredMethods(actionContainer.getClass())) {
				final ViewAction actionData = Reflection.getAnnotation(method, ViewAction.class);
				if (actionData != null) {
					annotatedMethods.put(actionData.value(), method);
				}
			}
		} catch (final Throwable exception) {
			// Somewhat expected. Class might be not reflected.
		}
	}

	/** @return wrapped action container. */
	public ActionContainer getActionContainer() {
		return actionContainer;
	}

	/** @param methodId ID of the referenced method.
	 * @return Autumn-reflected method annotated with
	 *         {@link com.github.czyzby.lml.parser.impl.annotation.ViewAction}. */
	public Method getNamedMethod(final String methodId) {
		return annotatedMethods.get(methodId);
	}

	/** @param methodName name of the possibly contained method.
	 * @param parameterClass class of the expected parameter.
	 * @return method with passed name and one or zero parameters. */
	public Method getMethod(final String methodName, final Class<?> parameterClass)
			throws ReflectionException {
		try {
			return ClassReflection.getMethod(actionContainer.getClass(), methodName, parameterClass);
		} catch (final Exception exception) {
			return ClassReflection.getMethod(actionContainer.getClass(), methodName);
		}
	}

	/** @param fieldName name of the field.
	 * @return field with the selected name or null. */
	public Field getField(final String fieldName) {
		try {
			return ClassReflection.getDeclaredField(actionContainer.getClass(), fieldName);
		} catch (final ReflectionException exception) {
			// Somewhat expected. Field unavailable.
			return null;
		}
	}
}