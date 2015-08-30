package com.github.czyzby.autumn.mvc.component.ui.controller.impl;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.czyzby.autumn.error.AutumnRuntimeException;
import com.github.czyzby.autumn.mvc.component.ui.dto.injection.ActorFieldInjection;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.reflection.Reflection;

/** Base class for controller wrappers of annotated objects.
 *
 * @author MJ */
public abstract class AbstractAnnotatedController {
	private final Array<ActorFieldInjection> actorsToInject = GdxArrays.newArray();
	protected final Object wrappedObject;
	private Field stageField;

	public AbstractAnnotatedController(final Object wrappedObject) {
		this.wrappedObject = wrappedObject;
	}

	/** Should be invoked after building controlled object.
	 *
	 * @param actorsMappedById actors mapped by their IDs returned by LML parser. */
	protected void injectReferencedActors(final ObjectMap<String, Actor> actorsMappedById) {
		for (final ActorFieldInjection injection : actorsToInject) {
			injection.injectActors(wrappedObject, actorsMappedById);
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

	/** Allows to specify a field that should have an actor (or multiple actors) injected each time the
	 * controlled object is built.
	 *
	 * @param actorFieldInjection contains data about the field and reference actor IDs. */
	public void registerActorField(final ActorFieldInjection actorFieldInjection) {
		actorsToInject.add(actorFieldInjection);
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