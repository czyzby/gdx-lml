package com.github.czyzby.autumn.mvc.component.ui.controller.impl;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Base class for controller wrappers of annotated objects.
 *
 * @author MJ */
public abstract class AbstractAnnotatedController {
	private final ObjectMap<String, Field> actorsToInject = GdxMaps.newObjectMap();
	protected final Object wrappedObject;
	private Field stageField;

	public AbstractAnnotatedController(final Object wrappedObject) {
		this.wrappedObject = wrappedObject;
	}

	/** Should be invoked after building controlled object.
	 *
	 * @param actorsMappedById actors mapped by their IDs returned by LML parser. */
	protected void injectReferencedActors(final ObjectMap<String, Actor> actorsMappedById) {
		for (final Entry<String, Actor> actor : actorsMappedById) {
			final Field actorField = actorsToInject.get(actor.key);
			if (actorField != null) {
				try {
					Reflection.setFieldValue(actorField, wrappedObject, actor.value);
				} catch (final ReflectionException exception) {
					throw new AutumnRuntimeException("Unable to inject actor: " + actor.value
							+ " mapped by ID: " + actor.key + " into controller: " + wrappedObject + ".",
							exception);
				}
			}
		}
	}

	/** @param stage will be injected into {@link com.github.czyzby.autumn.mvc.stereotype.ViewStage}-annotated
	 *            field, if present. */
	protected void injectStage(final Stage stage) {
		if (stageField != null) {
			try {
				Reflection.setFieldValue(stageField, wrappedObject, stage);
			} catch (final ReflectionException exception) {
				throw new AutumnRuntimeException("Unable to inject stage into controller: " + wrappedObject
						+ ".", exception);
			}
		}
	}

	/** Will inject null into {@link com.github.czyzby.autumn.mvc.stereotype.ViewStage}-annotated field. */
	protected void clearStage() {
		injectStage(null);
	}

	/** Allows to specify a field that should have an actor injected each time the controlled object is built.
	 *
	 * @param actorId ID of the actor specified with id tag attribute.
	 * @param field will hold the actor with the selected ID after parsing. */
	public void registerActorField(final String actorId, final Field field) {
		actorsToInject.put(actorId, field);
	}

	/** Allows to specify a field holding reference to current managed
	 * {@link com.badlogic.gdx.scenes.scene2d.Stage}.
	 *
	 * @param field will have current stage injected upon created and null upon stage destruction. */
	public void registerStageField(final Field field) {
		if (stageField != null) {
			throw new AutumnRuntimeException("Multiple stages fields annotated for view: " + wrappedObject
					+ ".");
		}
		stageField = field;
	}
}